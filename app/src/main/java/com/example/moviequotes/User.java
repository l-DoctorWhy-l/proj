package com.example.moviequotes;

import java.util.ArrayList;

public class User {
    public String userName;
    public String email;
    public String password;
    public ArrayList<Long> favourites;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.favourites = null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Long> getFavourites() {
        return favourites;
    }

    public void setFavourites(ArrayList<Long> favourites) {
        this.favourites = favourites;
    }
}
