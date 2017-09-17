package kryternext.graduatework.app.adapters;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import kryternext.graduatework.R;
import kryternext.graduatework.app.models.Product;

public class GoodsListAdapter extends BaseAdapter {
    Context context;
    List<Product> products;
    private static LayoutInflater inflater = null;

    public GoodsListAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View productRow = convertView;
        Product product = products.get(position);
        if (productRow == null)
            productRow = inflater.inflate(R.layout.product_row, parent, false);
        TextView name = (TextView) productRow.findViewById(R.id.productName);
        TextView price = (TextView) productRow.findViewById(R.id.productPrice);
        TextView count = (TextView) productRow.findViewById(R.id.productCount);
        final int availableCount = product.getCount();
        final TextView selectedCount = (TextView) productRow.findViewById(R.id.productSelectedCount);
        Button minus = (Button) productRow.findViewById(R.id.minusProduct);
        Button plus = (Button) productRow.findViewById(R.id.plusProduct);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countStr = selectedCount.getText().toString();
                int count = Integer.parseInt(countStr);
                if (count > 0) {
                    selectedCount.setText(String.valueOf(--count));
                } else {
                    Snackbar.make(v, "Count is 0", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countStr = selectedCount.getText().toString();
                int count = Integer.parseInt(countStr);
                if (count < availableCount) {
                    selectedCount.setText(String.valueOf(++count));
                } else {
                    Snackbar.make(v, "Maximal product count reached!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        name.setText(String.format(Locale.ENGLISH, "%d: %s", (position + 1), product.getProductName()));
        price.setText(String.format(Locale.ENGLISH, "%.2f$", product.getPrice()));
        count.setText(String.format(Locale.ENGLISH, "%d", product.getCount()));
        selectedCount.setText("0");
        return productRow;
    }
}
