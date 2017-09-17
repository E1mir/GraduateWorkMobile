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
import kryternext.graduatework.app.models.Order;
import kryternext.graduatework.app.models.Product;

public class GoodsListAdapter extends BaseAdapter {
    private Context context;
    private TextView totalTV;
    private List<Product> products;
    private static LayoutInflater inflater = null;

    public GoodsListAdapter(Context context, List<Product> products, TextView totalTV) {
        this.context = context;
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.totalTV = totalTV;
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
    public View getView(int position, final View convertView, ViewGroup parent) {
        View productRow = convertView;
        final Product product = products.get(position);
        if (productRow == null)
            productRow = inflater.inflate(R.layout.product_row, parent, false);
        // TextView totalPriceTV = (TextView) ;
        TextView nameTV = (TextView) productRow.findViewById(R.id.productName);
        TextView priceTV = (TextView) productRow.findViewById(R.id.productPrice);
        final TextView countTV = (TextView) productRow.findViewById(R.id.productCount);
        final TextView selectedCountTV = (TextView) productRow.findViewById(R.id.productSelectedCount);
        Button minus = (Button) productRow.findViewById(R.id.minusProduct);
        Button plus = (Button) productRow.findViewById(R.id.plusProduct);
        nameTV.setText(String.format(Locale.ENGLISH, "%d: %s", (position + 1), product.getProductName()));
        priceTV.setText(String.format(Locale.ENGLISH, "%.2f$", product.getPrice()));
        countTV.setText(String.format(Locale.ENGLISH, "%d", product.getCount()));
        selectedCountTV.setText("0");
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countStr = selectedCountTV.getText().toString();
                String totalCountStr = totalTV.getText().toString().split(":")[1].trim();
                double totalCount = Double.parseDouble(totalCountStr);
                int productCount = Integer.parseInt(countTV.getText().toString());
                int selectedCount = Integer.parseInt(countStr);
                if (selectedCount > 0) {
                    countTV.setText(String.valueOf(++productCount));
                    selectedCountTV.setText(String.valueOf(--selectedCount));
                    totalCount -= product.getPrice();
                    totalTV.setText(String.format(Locale.ENGLISH, "Total: %.2f", totalCount));
                    Order.order.put(product.getProductName(), selectedCount);
                } else {
                    Order.order.remove(product.getProductName());
                    Snackbar.make(view, "Count is 0", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countStr = selectedCountTV.getText().toString();
                String totalCountStr = totalTV.getText().toString().split(":")[1].trim();
                double totalCount = Double.parseDouble(totalCountStr);
                int productCount = Integer.parseInt(countTV.getText().toString());
                int selectedCount = Integer.parseInt(countStr);
                if (productCount > 0) {
                    countTV.setText(String.valueOf(--productCount));
                    selectedCountTV.setText(String.valueOf(++selectedCount));
                    totalCount += product.getPrice();
                    totalTV.setText(String.format(Locale.ENGLISH, "Total: %.2f", totalCount));
                    Order.order.put(product.getProductName(), selectedCount);
                } else {
                    Snackbar.make(view, "Maximal product count reached!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        return productRow;
    }
}
