package com.example.moviequotes;

import java.util.ArrayList;

public class User {
    ArrayList<String> likes;
    public String userName;
    public String email;
    public String password;

    public String sex;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.likes = new ArrayList<>(1000);
        this.sex = "Male";
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

    public void addLike(String id){
        likes.add(id);
    }

    public void dislike(String id){
        likes.remove(id);
    }

}
