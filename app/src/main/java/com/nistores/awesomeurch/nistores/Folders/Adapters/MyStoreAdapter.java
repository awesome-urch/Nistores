package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.MyStore;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyStoreAdapter extends RecyclerView.Adapter<MyStoreAdapter.MyViewHolder> {
    private Context context;
    private List<MyStore> myStores;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idView, nameView, addressView, expiryView, renewView;
        ImageView thumbNail;
        AppCompatButton deleteBtn, editBtn;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name);
            idView = view.findViewById(R.id.store_id);
            addressView = view.findViewById(R.id.address);
            expiryView = view.findViewById(R.id.expiry);
            renewView = view.findViewById(R.id.renew);
            thumbNail = view.findViewById(R.id.thumbnail);
            deleteBtn = view.findViewById(R.id.btn_delete);
            editBtn = view.findViewById(R.id.btn_edit);
        }
    }

    public MyStoreAdapter(Context context, List<MyStore> myStores) {
        this.context = context;
        this.myStores = myStores;
    }

    @Override
    public MyStoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_store_item_row, parent, false);

        return new MyStoreAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyStoreAdapter.MyViewHolder holder, final int position) {
        final MyStore myStore = myStores.get(position);
        holder.idView.setText(myStore.getStore_id());
        holder.nameView.setText(myStore.getSname());
        holder.addressView.setText(myStore.getSaddress());

        String orderDate = myStore.getSdate();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(orderDate);
            String dateString = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.ENGLISH).format(date);
            holder.expiryView.setText(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final String STRING_BASE_URL = new ApiUrls().getOnline();
        String pic = myStore.getSlogo();

        Picasso.with(context).load(STRING_BASE_URL + pic).placeholder(R.drawable.ic_store_grey).into(holder.thumbNail);

    }

    @Override
    public int getItemCount() {
        return myStores.size();
    }
}