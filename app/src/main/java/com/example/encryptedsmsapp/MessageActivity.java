package com.example.encryptedsmsapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.encryptedsmsapp.Helper.Encryption;
import com.example.encryptedsmsapp.Helper.InputValidation;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encryptedsmsapp.ui.main.SectionsPagerAdapter;

import org.w3c.dom.Text;

public class MessageActivity extends AppCompatActivity {
    private final AppCompatActivity activity = MessageActivity.this;
    private DatabaseHelper databaseHelper;
    private InputValidation inputValidation;
    private String username;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initObjects();
        Intent intent = getIntent();
        username = intent.getStringExtra("name");

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);

        }
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS},
                    1);
            return;
        }
        number = tMgr.getLine1Number();
        setContentView(R.layout.activity_message_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Account account = databaseHelper.getUser(username);
        System.out.println(account.isNumberConfrimed());
        if(account.isNumberConfrimed() == false){
           confrimNumber(account);
        }
        setSupportActionBar(toolbar);

    }
    public void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

    public String getNumber() {
        return number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


        }
    }
    public void changeNumber(final Account account1){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your phone number");


        final EditText input = new EditText(this);
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
                    account1.setNumber(input.getText().toString().trim());
                    databaseHelper.confirmNumber(account1);
                    Toast.makeText(activity, "Number updated", Toast.LENGTH_LONG).show();
                    alert.dismiss();
                }
            }
        });
    }
    public void confrimNumber(final Account account1){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm number");
        builder.setMessage("Is this your phone number?");

        final TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        text.setLayoutParams(lp);
        text.setText(number);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                changeNumber(account1);
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setView(text);
        alert.show();
    }
}