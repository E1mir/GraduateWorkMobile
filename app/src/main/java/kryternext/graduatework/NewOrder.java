package kryternext.graduatework;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import kryternext.graduatework.app.models.Order;
import kryternext.graduatework.app.models.Product;
import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.User;

public class NewOrder extends AppCompatActivity {
    private ListView availableGoods;
    private Storage storage;
    private User user;
    private TextView totalTV;
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
        totalTV.setText(String.format(Locale.ENGLISH, "Total: %d", 0));
        this.storage.getGoodsByType(this.user.getType(), this.availableGoods, this.totalTV, this.order.orderProductList);
        availableGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product prod = (Product) availableGoods.getItemAtPosition(position);
                AlertDialog alertDialog = new AlertDialog.Builder(NewOrder.this).create();
                alertDialog.setTitle(String.format("%s - Description", prod.getProductName()));
                alertDialog.setMessage(prod.getProductDescription());
                alertDialog.show();
            }
        });
    }

    public void order(View view) {
        double totalValue = Double.parseDouble(totalTV.getText().toString().split(":")[1].trim());
        double balance = user.getBalance();
        if (totalValue == 0) {
            Toast.makeText(this, "Please choose the products!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalValue <= balance) {
            order.setUser(user);
            order.setCost(totalValue);
            order.setOrderTimestamp((System.currentTimeMillis() / 1000) + 14400);
            this.storage.order(order);
        } else
            Toast.makeText(this, String.format(Locale.ENGLISH, "Not enough money: %.2f$", (balance - totalValue)), Toast.LENGTH_SHORT).show();
    }

}
