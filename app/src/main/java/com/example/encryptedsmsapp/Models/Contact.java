package com.example.encryptedsmsapp.Models;

import java.util.ArrayList;

public class Contact {
    private String name;
    private String id;
    private String number;
    private boolean favoutite;
    private int key;

    public Contact() {
    }

    public Contact(String name, String id, String number) {
        this.name = name;
        this.id = id;
        this.number = number;
    }

    public Contact(String name, String id, String number, boolean favoutite) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.favoutite = favoutite;
    }

    public Contact(String name, String id, String number, boolean favoutite, int key) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.favoutite = favoutite;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //placeholder
    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Contact("contact "+i ,"1","1"));
        }

        return contacts;
    }

    public boolean isFavoutite() {
        return favoutite;
    }

    public void setFavoutite(boolean favoutite) {
        this.favoutite = favoutite;
    }
}
