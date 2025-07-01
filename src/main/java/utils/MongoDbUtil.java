package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoDbUtil {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDbUtil(String uri, String dbName) {
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    public void insertOne(String collectionName, Document doc) {
        getCollection(collectionName).insertOne(doc);
    }

    public List<Document> find(String collectionName, Bson filter) {
        List<Document> results = new ArrayList<>();
        getCollection(collectionName).find(filter).into(results);
        return results;
    }

    public void updateOne(String collectionName, Bson filter, Bson update) {
        getCollection(collectionName).updateOne(filter, update);
    }

    public void deleteOne(String collectionName, Bson filter) {
        getCollection(collectionName).deleteOne(filter);
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
} 