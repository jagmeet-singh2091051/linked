package com.example.linked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewholder> {
    List<MessageModel> messageList = new ArrayList<MessageModel>();

    public MessageAdapter(List<MessageModel> list){

        this.messageList = list;

    }

    @NonNull
    @Override
    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewholder holder, int position) {

        MessageModel message = messageList.get(position);

        if(message.isSent()){
            holder.msgSentLayout.setVisibility(View.VISIBLE);
            holder.msgReceivedLayout.setVisibility(View.GONE);
            holder.msgSentTV.setText(message.getMessage());
            holder.msgSentTimestamp.setText(message.getTimeSent().toString());
        }
        else {
            holder.msgSentLayout.setVisibility(View.GONE);
            holder.msgReceivedLayout.setVisibility(View.VISIBLE);
            holder.msgReceivedTV.setText(message.getMessage());
            holder.msgReceivedTimestamp.setText(message.getTimeSent().toString());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewholder extends RecyclerView.ViewHolder {
        LinearLayout msgSentLayout;
        LinearLayout msgReceivedLayout;
        TextView msgSentTV;
        TextView msgReceivedTV;
        TextView msgSentTimestamp;
        TextView msgReceivedTimestamp;

        public MessageViewholder(@NonNull View itemView) {
            super(itemView);

            msgSentLayout = itemView.findViewById(R.id.msgSentLayout);
            msgReceivedLayout = itemView.findViewById(R.id.msgReceivedLayout);
            msgSentTV = itemView.findViewById(R.id.msgSentTV);
            msgReceivedTV = itemView.findViewById(R.id.msgReceivedTV);
            msgSentTimestamp = itemView.findViewById(R.id.timestampSent);
            msgReceivedTimestamp = itemView.findViewById(R.id.timestampReceived);
        }
    }
}
