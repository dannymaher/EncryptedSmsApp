package com.example.encryptedsmsapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;

public class ViewContactFragment extends Fragment {
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    public ViewContactFragment() {
    }

    public static ViewContactFragment newInstance(){
        ViewContactFragment fragment = new ViewContactFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initObjects();
    }
    public void initObjects(){

        databaseHelper = new DatabaseHelper(getContext());
        inputValidation = new InputValidation(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_view_contact,container,false);
        final TextView nameHeading = view.findViewById(R.id.viewContactNameHeading);
        final TextView numHeading = view.findViewById(R.id.viewContactNoHeading);
        final TextView idHeading = view.findViewById(R.id.viewContactIdHeading);
        TextView contactName = view.findViewById(R.id.viewContactName);
        TextView contactNum = view.findViewById(R.id.viewContactNum);
        TextView contactIdText = view.findViewById(R.id.viewContactId);
        Button editButton = view.findViewById(R.id.viewContactEditBtn);
        Button deleteButton = view.findViewById(R.id.viewContactDeleteButton);
        CheckBox isFavBox = view.findViewById(R.id.viewContactFav);
        Button button = view.findViewById(R.id.changePasswordButton);
        Button button1 = view.findViewById(R.id.logOutButton);
        Button button2 = view.findViewById(R.id.editNumButton);
        button2.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);
        nameHeading.setText("Name");
        numHeading.setText("Phone Number");
        idHeading.setText("Account ID");
        editButton.setText("Edit");
        deleteButton.setText("Delete");
        String username = ((MessageActivity) this.getActivity()).getUsername();
        String contactID = getArguments().getString("ID");
        final boolean isFav = getArguments().getBoolean("isFav");
        String key2= String.valueOf(getArguments().getInt("key"));
        final int isFavInt;
        if(isFav == true){
            isFavInt = 1;
        }
        else {
            isFavInt = 0;
        }

        final Account account = databaseHelper.getUser(username);
        final Contact contact = databaseHelper.getContact(contactID,key2);

        final int key = databaseHelper.getContactKey(contact,account);

        contactName.setText(contact.getName());
        contactIdText.setText(contact.getId());
        contactNum.setText(contact.getNumber());
        isFavBox.setChecked(contact.isFavoutite());
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID",contact.getId());
                bundle.putBoolean("isFav",contact.isFavoutite());
                bundle.putInt("key", contact.getKey());
                Navigation.findNavController(view).navigate(R.id.action_viewContactFragment_to_editContactFragment, bundle);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {



                        int contactKey = databaseHelper.getContactKey(contact,account);
                        databaseHelper.deleteContact(contactKey);
                        Navigation.findNavController(view).navigate(R.id.action_viewContactFragment_to_mainMenuFragment);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        return view;
    }
}
