package com.example.encryptedsmsapp.Fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptedsmsapp.Adapters.MessageAdapter;
import com.example.encryptedsmsapp.Adapters.MessageListAdapter;
import com.example.encryptedsmsapp.Helper.Encryption;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.Models.Message;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class SendMessageFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private BroadcastReceiver recievedBroadcastReceiver;
    private BroadcastReceiver sentBroadcastReciever;
    private BroadcastReceiver deliveredBroadcastReciever;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    Account account;
    Contact contact;
    RecyclerView rvMessage;
    MessageListAdapter adapter;
    IntentFilter recievedFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    IntentFilter sentFilter = new IntentFilter("android.provider.Telephony.SMS_SENT");
    IntentFilter deliveredFilter = new IntentFilter("android.provider.Telephony.SMS_DELIVERED");
    PendingIntent sentPI;
    PendingIntent deliveredPI;
    List<Message> filtered ;
    List<Message> decrypted;
    String contactNumber= "";
    String accountNumber="";
    public SendMessageFragment() {
    }
    public static SendMessageFragment newInstance(){
        SendMessageFragment fragment = new SendMessageFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void initObjects(){

        databaseHelper = new DatabaseHelper(getContext());
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initObjects();
        sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(
                SENT), 0);

        deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,
                new Intent(DELIVERED), 0);
        recievedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<Message> newMessages = Message.getContactSms(getContext(),contact,account);
                Message newMessage = newMessages.get(newMessages.size() -1);
                String[] split = newMessage.getMsg().split(" ");
                final String accountNumber = account.getNumber().substring(account.getNumber().length() -8);
                final String contactNumber = contact.getNumber().substring(contact.getNumber().length() -8);
                if (split.length >= 5) {
                    String msgContactId = split[2].trim();
                    String msgAccountId = split[4].trim();

                    if (msgAccountId.equals(account.getId().trim())  && msgContactId.equals(contact.getId().trim()) ) {
                        String decryptedMessage = Encryption.decryptRecieved(split[0].trim(), accountNumber, contactNumber);
                        newMessage.setMsg(decryptedMessage);
                        adapter.updateData(newMessage);
                        rvMessage.smoothScrollToPosition(adapter.getItemCount() -1);
                    }
                }


            }
        };
        // sent and deliver reciever not working

        sentBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                System.out.println("Sent");
            }
        };
        deliveredBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        messageSent();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        };
        getActivity().registerReceiver(recievedBroadcastReceiver, recievedFilter);
        getActivity().registerReceiver(sentBroadcastReciever, sentFilter );
        getActivity().registerReceiver(deliveredBroadcastReciever, deliveredFilter);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_message,container,false);
        Button sendMsgBtn = view.findViewById(R.id.send_message_btn);
        final EditText messageBox = view.findViewById(R.id.message_input_box);
        String username = ((MessageActivity) this.getActivity()).getUsername();
        String contactID = getArguments().getString("ID");
        final boolean isFav = getArguments().getBoolean("isFav");
        final String key = String.valueOf(getArguments().getInt("key")) ;
        final int isFavInt;
        if(isFav == true){
            isFavInt = 1;
        }
        else {
            isFavInt = 0;
        }


        account = databaseHelper.getUser(username);
        contact = databaseHelper.getContact(contactID,key);

        if(account.getNumber().length() >=8 && contact.getNumber().length() >=8){
            accountNumber = account.getNumber().substring(account.getNumber().length() -8);
            contactNumber = contact.getNumber().substring(contact.getNumber().length() -8);
        }


        rvMessage = (RecyclerView) view.findViewById(R.id.message_recycler);
        final List<Message> messages = Message.getContactSms(getContext(),contact,account);

        decrypted = new ArrayList<>();
        filtered = new ArrayList<>(); //messages.subList(messages.size() -5, messages.size() ) ;




        for(int j = 0; j< messages.size()  ; j++ ) {
            //System.out.println(messages.get(j).getMsg());
            String[] split = messages.get(j).getMsg().split(" ");
            if (split.length >= 5) {
                String msgContactId = split[2].trim();
                String msgAccountId = split[4].trim();

                if (msgAccountId.equals(account.getId().trim())  && msgContactId.equals(contact.getId().trim()) ) {
                    filtered.add(messages.get(j));
                }
            }
        }
        for(int g = 0; g< filtered.size() ;  g++){
            Message message = filtered.get(g);
            String[] messageText = message.getMsg().split(" ");
            String decryptedMessage = "";
            if(message.getType() == 1){
                decryptedMessage = Encryption.decryptRecieved(messageText[0], accountNumber, contactNumber);
            }
            if(message.getType() == 2){
                decryptedMessage = Encryption.decryptSent(messageText[0], accountNumber, contactNumber);
            }
            message.setMsg(decryptedMessage);
            decrypted.add(message);
        }









        adapter = new MessageListAdapter(getContext(),decrypted,account,contact);

        rvMessage.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        //manager.setReverseLayout(true);
        rvMessage.setLayoutManager(manager);
        rvMessage.scrollToPosition(adapter.getItemCount() -1);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SmsManager manager = SmsManager.getDefault();
                    String message = Encryption.encrypt(messageBox.getText().toString(), accountNumber, contactNumber, contact.getId(), account.getId());
                    manager.sendTextMessage(contact.getNumber(),null,message,sentPI,deliveredPI);

                    List<Message> newMessages = Message.getContactSms(getContext(),contact,account);

                    //placeholder to add message to view
                    Message newMessage = new Message(account.getId(), contact.getId(), messageBox.getText().toString(),2, System.currentTimeMillis());
                    adapter.updateData(newMessage);
                    messageBox.setText("");
                    rvMessage.smoothScrollToPosition(adapter.getItemCount() -1);
                }catch (Exception e){
                    Snackbar.make(getView(),"Something went wrong", Snackbar.LENGTH_LONG);
                }



            }
        });


        return view;
    }
    private void messageSent(){
        List<Message> newMessages = Message.getContactSms(getContext(),contact,account);
        Message newMessage = newMessages.get(newMessages.size() -1);
        adapter.updateData(newMessage);
        rvMessage.smoothScrollToPosition(adapter.getItemCount() -1);
    }
}

