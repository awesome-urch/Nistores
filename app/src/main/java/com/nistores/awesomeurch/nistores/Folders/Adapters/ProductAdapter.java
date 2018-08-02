package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.Product;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Awesome Urch on 29/07/2018.
 * My Adapter for All Products (Home) Fragment
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Context context;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, store;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            store = view.findViewById(R.id.store);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }


    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_row, parent, false);

        return new ProductAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.name.setText(product.getTitle());
        holder.price.setText(product.getPrice());
        holder.store.setText(product.getStore_uid());
        final String STRING_BASE_URL = "https://www.nistores.com.ng/";
        String pic = product.getImage();
        //String img = "https://www.nistores.com.ng/"+product.getImage();

        Picasso.with(context).load(STRING_BASE_URL + pic).placeholder(R.mipmap.ic_launcher).into(holder.thumbnail);

            /*Glide.with(context)
                    .load(str.toString())
                    .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
