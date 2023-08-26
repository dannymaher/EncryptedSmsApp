package com.example.encryptedsmsapp.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.encryptedsmsapp.Models.Account;
import com.example.encryptedsmsapp.Models.Contact;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AccountManager.db";

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_NUMBER = "user_number";
    private static final String COLUMN_USER_NUMBER_CONFIRMED = "user_number_confirmed";

    private static final String TABLE_CONTACT = "contact";
    private static final String COLUMN_CONTACT_KEY ="contact_key";
    private static final String COLUMN_CONTACT_NAME ="contact_name";
    private static final String COLUMN_CONTACT_NUMBER ="contact_number";
    private static final String COLUMN_CONTACT_ID = "contact_id";
    private static final String COLUMN_CONTACT_FAV = "contact_fav";
    private static final String COLUMN_CONTACT_USERID = "user_contact_userid";



    private String CREATE_USER_TABLE= "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT,"  + COLUMN_USER_NUMBER + " TEXT," + COLUMN_USER_NUMBER_CONFIRMED + " INTEGER" + ")";
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " +TABLE_USER;

    private String CREATE_CONTACT_TABLE = "CREATE TABLE " + TABLE_CONTACT + "(" + COLUMN_CONTACT_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CONTACT_ID + " TEXT,"
            + COLUMN_CONTACT_NAME + " TEXT," + COLUMN_CONTACT_NUMBER + " TEXT," + COLUMN_CONTACT_FAV
            + " INTEGER," + COLUMN_CONTACT_USERID + " INTEGER,"  +" FOREIGN KEY ("+COLUMN_CONTACT_USERID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    private String DROP_CONTACT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACT;


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CONTACT_TABLE);

        

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CONTACT_TABLE);

        onCreate(db);
    }
    public void deleteAllTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_USER);
    }

    public void addUser(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, account.getName());
        values.put(COLUMN_USER_PASSWORD, account.getPassword());
        values.put(COLUMN_USER_NUMBER, account.getNumber());
        values.put(COLUMN_USER_NUMBER_CONFIRMED, 0);
        db.insert(TABLE_USER,null,values);
        db.close();
    }

    public void addContact(Account account, Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int fav = 0;
        if(contact.isFavoutite()){
            fav= 1;
        }
        values.put(COLUMN_CONTACT_ID,contact.getId());
        values.put(COLUMN_CONTACT_NAME,contact.getName());
        values.put(COLUMN_CONTACT_NUMBER,contact.getNumber());
        values.put(COLUMN_CONTACT_FAV, fav);
        values.put(COLUMN_CONTACT_USERID, account.getId());
        long key = db.insert(TABLE_CONTACT,null,values);
        db.close();

    }


    public boolean checkUser(String name){
        String[] columns= {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount > 0){
            return true;
        }
        return false;
    }
    public boolean checkUser(String name,String password){
        String[] columns= {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" +" AND "+COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = {name,password};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount > 0){
            return true;
        }
        return false;
    }

    public Account getUser(String name){
        boolean confrimed = false;
        String[] columns= {
                COLUMN_USER_ID, COLUMN_USER_NUMBER, COLUMN_USER_NUMBER_CONFIRMED
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
        String number = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NUMBER));
        int numberConfirmed = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_NUMBER_CONFIRMED));
        if(numberConfirmed == 1){
            confrimed = true;
        }
        System.out.println(confrimed);
        String id_str = String.valueOf(id) ;
        Account account = new Account(name,id_str,number, confrimed);
        db.close();
        return account;
    }
    public boolean checkContact(String name, String accountID){
        boolean exists = false;
        String[] columns= {
                COLUMN_CONTACT_ID

        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CONTACT_NAME + " = ?" +" AND "+ COLUMN_CONTACT_USERID + " = ?";
        String[] selectionArgs = {name, accountID};
        Cursor cursor = db.query(TABLE_CONTACT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        if(cursorCount > 0){
            exists = true;


        }
        db.close();
        return exists;
    }


    public ArrayList<Contact> getUserContacts(Account account){

        String[] columns= {
                COLUMN_CONTACT_KEY, COLUMN_CONTACT_NUMBER, COLUMN_CONTACT_NAME, COLUMN_CONTACT_ID, COLUMN_CONTACT_FAV, COLUMN_CONTACT_USERID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CONTACT_USERID + " = ?" ;
        String[] selectionArgs = {account.getId()};
        Cursor cursor = db.query(TABLE_CONTACT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        if(cursorCount > 0){

            cursor.moveToFirst();
            for(int i = 0; i< cursorCount;i++){
                boolean isFav = false;
                int key = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_KEY) );
                String num = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER) );
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME) );
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID) );
                int fav = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_FAV) );
                if(fav == 1){
                    isFav = true;
                }
                Contact contact = new Contact(name,id,num,isFav,key);
                contacts.add(contact);
                cursor.moveToNext();
            }



        }
        db.close();
        return contacts;
    }
    public Contact getContact(String id,  String key){

        String[] columns= {
                COLUMN_CONTACT_KEY, COLUMN_CONTACT_NUMBER, COLUMN_CONTACT_NAME, COLUMN_CONTACT_ID, COLUMN_CONTACT_FAV, COLUMN_CONTACT_USERID

        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CONTACT_ID + " = ?"+" AND "+ COLUMN_CONTACT_KEY + " = ?" ;
        String[] selectionArgs = {id,key};
        Cursor cursor = db.query(TABLE_CONTACT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        cursor.moveToFirst();
        boolean isFav = false;
        int key2 = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_KEY) );
        String num = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER) );
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME) );
        String id2 = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID) );
        int fav = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_FAV) );
        if(fav == 1){
            isFav = true;
        }
        Contact contact = new Contact(name,id2,num,isFav,key2);
        db.close();
        return contact;
    }

    public ArrayList<Contact> getUserFavourites(Account account){
        ArrayList<Contact> favourites = new ArrayList<>();
        String[] columns= {
                COLUMN_CONTACT_KEY, COLUMN_CONTACT_NUMBER, COLUMN_CONTACT_NAME, COLUMN_CONTACT_ID, COLUMN_CONTACT_FAV, COLUMN_CONTACT_USERID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CONTACT_USERID + " = ?" +" AND "+ COLUMN_CONTACT_FAV + " = ?";
        String[] selectionArgs = {account.getId(), String.valueOf(1)};
        Cursor cursor = db.query(TABLE_CONTACT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();


        if(cursorCount > 0){

            cursor.moveToFirst();
            for(int i = 0; i< cursorCount;i++){
                boolean isFav = false;
                int key = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_KEY) );
                String num = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER) );
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_NAME) );
                String id = cursor.getString(cursor.getColumnIndex(COLUMN_CONTACT_ID) );
                int fav = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_FAV) );
                if(fav == 1){
                    isFav = true;
                }
                Contact contact = new Contact(name,id,num,isFav,key);
                favourites.add(contact);
                cursor.moveToNext();
            }



        }
        db.close();

        return favourites;
    }
    public void updateContact(Contact contact, Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        int isFav = 0;
        if(contact.isFavoutite()){
            isFav = 1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_ID, contact.getId());
        values.put(COLUMN_CONTACT_NAME,contact.getName());
        values.put(COLUMN_CONTACT_NUMBER,contact.getNumber());
        values.put(COLUMN_CONTACT_FAV, isFav);
        values.put(COLUMN_CONTACT_USERID, account.getId());
        String whereClause = COLUMN_CONTACT_KEY + " =?";
        String[] whereArgs = {String.valueOf(contact.getKey())};
        db.update(TABLE_CONTACT,values,whereClause,whereArgs);
        db.close();


    }
    public void updateAccountName( Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, account.getName());
        String whereClause = COLUMN_USER_ID + " =?";
        String[] whereArgs = {String.valueOf(account.getId()) };
        db.update(TABLE_USER,values,whereClause,whereArgs);
        db.close();


    }

    public int getContactKey(Contact contact, Account account){
        String[] columns= {
                COLUMN_CONTACT_KEY

        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_CONTACT_ID + " = ?" +" AND "+ COLUMN_CONTACT_NAME + " = ?" +" AND "+ COLUMN_CONTACT_USERID+ " = ?";
        String[] selectionArgs = {String.valueOf(contact.getId()), contact.getName(), account.getId()};
        Cursor cursor = db.query(TABLE_CONTACT,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        cursor.moveToFirst();
        int key = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACT_KEY));
        db.close();
        return key;

    }

    public void deleteContact(int contactKey ){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_CONTACT_KEY + " = ?";
        String[] whereArgs = {String.valueOf(contactKey)};
        db.delete(TABLE_CONTACT,whereClause,whereArgs);
        db.close();

    }
    public void deleteAccount(Account account ){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USER_ID + " = ?";
        String[] whereArgs = {String.valueOf(account.getId())};
        db.delete(TABLE_USER,whereClause,whereArgs);
        db.close();
        deleteUserContacts(account);

    }
    private void deleteUserContacts(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Contact> contacts = getUserContacts(account);
        for(int i =0; i< contacts.size(); i++){

            deleteContact(contacts.get(i).getKey());
        }
        db.close();
    }
    public void changePassword(Account account, String newPassword){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, newPassword);
        String whereClause = COLUMN_USER_ID + " =?";
        String[] whereArgs = {String.valueOf(account.getId()) };
        db.update(TABLE_USER,values,whereClause,whereArgs);
        db.close();
    }

    public void confirmNumber(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NUMBER, account.getNumber());
        values.put(COLUMN_USER_NUMBER_CONFIRMED, 1);
        String whereClause = COLUMN_USER_ID + " =?";
        String[] whereArgs = {String.valueOf(account.getId()) };
        db.update(TABLE_USER,values,whereClause,whereArgs);
        db.close();
    }

}
