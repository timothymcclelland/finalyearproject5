package com.example.uuj.finalyearproject;

//model class used to retrieve the data from the database and set it to the referenced variables for use in saveToken method in content.java
public class User {

    //class variables referenced in Firebase Database
    public String email;
    public String token;

    //User constructor class
    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
