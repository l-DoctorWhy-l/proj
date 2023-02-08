package com.example.moviequotes;

public class Validator {
    public static String validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            return ("Field cannot be empty");
        } else if (!email.matches(emailPattern)) {
            return ("Invalid email address");
        } else {
            return null;
        }
    }
    public static String validatePassword(String pass) {
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (pass.isEmpty()) {
            return ("Field cannot be empty");
        } else if (!pass.matches(passwordVal)) {
            return ("Password is too weak");
        } else {
            return null;
        }
    }
    public static String validateConfirmPassword(String pass) {
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (pass.isEmpty()) {
            return ("Field cannot be empty");
        } else if (!pass.matches(passwordVal)) {
            return ("Second password is too weak");
        } else {
            return null;
        }
    }
    public static String validateName(String name) {
        if (name.isEmpty()) {
            return ("Field cannot be empty");
        } else if (name.length() < 4) {
            return ("The name must be at least 4 characters long");
        } else {
            return null;
        }
    }
}
