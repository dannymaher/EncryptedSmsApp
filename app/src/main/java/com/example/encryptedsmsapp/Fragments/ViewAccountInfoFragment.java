package com.example.encryptedsmsapp.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.encryptedsmsapp.Adapters.MessageAdapter;
import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.MainActivity;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class ViewAccountInfoFragment extends Fragment {
    DatabaseHelper databaseHelper;
    InputValidation inputValidation;
    public ViewAccountInfoFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_view_contact, container, false);
        String username = ((MessageActivity) this.getActivity()).getUsername();
        final Account account = databaseHelper.getUser(username);

        final TextView nameHeading = view.findViewById(R.id.viewContactNameHeading);
        final TextView numHeading = view.findViewById(R.id.viewContactNoHeading);
        final TextView idHeading = view.findViewById(R.id.viewContactIdHeading);
        TextView contactName = view.findViewById(R.id.viewContactName);
        TextView contactNum = view.findViewById(R.id.viewContactNum);
        TextView contactIdText = view.findViewById(R.id.viewContactId);
        Button editButton = view.findViewById(R.id.viewContactEditBtn);
        Button deleteButton = view.findViewById(R.id.viewContactDeleteButton);
        Button logOutButton = view.findViewById(R.id.logOutButton);
        CheckBox isFavBox = view.findViewById(R.id.viewContactFav);
        Button changePasswordBtn = view.findViewById(R.id.changePasswordButton);
        Button editNumBtn = view.findViewById(R.id.editNumButton);
        editButton.setText("Edit Number");
        changePasswordBtn.setText("Change Password");
        nameHeading.setText("Account Name");
        idHeading.setText("Account ID");
        numHeading.setText("Phone Number");
        contactName.setText(account.getName());
        contactIdText.setText(account.getId());
        logOutButton.setText("log Out");
        editNumBtn.setText("change Number");
        if(account.getNumber().isEmpty() || account.getNumber() == null){
            Snackbar.make(view,"Could not get number", Snackbar.LENGTH_LONG).show();
        }
        else {
            contactNum.setText(account.getNumber());

        }
        deleteButton.setText("Delete account");
        editButton.setText("Edit account name");

        editNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter your phone number");


                final EditText input = new EditText(getContext());
                input.setGravity(Gravity.CENTER);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();
                alert.setView(input);
                alert.show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!input.getText().toString().isEmpty()){
                            account.setNumber(input.getText().toString().trim());
                            databaseHelper.confirmNumber(account);
                            Toast.makeText(getContext(), "Number updated", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_viewAccountInfoFragment_to_mainMenuFragment);
                            alert.dismiss();
                        }
                    }
                });

            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


                builder.setTitle("Edit Name");
                builder.setMessage("Enter a name");

                final EditText input = new EditText(getActivity());
                input.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        account.setName(input.getText().toString().trim());
                        databaseHelper.updateAccountName(account);
                        ((MessageActivity) getActivity()).setUsername(account.getName());
                        Snackbar.make(view,"Account name changed to " + account.getName(), Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_viewAccountInfoFragment_to_mainMenuFragment);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setView(input);
                alert.show();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to permanently delete this account? This action cannot be undone.");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        databaseHelper.deleteAccount(account);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


                builder.setTitle("Change password");
                builder.setMessage("Enter a password and re-enter password");

                final TextInputEditText passwordInput1 = new TextInputEditText(getActivity());
                final TextInputEditText passwordInput2 = new TextInputEditText(getActivity());
                passwordInput1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordInput1.setHint("Enter password");
                passwordInput2.setHint("Re-enter password");
                passwordInput1.setGravity(Gravity.CENTER);
                passwordInput2.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT

                );
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setLayoutParams(lp);
                final TextInputLayout textInputLayout = new TextInputLayout(getActivity());
                final TextInputLayout textInputLayout1 = new TextInputLayout(getActivity());
                textInputLayout.addView(passwordInput1);
                textInputLayout1.addView(passwordInput2);
                layout.addView(textInputLayout);
                layout.addView(textInputLayout1);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {




                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                final AlertDialog alert = builder.create();
                alert.setView(layout);
                alert.show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(inputValidation.isInputTextFilled(passwordInput1,textInputLayout,"Enter a password") &&
                                (inputValidation.isInputTextFilled(passwordInput2,textInputLayout1,"Enter a password"))
                                && inputValidation.doesInputTextMatch(passwordInput1,passwordInput2, textInputLayout, "Password must match")){

                            databaseHelper.changePassword(account, passwordInput1.getText().toString().trim());
                            Snackbar.make(view,"Password changed", Snackbar.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_viewAccountInfoFragment_to_mainMenuFragment);
                            alert.dismiss();

                        }
                    }
                });



            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to log out");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
        isFavBox.setVisibility(View.GONE);
        return view;
    }
}
