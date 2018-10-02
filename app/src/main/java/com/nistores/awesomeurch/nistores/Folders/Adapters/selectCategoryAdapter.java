package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.selectCategory;
import com.nistores.awesomeurch.nistores.R;

import java.util.Arrays;
import java.util.List;

public class selectCategoryAdapter extends RecyclerView.Adapter<selectCategoryAdapter.MyViewHolder> {
    private Context context;
    private List<selectCategory> selectCategories;
    private String preSelect;
    private static int SELECT_DESIGN = 1;
    private static int STRAIGHT_DESIGN = 2;
    private int design = 1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, link, nameNormal ;
        public CheckBox name;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            nameNormal = view.findViewById(R.id.name_normal);
            id = view.findViewById(R.id.id);


        }
    }

    public selectCategoryAdapter(Context context, List<selectCategory> selectCategories) {
        this.context = context;
        this.selectCategories = selectCategories;
        this.preSelect = "";
    }

    @Override
    public selectCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_category_item_row, parent, false);
        if(design == STRAIGHT_DESIGN){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_item_row, parent, false);
        }

        return new selectCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(selectCategoryAdapter.MyViewHolder holder, final int position) {
        final selectCategory category = selectCategories.get(position);
        holder.id.setText(category.getId());

        if(design == SELECT_DESIGN){
            holder.name.setText(Html.fromHtml(category.getName()));
            holder.name.setChecked(false);
            if(Arrays.asList(preSelectedArray()).contains(category.getId())){
                final CheckBox cb = holder.name;
                Log.d("CHECKEE","e dey for "+category.getName());
                cb.setChecked(true);
            /*cb.post(new Runnable() {
                @Override
                public void run() {
                    cb.setSelected(true);
                }
            });*/
            }
        }else if(design == STRAIGHT_DESIGN){
            holder.nameNormal.setText(Html.fromHtml(category.getName()));
        }

    }

    @Override
    public int getItemCount() {
        return selectCategories.size();
    }

    public void setPreSelect(String preSelect) {
        this.preSelect = preSelect;
    }

    private String[] preSelectedArray(){
        String delim = ",";
        return this.preSelect.split(delim);
    }

    public void setDesign(int design) {
        this.design = design;
    }
}
