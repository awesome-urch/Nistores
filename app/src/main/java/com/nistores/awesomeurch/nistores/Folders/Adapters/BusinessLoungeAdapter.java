package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.BusinessLounge;
import com.nistores.awesomeurch.nistores.R;

import java.util.List;

public class BusinessLoungeAdapter  extends RecyclerView.Adapter<BusinessLoungeAdapter.MyViewHolder> {
    private Context context;
    private List<BusinessLounge> bizList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, link ;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            id = view.findViewById(R.id.id);
            link = view.findViewById(R.id.link);
        }
    }

    public BusinessLoungeAdapter(Context context, List<BusinessLounge> bizList) {
        this.context = context;
        this.bizList = bizList;
    }

    @Override
    public BusinessLoungeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.businesslounge_item_row, parent, false);

        return new BusinessLoungeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BusinessLoungeAdapter.MyViewHolder holder, final int position) {
        final BusinessLounge business = bizList.get(position);
        holder.name.setText(business.getMcat_title());
        holder.id.setText(business.getMcat_id());
        holder.link.setText(business.getMcat_link());


    }

    @Override
    public int getItemCount() {
        return bizList.size();
    }
}