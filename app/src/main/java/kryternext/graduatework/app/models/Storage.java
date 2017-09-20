package kryternext.graduatework.app.models;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kryternext.graduatework.AccountActivity;
import kryternext.graduatework.R;
import kryternext.graduatework.app.adapters.GoodsListAdapter;
import kryternext.graduatework.app.services.DatabaseController;
import kryternext.graduatework.app.services.StringUtils;

/**
 * This is storage class, which using for interact application with database.
 */
public class Storage {
    private DatabaseController storage;
    private Context context;
    private AppCompatActivity activity;
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

    public DatabaseController getMainStorage() {
        return this.storage;
    }

    public void getCategoriesByType(String type, final Spinner categories) {
        this.storage.getCollection("types").find(new Document("name", type)).continueWith(new Continuation<List<Document>, Object>() {
            @Override
            public Object then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    Document typesDoc = task.getResult().get(0);
                    String categoriesStr = typesDoc.getString("categories");
                    ArrayList<String> categoriesList = StringUtils.getListFromStringSplit(categoriesStr, ",");
                    Collections.sort(categoriesList);
                    categoriesList.add(0, "Category:");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoriesList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(dataAdapter);
                    return null;
                }
                return null;
            }
        });
    }

    public void getGoodsByType(String type, final ListView products, final TextView totalTV, final Map<String, String> map, final String searchText, final String searchCategory) {
        Document query = new Document();
        query.append("type", type);
        query.append("count", new Document("$gt", 0));
        this.storage.getCollection("warehouse").find(query).continueWith(new Continuation<List<Document>, Object>() {
            @Override
            public Object then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    List<Document> warehouse = task.getResult();
                    ArrayList<Product> availableProducts = new ArrayList<>();
                    for (Document product : warehouse) {
                        Product newProduct = new Product();
                        newProduct.setProductName(product.getString("name"));
                        if (searchText != null) {
                            if (!newProduct.getProductName().toLowerCase().contains(searchText))
                                continue;
                        }
                        newProduct.setCategory(product.getString("category"));
                        if (searchCategory != null) {
                            if (!newProduct.getCategory().toLowerCase().contains(searchCategory))
                                continue;
                        }
                        newProduct.setProductType(product.getString("type"));
                        newProduct.setProductDescription(product.getString("description"));
                        newProduct.setCount(product.getInteger("count"));
                        newProduct.setPrice(product.getDouble("price"));
                        availableProducts.add(newProduct);
                    }
                    products.setAdapter(new GoodsListAdapter(context, availableProducts, totalTV, map));
                }
                return null;
            }
        });
    }

    public void order(Order order) {
        Document newOrder = new Document();
        newOrder.append("order_id", order.getOrderTimestamp());
        newOrder.append("account", order.getUser().getUsername().toLowerCase());
        newOrder.append("total_cost", order.getCost());
        newOrder.append("date", StringUtils.getDateFromTimestamp(order.getOrderTimestamp()));
        newOrder.append("status", "Awaiting confirmation");
        newOrder.append("confirm_timestamp", 0L);
        List<Document> orderedProducts = new ArrayList<>();
        for (Map.Entry<String, String> pair : order.getProducts().entrySet()) {
            Document query = new Document("name", pair.getKey());
            String[] productData = pair.getValue().split("-");
            int productCount = Integer.parseInt(productData[1]);
            this.storage.save("warehouse", query, new Document("count", productCount));
            orderedProducts.add(new Document().append("name", pair.getKey()).append("count", pair.getValue()));
        }
        Document account = new Document("username", order.getUser().getUsername().toLowerCase());
        this.storage.save("accounts", account, new Document("balance", order.getUser().getBalance()));
        newOrder.append("order", orderedProducts);
        this.storage.insert("orders", newOrder);
        activity.finish();
    }

    public void refreshPage(String username) {
        User user = new User();
        Document query = new Document();
        query.append("username", username);
        final TableLayout account = (TableLayout) this.activity.findViewById(R.id.account_content_layout);
        final NavigationView navigationView = (NavigationView) this.activity.findViewById(R.id.nav_view);

        this.storage.getCollection("accounts").find(query).continueWith(new Continuation<List<Document>, Object>() {
            @Override
            public Object then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    Document accountDoc = task.getResult().get(0);
                    TextView usernameTV = (TextView) account.findViewById(R.id.account_username_USER);
                    TextView emailTV = (TextView) account.findViewById(R.id.account_email_USER);
                    TextView shopTV = (TextView) account.findViewById(R.id.account_shop_USER);
                    TextView typeTV = (TextView) account.findViewById(R.id.account_type_USER);
                    TextView balanceTV = (TextView) account.findViewById(R.id.account_balance_USER);
                    Menu menu = navigationView.getMenu();
                    MenuItem balanceNav = menu.findItem(R.id.nav_balance);
                    usernameTV.setText(getFormattedText("Username", accountDoc.getString("username")));
                    emailTV.setText(getFormattedText("Email", accountDoc.getString("email")));
                    shopTV.setText(getFormattedText("Shop", accountDoc.getString("shop_name")));
                    typeTV.setText(getFormattedText("Type", accountDoc.getString("type")));
                    balanceTV.setText(String.format(Locale.ENGLISH, "%.2f $", accountDoc.getDouble("balance")));
                    balanceNav.setTitle(String.format(Locale.ENGLISH, "%.2f $", accountDoc.getDouble("balance")));
                }
                return null;
            }
        });
        final ListView ordersLV = (ListView) this.activity.findViewById(R.id.orders_content_layout);
        this.storage.getCollection("orders").find(new Document("account", username)).continueWith(new Continuation<List<Document>, Object>() {
            @Override
            public Object then(@NonNull Task<List<Document>> task) throws Exception {
                if (task.isSuccessful()) {
                    List<Document> orders = task.getResult();
                    TextView ordersTV = (TextView) Storage.this.activity.findViewById(R.id.account_orders_USER);
                    ordersTV.setText(getFormattedText("Orders", String.valueOf(orders.size())));
                    ArrayList<String> orderList = new ArrayList<String>();
                    for (int i = 0; i < orders.size(); i++) {
                        int index = i + 1;
                        try {
                            Long orderID = orders.get(i).getLong("order_id");
                            Long confirmTimestamp = orders.get(i).getLong("confirm_timestamp");
                            String orderDate = StringUtils.getDateFromTimestamp(orderID);
                            String confirmDate = StringUtils.getDateFromTimestamp(confirmTimestamp);
                            String status = orders.get(i).getString("status");
                            if (!confirmDate.isEmpty()) {
                                status += " at " + confirmDate;
                            }
                            Double totalCost = orders.get(i).getDouble("total_cost");
                            orderList.add(String.format(Locale.ENGLISH, "%d: %d %s %s    %.2f $", index, orderID, orderDate, status, totalCost));
                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Storage.this.context, android.R.layout.simple_list_item_1, orderList);
                    ordersLV.setAdapter(arrayAdapter);
                }
                return null;
            }
        });

    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private void showMessage(Context context) {
        Toast.makeText(context, "Incorrect username or password!", Toast.LENGTH_SHORT).show();
    }

    private void alert(String message) {
        Toast.makeText(context, String.format("%s has already taken!", message), Toast.LENGTH_SHORT).show();
    }

    private String getFormattedText(String field, String text) {
        return String.format("%s: %s", field, StringUtils.getCapitalizedText(text));
    }
}
