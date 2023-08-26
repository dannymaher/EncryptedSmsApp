package com.example.encryptedsmsapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.Models.Message;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Contact> contacts;
    public RecyclerView layout;

    public MessageAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.message_box,parent,false);
        MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(contactView);
        layout = (RecyclerView) parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        final Contact contact = contacts.get(position);
        TextView initial = holder.initial;
        TextView name = holder.messageName;
        initial.setText(String.valueOf(contact.getName().charAt(0)));

        name.setText(contact.getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(layout.getChildAt(position )).navigate(R.id.action_mainMenuFragment_to_sendMessageFragment,bundle);
            }
        });
//        List<Message> messageList = Message.getContactSms(holder.layout.getContext(),contact);
//        if(messageList.size() > 0){
//
//        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView initial;
        public TextView messageName;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initial = (TextView) itemView.findViewById(R.id.msgInitial);
            messageName = itemView.findViewById(R.id.msgName);
            layout = itemView.findViewById(R.id.messageBoxLayout);
        }
    }
}

