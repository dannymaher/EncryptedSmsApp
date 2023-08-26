package com.example.encryptedsmsapp.Fragments;

import android.app.Notification;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.encryptedsmsapp.Adapters.MessageAdapter;
import com.example.encryptedsmsapp.MessageActivity;
import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;
import com.example.encryptedsmsapp.Models.Message;
import com.example.encryptedsmsapp.R;
import com.example.encryptedsmsapp.SQL.DatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Contact> contacts;
    private DatabaseHelper databaseHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public messageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messageFragment newInstance(String param1, String param2) {
        messageFragment fragment = new messageFragment();
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
        databaseHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message2, container, false);
        RecyclerView rvMessages = (RecyclerView) view.findViewById(R.id.messageList);


        Account account = databaseHelper.getUser(((MessageActivity) this.getActivity()).getUsername()) ;
        contacts = databaseHelper.getUserContacts(account);
        //messages = Message.getContactSms(contact,account);
        MessageAdapter adapter = new MessageAdapter(contacts);
        rvMessages.setAdapter(adapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

}
