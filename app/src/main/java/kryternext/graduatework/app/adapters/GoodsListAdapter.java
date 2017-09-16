package kryternext.graduatework.app.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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

        View vi = convertView;
        Product product = products.get(position);
        if (vi == null)
            vi = inflater.inflate(R.layout.product_row, parent, false);
        TextView text = (TextView) vi.findViewById(R.id.productName);
        text.setText(product.getProductName());
        return vi;
    }
}
