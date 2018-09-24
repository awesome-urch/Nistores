package com.nistores.awesomeurch.nistores.Folders.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.Member;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {
    private Context context;
    private List<Member> members;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView nameView, idView, addressView;

        public MyViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name);
            idView = view.findViewById(R.id.member_id);
            addressView = view.findViewById(R.id.address);
            thumbnail = view.findViewById(R.id.profile_picture);
        }
    }

    public MemberAdapter(Context context, List<Member> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public MemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.member_item_row, parent, false);

        return new MemberAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.MyViewHolder holder, final int position) {
        final Member member = members.get(position);
        holder.idView.setText(member.getMerchant_id());
        holder.nameView.setText(Html.fromHtml(member.getSurname() + " " + member.getFirstname()));
        holder.addressView.setText(member.getLocation());

        String pic = "https://www.nistores.com.ng/" + member.getPicture();

        Picasso.with(context).load(pic).placeholder(R.drawable.ic_person_default).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }
}