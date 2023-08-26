package com.example.encryptedsmsapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.R;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<Contact> contacts;
    public RecyclerView layout;
    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView initial;
        public  TextView name;
        public Button button;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            //initialise view elements
            super(itemView);
            initial = (TextView) itemView.findViewById(R.id.initial);
            name = (TextView) itemView.findViewById(R.id.name);
            button = itemView.findViewById(R.id.editContactButton);
            layout = itemView.findViewById(R.id.contactBoxLayout);
        }
    }
    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.contact_box,parent,false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        layout = (RecyclerView) parent;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactsAdapter.ViewHolder holder, final int position) {
        //set text on holder
        final Contact contact = contacts.get(position);
        ConstraintLayout cLayout = holder.layout;
        TextView textView = holder.initial;
        char letter =contact.getName().charAt(0) ;
        Button button = holder.button;
        textView.setText(String.valueOf(letter));
        TextView textView2 = holder.name;
        textView2.setText(contact.getName());
        button.setText("Edit");
        //set onclick to edit contact
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putString("name", contact.getName());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(layout.getChildAt(position )).navigate(R.id.action_mainMenuFragment_to_editContactFragment,bundle);
            }
        });
        //set onclick to view contact
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(layout.getChildAt(position)).navigate(R.id.action_mainMenuFragment_to_viewContactFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
