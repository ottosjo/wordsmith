package org.alphadev.storage.mongo;

import static org.alphadev.storage.mongo.MongoConstant.SESSION_ID_KEY;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.alphadev.storage.UserTextInputStorage;
import org.alphadev.text.reverse.TextReverseItem;
import org.bson.Document;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import io.quarkus.runtime.Shutdown;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Persistence layer for user text input to/from mongo db
 */
@ApplicationScoped
public class MongoUserTextInputStorage implements UserTextInputStorage {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final MongoDatabaseInitializer initializer = new MongoDatabaseInitializer();

	private final MongoClient mongoClient;
	private final Clock clock;

	public MongoUserTextInputStorage() {
		mongoClient = createMongoClient();
		clock = Clock.system(ZoneId.of("Europe/Stockholm")); // TODO - timestamp zone stuff...
	}

	private MongoClient createMongoClient() {
		try {
			var mongoConnection = getMongoConnectionString();
			var client = MongoClients.create(mongoConnection);
			log.info("Mongo client was created successfully: {}", client);
			var db = client.getDatabase(MongoConstant.DB_NAME);
			initializer.init(db);
			return client;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to create mongo client", ex);
		}
	}

	private String getMongoConnectionString() {
		var config = ConfigProvider.getConfig();
		var connectionString = config.getConfigValue("mongodb.connectionstring");
		var password = config.getConfigValue("mongo.password");
		log.info("Connection string: {}", connectionString);
		return connectionString.getValue().replace("<password>", password.getValue());
	}

	@Override
	public boolean write(final String sessionId, final String text, final String reversedText) {
		var item = new TextReverseItem(
				sessionId, Instant.now(clock).toEpochMilli(), text, reversedText
		);
		var res = getCollection().insertOne(item);
		return res.wasAcknowledged();
	}

	@Override
	public List<TextReverseItem> read(final String sessionId) {
		var query = new Document(SESSION_ID_KEY, sessionId);
		var items = new ArrayList<TextReverseItem>();
		getCollection().find(query, TextReverseItem.class).forEach(items::add);
		return items;
	}

	private MongoCollection<TextReverseItem> getCollection() {
		return mongoClient
				.getDatabase(MongoConstant.DB_NAME)
				.getCollection(MongoConstant.Collection.REVERSIBLE_TEXT_INPUT, TextReverseItem.class);
	}

	@Shutdown
	void shutdown() {
		mongoClient.close();
	}
}
