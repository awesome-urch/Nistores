package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.selectCategory;
import com.nistores.awesomeurch.nistores.R;

import java.util.List;

public class selectCategoryAdapter extends RecyclerView.Adapter<selectCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<selectCategory> selectCategories;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, id, link ;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            id = view.findViewById(R.id.id);
        }
    }

    public selectCategoryAdapter(Context context, List<selectCategory> selectCategories) {
        this.context = context;
        this.selectCategories = selectCategories;
    }

    @Override
    public selectCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_category_item_row, parent, false);

        return new selectCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(selectCategoryAdapter.MyViewHolder holder, final int position) {
        final selectCategory category = selectCategories.get(position);
        holder.name.setText(category.getName());
        holder.id.setText(category.getId());


    }

    @Override
    public int getItemCount() {
        return selectCategories.size();
    }
}
