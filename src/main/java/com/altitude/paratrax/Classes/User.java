package com.altitude.paratrax.Classes;

public class User {

    public String username, password;

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password){
     this.username = username;
     this.password = password;
    }




}
