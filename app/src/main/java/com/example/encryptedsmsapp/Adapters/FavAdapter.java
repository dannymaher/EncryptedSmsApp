package com.example.encryptedsmsapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.R;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private List<Contact> favourites;
    public RecyclerView layout;
    public FavAdapter(List<Contact> favourites) {
        this.favourites = favourites;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        //initialise view elements

        public TextView initial;
        public  TextView name;
        public Button button;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initial = (TextView) itemView.findViewById(R.id.initial);
            name = (TextView) itemView.findViewById(R.id.name);
            button = itemView.findViewById(R.id.editContactButton);
            layout = itemView.findViewById(R.id.contactBoxLayout);
        }
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate view
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.contact_box,parent,false);
        FavAdapter.ViewHolder viewHolder = new FavAdapter.ViewHolder(contactView);
        layout = (RecyclerView) parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, final int position) {
        //set text of favourite name and initial
        final Contact contact = favourites.get(position);
        TextView textView = holder.initial;
        char letter =contact.getName().charAt(0) ;
        Button button = holder.button;
        ConstraintLayout cLayout = holder.layout;
        textView.setText(String.valueOf(letter));
        TextView textView2 = holder.name;
        textView2.setText(contact.getName());
        button.setText("Edit");
        //set on click to edit fav
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(layout.getChildAt(position )).navigate(R.id.action_mainMenuFragment_to_editContactFragment,bundle);
            }
        });
        //set onclcik to view fav
        cLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(layout.getChildAt(position)).navigate(R.id.action_mainMenuFragment_to_viewContactFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }
}
