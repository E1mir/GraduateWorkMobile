package kryternext.graduatework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import kryternext.graduatework.app.models.Order;
import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.User;

public class NewOrder extends AppCompatActivity {
    private ListView availableGoods;
    private Storage storage;
    private User user;
    private TextView totalTV;
    private EditText searchET;
    private Spinner categorySP;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        this.storage = new Storage(this, "wms");
        this.storage.setActivity(this);
        this.order = new Order();
        this.user = (User) getIntent().getSerializableExtra("USER");
        availableGoods = (ListView) findViewById(R.id.goods_list);
        totalTV = (TextView) findViewById(R.id.totalPrice);
        searchET = (EditText) findViewById(R.id.search_text);
        categorySP = (Spinner) findViewById(R.id.search_category);
        totalTV.setText(String.format(Locale.ENGLISH, "Total: %d", 0));
        initList();

    }

    public void initList() {
        this.storage.getCategoriesByType(user.getType(), categorySP);
        this.storage.getGoodsByType(user.getType(), availableGoods, totalTV, order.orderProductList, null, null);
    }

    public void order(View view) {
        double totalValue = Double.parseDouble(totalTV.getText().toString().split(":")[1].trim());
        double balance = user.getBalance();
        if (totalValue == 0) {
            Toast.makeText(this, "Please choose the products!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalValue <= balance) {
            user.setBalance(balance - totalValue);
            order.setUser(user);
            order.setCost(totalValue);
            order.setOrderTimestamp(System.currentTimeMillis() / 1000);
            Toast.makeText(this, "We have received your order.", Toast.LENGTH_SHORT).show();
            this.storage.order(order);
        } else
            Toast.makeText(this, String.format(Locale.ENGLISH, "Not enough money: %.2f$", (balance - totalValue)), Toast.LENGTH_SHORT).show();
    }

    public void search(View view) {
        String searchTxt = searchET.getText().toString().toLowerCase().trim();
        String category = categorySP.getSelectedItem().toString().toLowerCase().trim();
        if (searchTxt.isEmpty() && category.equals("category:")) {
            this.storage.getGoodsByType(user.getType(), availableGoods, totalTV, order.orderProductList, null, null);
            return;
        }
        if (!searchTxt.isEmpty() && category.equals("category:")) {
            this.storage.getGoodsByType(user.getType(), availableGoods, totalTV, order.orderProductList, searchTxt, null);
            return;
        }
        if (!category.equals("category:") && searchTxt.isEmpty()) {
            this.storage.getGoodsByType(user.getType(), availableGoods, totalTV, order.orderProductList, null, category);
            return;
        }
        if (!searchTxt.isEmpty() && !category.equals("category:")) {
            this.storage.getGoodsByType(user.getType(), availableGoods, totalTV, order.orderProductList, searchTxt, category);
        }
    }
}
