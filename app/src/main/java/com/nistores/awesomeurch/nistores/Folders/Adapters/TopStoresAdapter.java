package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.TopStores;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopStoresAdapter extends RecyclerView.Adapter<TopStoresAdapter.MyViewHolder> {
    private Context context;
    private List<TopStores> topStoresList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView storeNameView, ownerNameView, viewsView;
        public ImageView storeLogoView, ownerLogoView;
        LinearLayout starsLayout;

        public MyViewHolder(View view) {
            super(view);
            storeNameView = view.findViewById(R.id.storeName);
            storeLogoView = view.findViewById(R.id.storeLogo);
            ownerLogoView = view.findViewById(R.id.ownerLogo);
            ownerNameView = view.findViewById(R.id.ownerName);
            viewsView = view.findViewById(R.id.views);
            starsLayout = view.findViewById(R.id.starsLayout);
        }
    }

    public TopStoresAdapter(Context context, List<TopStores> topStoresList) {
        this.context = context;
        this.topStoresList = topStoresList;
    }

    @Override
    public TopStoresAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topstores_item_row, parent, false);

        return new TopStoresAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopStoresAdapter.MyViewHolder holder, final int position) {
        final TopStores stores = topStoresList.get(position);
        String owner = stores.getSurname() + " " + stores.getFirstname();
        holder.ownerNameView.setText(owner);
        holder.storeNameView.setText(stores.getSname());
        holder.viewsView.setText(stores.getViews());

        ViewGroup viewGroup = holder.starsLayout;
        int rating = Integer.parseInt(stores.getStars());
        if(rating < 6){
            for(int i = 0; i < rating; i++){
                View child = viewGroup.getChildAt(i+1);
                if(child instanceof ImageView){
                    ((ImageView) child).setImageResource(R.drawable.ic_rating_good);
                }
            }
        }


        final String STRING_BASE_URL = "https://www.nistores.com.ng/";
        String pic = stores.getSlogo();

        Picasso.with(context).load(STRING_BASE_URL + pic).placeholder(R.drawable.ic_store_grey).into(holder.storeLogoView);

            /*Glide.with(context)
                    .load(str.toString())
                    .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return topStoresList.size();
    }
}
