package com.msgclient;

public class User implements UserInterface {

    String username = null;

    public User(String username){
        this.username = username;
    }

    @Override
    public String getDisplayName() {
        return username;
    }
}
