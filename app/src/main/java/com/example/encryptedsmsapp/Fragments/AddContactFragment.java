package com.example.encryptedsmsapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddContactFragment extends Fragment {
    private Contact contact;
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    public AddContactFragment() {

    }

    public static AddContactFragment newInstance(){
        AddContactFragment fragment = new AddContactFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String username = ((MessageActivity) this.getActivity()).getUsername();
        final View view = inflater.inflate(R.layout.add_contact_fragment,container,false);
        final TextView nameHeading = view.findViewById(R.id.contactNameHeading);
        final TextView numberHeading = view.findViewById(R.id.contactNoHeading);
        final TextView idHeading = view.findViewById(R.id.contactIdHeading);
        final Button submitBtn = view.findViewById(R.id.saveContactButton);
        final CheckBox favBox = view.findViewById(R.id.favCheckBox);
        final TextInputEditText nameInput = view.findViewById(R.id.contactNameInput);
        final TextInputEditText idInput = view.findViewById(R.id.contactIdInput);
        final TextInputEditText numInput = view.findViewById(R.id.contactNoInput);
        final TextInputLayout nameLayout = view.findViewById(R.id.contactNameInputLayout);
        final TextInputLayout numLayout = view.findViewById(R.id.contactNoInputLayout);
        final TextInputLayout idLayout = view.findViewById(R.id.contactIdInputLayout);
        final ConstraintLayout layout = view.findViewById(R.id.add_contact_layout);
        nameHeading.setText("Name");
        numberHeading.setText("Phone Number");
        idHeading.setText("Account ID");
        submitBtn.setText("Submit");

        final Account account = databaseHelper.getUser(username);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySQL(nameInput,numInput,idInput,favBox,nameLayout,numLayout,idLayout,account,layout,view);
                //getContext().deleteDatabase("AccountManager.db");



            }
        });
        return view;
    }
    public void initObjects(){

        databaseHelper = new DatabaseHelper(getContext());
        inputValidation = new InputValidation(getContext());
    }
    private void verifySQL(TextInputEditText nameText, TextInputEditText numberText, TextInputEditText idText, CheckBox favBox,
                           TextInputLayout nameLayout, TextInputLayout numLayout,TextInputLayout idLayout, Account account, ConstraintLayout layout, View view ){

        if(!inputValidation.isInputTextFilled(nameText,nameLayout,getString(R.string.blank_contact_name_input))){
            return;
        }
        if(!inputValidation.isInputTextFilled(numberText,numLayout, getString(R.string.blank_contact_num_input))){
            return;
        }
        if(!inputValidation.isInputTextFilled(idText,idLayout,getString(R.string.blank_contact_id_input))){
            return;
        }
        if(databaseHelper.checkContact(nameText.getText().toString().trim(), account.getId())){
            Snackbar.make(layout, getString(R.string.error_contact_name),Snackbar.LENGTH_LONG).show();
            return;
        }
        boolean fav = false;
        if(favBox.isChecked()){
            fav = true;
        }
        Contact contact = new Contact(nameText.getText().toString().trim(), idText.getText().toString().trim() ,String.valueOf(numberText.getText().toString().trim()),fav);
        databaseHelper.addContact(account,contact);
        Snackbar.make(layout, contact.getName() + " added to contacts",Snackbar.LENGTH_LONG).show();
        Navigation.findNavController(view).navigate(R.id.action_addContactFragment_to_mainMenuFragment);
    }
}
