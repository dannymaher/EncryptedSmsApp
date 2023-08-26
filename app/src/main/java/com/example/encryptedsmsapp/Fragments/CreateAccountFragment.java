package com.example.encryptedsmsapp.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.MainActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Account account;
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initObjects();
    }
    public void initObjects(){
        //databaseHelper = new DatabaseHelper(getActivity());
        //inputValidation = new InputValidation(getActivity());
        databaseHelper = new DatabaseHelper(getContext());
        inputValidation = new InputValidation(getContext());
        account = new Account();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_create_account, container,false);
        final TextInputEditText userNameInput = view.findViewById(R.id.userNameInputBox);
        final TextInputEditText passwordInput = view.findViewById(R.id.passwordInputBox);
        final TextInputEditText reenterInput = view.findViewById(R.id.reenterpasswordInputBox);
        final TextInputLayout userNameLayout = view.findViewById(R.id.userNameLayout);
        final TextInputLayout passwordLayout = view.findViewById(R.id.passwordLayout);
        final TextInputLayout reenterLayout = view.findViewById(R.id.reenterPassowrdLayout);
        final ConstraintLayout scroll = view.findViewById(R.id.frameLayout);
        TextView userName= view.findViewById(R.id.userName);
        TextView password = view.findViewById(R.id.password);
        TextView reenter = view.findViewById(R.id.reenterPassword);
        Button button = view.findViewById(R.id.submitButton);
        button.setText("Submit");
        reenter.setText("Re-enter Password");
        userName.setText("UserName");
        password.setText("Password");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PostSQLData(userNameInput,userNameLayout,passwordInput,passwordLayout,reenterInput,reenterLayout,scroll);
            }
        });
        return view;
        //return inflater.inflate(R.layout.fragment_create_account, container, false);
    }
    private void PostSQLData(TextInputEditText username , TextInputLayout usernameLayout, TextInputEditText password, TextInputLayout passwordLayout,
                             TextInputEditText reenter, TextInputLayout reenterLayout, ConstraintLayout scroll){
        if(!inputValidation.isInputTextFilled(username,usernameLayout,getString(R.string.error_message_name))){
            return;
        }
        if(!inputValidation.isInputTextFilled(password,passwordLayout,getString(R.string.error_message_password))){
            return;
        }
        if(!inputValidation.isInputTextFilled(reenter,reenterLayout,getString(R.string.error_message_reenter))){
            return;
        }
        if(!inputValidation.doesInputTextMatch(password,reenter,reenterLayout,getString(R.string.error_password_invalid))){
            return;
        }
        if(!databaseHelper.checkUser(username.getText().toString().trim())){
            account.setName(username.getText().toString().trim());
            account.setPassword(password.getText().toString().trim());
            account.setNumber(((MainActivity) this.getActivity()).getNumber()) ;
            account.setNumberConfrimed(false);
            databaseHelper.addUser(account);
            Snackbar.make(scroll,getString(R.string.success_message),Snackbar.LENGTH_LONG).show();
            emptyInput(username,password,reenter);
        }
        else{
            Snackbar.make(scroll, getString(R.string.error_account_exists),Snackbar.LENGTH_LONG).show();
        }
    }
    private void emptyInput(TextInputEditText username, TextInputEditText password, TextInputEditText reenter){
        username.setText(null);
        password.setText(null);
        reenter.setText(null);
    }
}
