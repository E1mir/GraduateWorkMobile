package kryternext.graduatework.app.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.stitch.android.services.mongodb.MongoClient;

import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

import kryternext.graduatework.app.DatabaseController;

public class Storage {
    private DatabaseController storage;
    private Context context;
    final private String TAG = "STITCH-SDK:";

    public Storage(Context context, String databaseName) {
        this.context = context;
        this.storage = new DatabaseController(context, databaseName);
    }

    public boolean logIn(final UserAuth user) {
        List<Document> orCondition = new LinkedList<>();
        orCondition.add(new Document("username", user.getUsername()));
        orCondition.add(new Document("email", user.getUsername()));
        Document query = new Document();
        query.append("$or", orCondition);
        this.storage.getCollection("accounts").find(query).continueWith(new Continuation<List<Document>, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    if (task.getResult().size() > 0) {
                        final Document account = task.getResult().get(0);
                        String databasePass = account.getString("password");
                        String requestPass = user.getPassword();
                        if (databasePass.equals(requestPass)) {
                            Log.i(TAG, "Authorized!");
                            return true;
                        } else {
                            Log.i(TAG, "Incorrect username or password!");
                            return false;
                        }
                    } else {
                        Log.i(TAG, "Account not found!");
                        return false;
                    }
                } else {
                    Log.e(TAG, "Something went wrong!");
                    return false;
                }
            }
        });
        return true;
    }
}
