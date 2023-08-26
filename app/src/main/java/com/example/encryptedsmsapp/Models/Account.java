package com.example.encryptedsmsapp.Models;

import java.util.ArrayList;

public class Account {
    private String name;
    private String id;
    private String number;
    private String password;
    private boolean numberConfrimed;
    private Contact[] contacts;
    private Contact[] favourites;

    public Account(String name, String id, String number, String password,Contact[] contacts, Contact[] favourites){
        this.name = name;
        this.id=id;
        this.number = number;
        this.password = password;
        this.contacts = contacts;
        this.favourites = favourites;
    }

    public Account(String name, String id, String number, boolean numberConfrimed) {
        this.name = name;
        this.id = id;
        this.number = number;
        this.numberConfrimed = numberConfrimed;
    }

    public Account(String name, String id) {
        this.name = name;
        this.id = id;

    }

    public Account() {
    }

    public String getName() {
        return name;
    }

    public Contact[] getFavourites() { return favourites; }

    public void setFavourites(Contact[] favourites) {
        this.favourites = favourites;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public void setContacts(Contact[] contacts) {
        this.contacts = contacts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isNumberConfrimed() {
        return numberConfrimed;
    }

    public void setNumberConfrimed(boolean numberConfrimed) {
        this.numberConfrimed = numberConfrimed;
    }
}
