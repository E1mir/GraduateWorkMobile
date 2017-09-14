package kryternext.graduatework.app.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import kryternext.graduatework.AccountActivity;
import kryternext.graduatework.app.services.DatabaseController;

public class Storage {
    private DatabaseController storage;
    private Context context;
    private AppCompatActivity activity;
    final private String TAG = "STITCH-SDK:";

    public Storage(Context context, String databaseName) {
        this.context = context;
        this.storage = new DatabaseController(context, databaseName);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
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

    public void getTypes(final Spinner spinner) {
        Document none = new Document();
        this.storage.getCollection("types").find(none).continueWith(new Continuation<List<Document>, Object>() {
            @Override
            public Object then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    List<Document> typesDoc = task.getResult();
                    ArrayList<String> types = new ArrayList<>();
                    for (Document type : typesDoc) {
                        types.add(type.getString("name"));
                    }
                    Collections.sort(types);
                    types.add(0, "Select type");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, types);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    return null;
                }
                return null;
            }
        });
    }

    public DatabaseController getMainStorage() {
        return this.storage;
    }

    public void register(final User newUser) {
        final Document user = new Document();
        user.append("username", newUser.getUsername());
        user.append("email", newUser.getEmail());
        user.append("type", newUser.getType());
        user.append("password", newUser.getPassword());
        user.append("shop_name", newUser.getShopName());
        user.append("permission", "default");
        user.append("balance", 0.0);
        Document checkingQueryForUsername = new Document();
        checkingQueryForUsername.append("username", newUser.getUsername());
        this.storage.getCountByQuery("accounts", checkingQueryForUsername).continueWith(new Continuation<Integer, Object>() {
            @Override
            public Object then(@NonNull Task<Integer> task) throws Exception {
                if (task.isSuccessful()) {
                    if (task.getResult() > 0) {
                        alert("Username");
                        return null;
                    } else {
                        final Document checkingQueryForEmail = new Document();
                        checkingQueryForEmail.append("email", newUser.getEmail());
                        storage.getCountByQuery("accounts", checkingQueryForEmail).continueWith(new Continuation<Integer, Object>() {
                            @Override
                            public Object then(@NonNull Task<Integer> task) throws Exception {
                                if (task.isSuccessful()) {
                                    if (task.getResult() > 0) {
                                        alert("Email");
                                        return null;
                                    } else {
                                        Toast.makeText(context, "Registered!", Toast.LENGTH_SHORT).show();
                                        storage.insert("accounts", user);
                                        activity.finish();
                                        return null;
                                    }
                                }
                                return null;
                            }
                        });
                    }
                }
                return null;
            }
        });
    }

    private void alert(String message) {
        Toast.makeText(context, String.format("%s has already taken!", message), Toast.LENGTH_SHORT).show();
    }
}
