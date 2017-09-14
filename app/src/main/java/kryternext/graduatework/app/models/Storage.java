package kryternext.graduatework.app.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

import kryternext.graduatework.AccountActivity;
import kryternext.graduatework.app.services.DatabaseController;

public class Storage {
    private DatabaseController storage;
    private Context context;
    final private String TAG = "STITCH-SDK:";

    public Storage(Context context, String databaseName) {
        this.context = context;
        this.storage = new DatabaseController(context, databaseName);
    }

    public void logIn(final UserAuth user) {
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
                            Intent intent = new Intent(Storage.this.context, AccountActivity.class);
                            User authorizedUser = new User();
                            authorizedUser.setUsername(account.getString("username"));
                            authorizedUser.setEmail(account.getString("email"));
                            authorizedUser.setShopName(account.getString("shop_name"));
                            authorizedUser.setType(account.getString("type"));
                            authorizedUser.setBalance(account.getDouble("balance"));
                            intent.putExtra("USER", authorizedUser);
                            context.startActivity(intent);
                            return true;
                        } else {
                            showMessage(context);
                            Log.i(TAG, "Incorrect username or password!");
                            return false;
                        }
                    } else {
                        showMessage(context);
                        Log.i(TAG, "Account not found!");
                        return false;
                    }
                } else {
                    showMessage(context);
                    Log.e(TAG, "Something went wrong!");
                    return false;
                }
            }
        });
    }

    private void showMessage(Context context) {
        Toast.makeText(context, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
    }

    public boolean register() {
        return true;
    }
}
