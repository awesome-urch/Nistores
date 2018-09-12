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

import com.nistores.awesomeurch.nistores.Folders.Helpers.ChatMembers;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatMembersAdapter extends RecyclerView.Adapter<ChatMembersAdapter.MyViewHolder> {
    private Context context;
    private List<ChatMembers> chatMembers;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, messageView, friendIdView;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.friendName);
            messageView = view.findViewById(R.id.message);
            friendIdView = view.findViewById(R.id.friend_id);
            thumbnail = view.findViewById(R.id.friendLogo);
        }
    }

    public ChatMembersAdapter(Context context, List<ChatMembers> chatMembers) {
        this.context = context;
        this.chatMembers = chatMembers;
    }

    @Override
    public ChatMembersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item_row, parent, false);

        return new ChatMembersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatMembersAdapter.MyViewHolder holder, final int position) {
        final ChatMembers members = chatMembers.get(position);
        String fullName = members.getSurname() + " " + members.getFirstname();
        holder.name.setText(fullName);
        holder.messageView.setText(Html.fromHtml(members.getLast_message()));
        holder.friendIdView.setText(members.getFto());

        final String STRING_BASE_URL = "https://www.nistores.com.ng/";
        String pic = members.getPicture();
        //String img = "https://www.nistores.com.ng/"+product.getImage();

        Picasso.with(context).load(STRING_BASE_URL + pic).placeholder(R.drawable.ic_person_default).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return chatMembers.size();
    }
}
