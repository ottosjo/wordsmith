package org.alphadev.storage.mongo;

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

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.quarkus.runtime.Shutdown;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Persistence layer for user text input to/from mongo db
 */
@ApplicationScoped
public class MongoUserTextInputStorage implements UserTextInputStorage {

	public static final String SESSION_ID_KEY = "sessionId";
	public static final String TEXT_KEY = "text";
	public static final String REVERSED_TEXT_KEY = "reversedText";
	public static final String TIMESTAMP_KEY = "timestamp";
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final MongoDatabaseInitializer initializer = new MongoDatabaseInitializer();

	private final MongoClient mongoClient;
	private final Clock clock;

	public MongoUserTextInputStorage() {
		mongoClient = createMongoClient();
		clock = Clock.system(ZoneId.of("Europe/Stockholm")); // TODO - time zone stuff...
	}

	private MongoClient createMongoClient() {
		var config = ConfigProvider.getConfig();
		var connectionString = config.getConfigValue("mongodb.connectionstring");
		var password = config.getConfigValue("mongo.password");
		log.info("Connection string: {}", connectionString);
		log.info("Mongo password length: {}", password.getValue().length());

		var mongoConnection = connectionString.getValue().replace("<password>", password.getValue());
		var client = MongoClients.create(mongoConnection);
		log.info("Mongo client: {}", client);
		var db = client.getDatabase(MongoConstant.DB_NAME);
		initializer.init(db);
		return client;
	}

	private MongoDatabase getDb() {
		return mongoClient.getDatabase(MongoConstant.DB_NAME);
	}

	@Override
	public boolean write(final String sessionId, final String text, final String reversedText) {
		var document = new Document(SESSION_ID_KEY, sessionId)
				.append(TIMESTAMP_KEY, Instant.now(clock).toEpochMilli())
				.append(TEXT_KEY, text)
				.append(REVERSED_TEXT_KEY, reversedText);
		var res = getCollection().insertOne(document);
		return res.wasAcknowledged();
	}

	@Override
	public List<TextReverseItem> read(final String sessionId) {
		var query = new Document(SESSION_ID_KEY, sessionId);
		var docs = getCollection().find(query);
		return convertToDomain(docs);
	}

	private static ArrayList<TextReverseItem> convertToDomain(final FindIterable<Document> docs) {
		var items = new ArrayList<TextReverseItem>();
		for (final Document doc : docs) {
			var item = new TextReverseItem(
					doc.getString(SESSION_ID_KEY),
					Instant.ofEpochMilli(doc.getLong(TIMESTAMP_KEY)),
					doc.getString(TEXT_KEY),
					doc.getString(REVERSED_TEXT_KEY)
			);
			items.add(item);
		}
		return items;
	}

	private MongoCollection<Document> getCollection() {
		return getDb().getCollection(MongoConstant.Collection.REVERSIBLE_TEXT_INPUT);
	}

	@Shutdown
	void shutdown() {
		mongoClient.close();
	}
}
