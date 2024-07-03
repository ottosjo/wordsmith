package org.alphadev.storage.mongo;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.alphadev.storage.mongo.MongoConstant.SESSION_ID_KEY;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.alphadev.config.WordsmithConfig;
import org.alphadev.storage.UserTextInputStorage;
import org.alphadev.text.reverse.TextReverseItem;
import org.bson.Document;
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
	private static final String CONNECTION_STRING_KEY = "mongoConnectionString";

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final MongoDatabaseInitializer initializer = new MongoDatabaseInitializer();
	private final WordsmithConfig config;
	private final Clock clock;
	private MongoClient mongoClient;

	public MongoUserTextInputStorage(WordsmithConfig config) {
		this.config = config;
		this.clock = Clock.system(ZoneId.of("Europe/Stockholm")); // TODO - timestamp zone stuff...
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

	private String getMongoConnectionString() {
		var connectionString = config.getSecret(CONNECTION_STRING_KEY);
		if (isNullOrEmpty(connectionString)) {
			throw new IllegalStateException("Mongo connection string is required: " + CONNECTION_STRING_KEY);
		}
		return connectionString;
	}

	private MongoCollection<TextReverseItem> getCollection() {
		return getMongoClient()
				.getDatabase(MongoConstant.DB_NAME)
				.getCollection(MongoConstant.Collection.REVERSIBLE_TEXT_INPUT, TextReverseItem.class);
	}

	private synchronized MongoClient getMongoClient() {
		if (mongoClient == null) {
			mongoClient = createMongoClient();
		}
		return mongoClient;
	}

	private MongoClient createMongoClient() {
		try {
			var mongoConnection = getMongoConnectionString();
			log.info("Creating mongo client...");
			var client = MongoClients.create(mongoConnection);
			log.info("Mongo client was created successfully");
			var db = client.getDatabase(MongoConstant.DB_NAME);
			initializer.init(db);
			return client;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to create mongo client", ex);
		}
	}

	@Shutdown
	void shutdown() {
		log.info("Shutting down mongo client...");
		if (mongoClient != null) {
			mongoClient.close();
		}
	}
}
