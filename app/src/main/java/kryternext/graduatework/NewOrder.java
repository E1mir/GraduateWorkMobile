package kryternext.graduatework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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
        totalTV.setText(String.format(Locale.ENGLISH, "Total: %d", 0));
        initList();
        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    initList();
                } else {
                    search(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    public void initList() {
        this.storage.getGoodsByType(this.user.getType(), this.availableGoods, this.totalTV, this.order.orderProductList, null);
    }

    public void search(String searchText) {
        this.storage.getGoodsByType(this.user.getType(), this.availableGoods, this.totalTV, this.order.orderProductList, searchText.toLowerCase());

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

}
