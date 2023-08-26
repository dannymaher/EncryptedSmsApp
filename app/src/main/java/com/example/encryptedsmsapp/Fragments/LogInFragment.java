package com.example.encryptedsmsapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        ;
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
    }
    private void verifyFromSQL(TextInputEditText userNameInput,TextInputLayout userNameLayout,TextInputEditText passwordInput, TextInputLayout passwordLayout, NestedScrollView loginScroll){
        if(!inputValidation.isInputTextFilled(userNameInput,userNameLayout,getString(R.string.error_message_name))){
            return;
        }
        if(!inputValidation.isInputTextFilled(passwordInput,passwordLayout,getString(R.string.error_message_password))){
            return;
        }
        if(databaseHelper.checkUser(userNameInput.getText().toString().trim(),
                passwordInput.getText().toString().trim())){
            Intent accountIntent = new Intent(getActivity(), MessageActivity.class);
            accountIntent.putExtra("name",userNameInput.getText().toString().trim());
            emptyInputEditText(userNameInput,passwordInput);

            startActivity(accountIntent);
        }else{
            Snackbar.make(loginScroll, getString(R.string.error_password_invalid),Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_log_in, container,false);
        final TextInputEditText userNameInput = view.findViewById(R.id.userNameInputBox);
        final TextInputEditText passwordInput = view.findViewById(R.id.passwordInputBox);
        final TextInputLayout userNameLayout = view.findViewById(R.id.userNameInputLayout);
        final TextInputLayout passwordLayout = view.findViewById(R.id.passwordInputLayout);
        final NestedScrollView scrollView = view.findViewById(R.id.logInScoll);
        TextView userName= view.findViewById(R.id.userName);
        TextView password = view.findViewById(R.id.password);
        userName.setText("UserName");
        password.setText("Password");
        Button submitButton = view.findViewById(R.id.submitButton);
        submitButton.setText("Log In");
        Button button = view.findViewById(R.id.createAccountButton);
        button.setText("Create New Account");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_logInFragment_to_CreateAccountFragment);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQL(userNameInput,userNameLayout,passwordInput,passwordLayout,scrollView);
                //getContext().deleteDatabase("AccountManager.db"); //delete this line when finished
            }
        });

        return(view);
    }
    private void emptyInputEditText(TextInputEditText username, TextInputEditText password){
        username.setText(null);
        password.setText(null);
    }
}
