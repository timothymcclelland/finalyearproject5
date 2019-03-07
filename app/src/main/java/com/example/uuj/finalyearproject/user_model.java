package com.example.uuj.finalyearproject;

//followed https://www.youtube.com/watch?v=6Od5PqDktGo&list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh&index=5 tutorial in the creation of this class

//model class used to retrieve the data from the database and set it to the referenced variables for use in saveToken method in content_screen.java
public class user_model {

    //class variables referenced in Firebase Database
    public String email;
    public String token;

    //user_model constructor class
    public user_model(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
