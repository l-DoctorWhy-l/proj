package com.example.moviequotes.Entities;

import java.util.ArrayList;

public class User {
    ArrayList<String> likes;
    public String userName;
    public String email;

    public String sex;
    public Boolean isAdmin;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "likes=" + likes +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public User() {
    }

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.likes = new ArrayList<>(1000);
        this.sex = "Male";
        this.isAdmin = false;
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

    public void addLike(String id){
        likes.add(id);
    }

    public void dislike(String id){
        likes.remove(id);
    }

}
