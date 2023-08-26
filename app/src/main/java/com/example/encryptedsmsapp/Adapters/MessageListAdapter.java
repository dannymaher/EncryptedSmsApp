package com.example.encryptedsmsapp.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.Models.Message;
import com.example.encryptedsmsapp.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int MESSAGE_SENT = 2;
    private static final int MESSAGE_RECEIVED = 1;
    private Context context;
    private List<Message> messageList;
    private Account account;
    private Contact contact;
    public RecyclerView layout;

    public MessageListAdapter(Context context, List<Message> messageList, Account account, Contact contact) {
        this.context = context;
        this.messageList = messageList;
        this.account =account;
        this.contact = contact;

    }

    @Override
    public int getItemViewType(int position) {

        Message message = (Message) messageList.get(position);

        if(message.getType() == MESSAGE_SENT){
            return MESSAGE_SENT;
        }
        else{
            return MESSAGE_RECEIVED;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(messageList.isEmpty()){
            Snackbar.make(parent,"could not find messages, make sure contact number includes dialing codes e.g +44",BaseTransientBottomBar.LENGTH_LONG);
        }

        if(viewType == MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_msg_box,parent,false);
            return new recievedMessageHolder(view);
        }
        else if (viewType == MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_box,parent,false);
            return new sentMessageHolder(view);
        }
        return null;
    }
    public void updateData(Message newMessage){
        messageList.add(newMessage);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message message = (Message) messageList.get(position);
        switch (holder.getItemViewType()){
            case MESSAGE_RECEIVED:
                ((recievedMessageHolder) holder).bind(message);

                break;
            case MESSAGE_SENT:
                ((sentMessageHolder) holder).bind(message);
        }



    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
    private class recievedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        TextView nameText;
        TextView dateText;


        public recievedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.contact_message_text);
            timeText = itemView.findViewById(R.id.contact_message_time);
            nameText = itemView.findViewById(R.id.contact_message_name);
            dateText = itemView.findViewById(R.id.contact_message_date);
        }
        void bind(Message message){
            messageText.setText(message.getMsg());
            timeText.setText(DateUtils.formatDateTime(context,message.getDate(),1));
            nameText.setText(contact.getName());
            dateText.setText(DateUtils.formatDateTime(context,message.getDate(),DateUtils.FORMAT_SHOW_DATE));
        }
    }

    private class sentMessageHolder extends  RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;
        TextView dateText;
        public sentMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.user_message_body);
            timeText = itemView.findViewById(R.id.user_message_time);
            dateText = itemView.findViewById(R.id.user_message_date);
        }
        void bind(Message message){
            messageText.setText(message.getMsg());
            timeText.setText(DateUtils.formatDateTime(context,message.getDate(),1));
            dateText.setText(DateUtils.formatDateTime(context,message.getDate(),DateUtils.FORMAT_SHOW_DATE));

        }
    }

}
