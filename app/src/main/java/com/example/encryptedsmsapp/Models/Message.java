package com.example.encryptedsmsapp.Models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String accountId;
    private String contactId;

    private String id;
    private String address;
    private String msg;



    private int type;
    private long date;


    public Message(String accountId, String contactId, String id, String address, String msg, int type, long date) {
        this.accountId = accountId;
        this.contactId = contactId;
        this.id = id;
        this.address = address;
        this.msg = msg;
        this.type = type;
        this.date = date;

    }

    public Message(String accountId, String contactId, String msg, int type, long date) {
        this.accountId = accountId;
        this.contactId = contactId;
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public Message() {
    }
    //, Contact contact, Account account
    public static List<Message> getContactSms(Context context, Contact contact, Account account){
        ArrayList<Message> messages = new ArrayList<Message>();
        Message objMsg = new Message();

        try{
            Uri message = Uri.parse("content://sms"); ///inbox
            String[] phoneNo = new String[]{contact.getNumber()};
            String[] projection = {"_id", "address", "person", "body", "date", "type"};
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(message,projection,"address = ?" ,phoneNo,"date asc");
            int totalMessages = cursor.getCount();
            objMsg = new Message();
            cursor.moveToFirst();
            for(int i =0; i< totalMessages; i++){

                int index_Address = cursor.getColumnIndex("address");

                int index_Body = cursor.getColumnIndex("body");
                int index_Date = cursor.getColumnIndex("date");
                int index_Type = cursor.getColumnIndex("type");
                int index_Id = cursor.getColumnIndex("_id");
                objMsg = new Message(account.getId(), contact.getId(), cursor.getString(index_Id), cursor.getString(index_Address) , cursor.getString(index_Body),
                        cursor.getInt(index_Type), cursor.getLong(index_Date) );
                messages.add(objMsg);
                cursor.moveToNext();

            }
            cursor.close();


        }catch (SQLiteException ex){

        }


        return messages;
    }
    public void encryptMessage(){

    }

    public void decryptMessage(){

    }

    public void getMessages(){

    }

    public void sendMessage(){

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }





}
