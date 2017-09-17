package kryternext.graduatework;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import kryternext.graduatework.app.models.Product;
import kryternext.graduatework.app.models.Storage;

public class NewOrder extends AppCompatActivity {
    private ListView availableGoods;
    private Storage storage;
    private TextView totalTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        this.storage = new Storage(this, "wms");
        availableGoods = (ListView) findViewById(R.id.goods_list);
        totalTV = (TextView) findViewById(R.id.totalPrice);
        totalTV.setText("Total: 0");
        String accountType = getIntent().getStringExtra("accountType");
        this.storage.getGoodsByType(accountType, availableGoods, totalTV);
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
        double balance = getIntent().getDoubleExtra("accountBalance", 0);
        if (totalValue <= balance) {
            Toast.makeText(this, "ORDER", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Balance...", Toast.LENGTH_SHORT).show();
    }

}
