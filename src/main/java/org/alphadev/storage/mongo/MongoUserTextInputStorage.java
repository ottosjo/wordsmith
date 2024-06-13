package org.alphadev.storage.mongo;

import java.util.ArrayList;
import java.util.List;

import org.alphadev.storage.UserTextInputStorage;
import org.alphadev.text.reverse.TextReverseItem;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.quarkus.runtime.Shutdown;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Persistence layer for user text input to/from mongo db
 */
@ApplicationScoped
public class MongoUserTextInputStorage implements UserTextInputStorage {

	public static final String SESSION_ID_KEY = "sessionId";
	public static final String TEXT_KEY = "text";

	@ConfigProperty(name = "mongodb.connectionString")
	String connectionString;

	@Inject
	MongoDatabaseInitializer initializer;

	private final MongoClient mongoClient;

	private MongoUserTextInputStorage() {
		mongoClient = MongoClients.create(connectionString);
		initializer.init(getDb());
	}

	private MongoDatabase getDb() {
		return mongoClient.getDatabase(MongoConstant.DB_NAME);
	}

	@Override
	public boolean write(final String sessionId, final String text) {
		var document = new Document(SESSION_ID_KEY, sessionId)
				.append(TEXT_KEY, text);
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
			var item = new TextReverseItem(doc.getString(SESSION_ID_KEY), doc.getString(TEXT_KEY));
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
