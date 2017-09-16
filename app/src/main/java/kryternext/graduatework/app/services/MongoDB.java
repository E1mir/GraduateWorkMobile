package kryternext.graduatework.app.services;

import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.services.mongodb.MongoClient;

import org.bson.Document;

import java.util.List;

/**
 * The main interface of MongoDB database
 */
public interface MongoDB {
    void insert(String collection, Document document);

    void save(String collection, Document query, Document document);

    Task<Integer> getCountByQuery(String collection, Document query);

    Task<List<Document>> getListByQuery(String collection, Document query);

    MongoClient.Collection getCollection(String collection);
}
