package com.example.linked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContactAapter extends RecyclerView.Adapter<ContactAapter.ContactViewholder> {

    List<ContactModel> contactList = new ArrayList<ContactModel>();
    private static ClickListener clickListener;

    public ContactAapter(List<ContactModel> list){
        this.contactList = list;
    }

    @NonNull
    @Override
    public ContactViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewholder holder, int position) {

        ContactModel contact = contactList.get(position);

        Glide.with(holder.itemView).load(contact.getImageUrl()).into(holder.profileImage);
        holder.username.setText(contact.getUserName());
        if(contact.getLastMsg() != null){holder.lastMsg.setText(contact.getLastMsg().getMessage());}
        if(contact.getLastMsg() != null) {
            if (contact.getLastMsg().isSent())
                holder.timestamp.setText(contact.getLastMsg().getTimeSent().toString());
            else
                holder.timestamp.setText(contact.getLastMsg().getTimeReceived().toString());
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView profileImage;
        private TextView username;
        private TextView lastMsg;
        private TextView timestamp;

        public ContactViewholder(View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.contact_item_profile_image);
            username = itemView.findViewById(R.id.contact_item_username);
            lastMsg = itemView.findViewById(R.id.contact_item_last_msg);
            timestamp = itemView.findViewById(R.id.contact_item_timestamp);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnClickListener(ClickListener clickListener){
        ContactAapter.clickListener = clickListener;
    }

    public interface ClickListener{
        void onItemClick(int position, View v);
    }
}
