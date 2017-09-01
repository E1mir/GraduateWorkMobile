package kryternext.graduatework.app;

import android.content.Context;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.IOException;
import java.util.Properties;

public class DatabaseController {
    private static Properties settings = new Properties();
    private MongoClient mongoClient;


    public DatabaseController(Context context) {
        try {
            settings.load(context.getAssets().open("settings.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String connectionString = settings.getProperty("connectionString").trim();
        System.out.println(connectionString);
        this.mongoClient = new MongoClient(new MongoClientURI(connectionString));
    }
}
