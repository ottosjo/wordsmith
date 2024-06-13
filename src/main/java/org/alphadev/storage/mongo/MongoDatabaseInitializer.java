package org.alphadev.storage.mongo;

import static org.alphadev.storage.mongo.MongoConstant.Collection.REVERSIBLE_TEXT_INPUT;

import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Creates the collections and indices for the mongo db if missing
 */
@ApplicationScoped
public class MongoDatabaseInitializer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public void init(MongoDatabase database) {
		addMissingCollections(database);
		addIndices(database);
	}

	private void addIndices(final MongoDatabase database) {
		createSessionIdIndex(database);
		// add other index creations here
	}

	private static void createSessionIdIndex(final MongoDatabase database) {
		var collection = database.getCollection(REVERSIBLE_TEXT_INPUT);

		var index = new Document("sessionId", 1);  // 1 for ascending, -1 for descending
		var indexOptions = new IndexOptions().unique(false);
		collection.createIndex(index, indexOptions);
	}

	private void addMissingCollections(final MongoDatabase database) {
		var requiredCollections = Set.of(REVERSIBLE_TEXT_INPUT);
		var existingCollections = getExistingCollections(database);
		for (var collectionName : requiredCollections) {
			if (existingCollections.contains(collectionName)) {
				log.info("Mongo collection already exists: {}", collectionName);
			} else {
				log.info("Mongo collection is missing and will be created: {}", collectionName);
				database.createCollection(collectionName);
			}
		}
	}

	private static HashSet<Object> getExistingCollections(final MongoDatabase database) {
		var collectionNames = database.listCollectionNames();
		var existingCollections = new HashSet<>();
		collectionNames.forEach(existingCollections::add);
		return existingCollections;
	}
}
