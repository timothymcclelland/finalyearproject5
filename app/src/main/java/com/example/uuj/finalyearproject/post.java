package com.example.uuj.finalyearproject;

//model class used to retrieve the data from the database and set it to the referenced variables for use by the RecyclerAdapter method
public class post {

    //class variables referenced in Firebase Database
    public String post;
    public String date;
    public String time;
    public String category;

    //default constructor
    public post(){

    }

    //post constructor class
    public post(String post, String date, String time, String category) {
        this.post = post;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    //getter method for post variable
    public String getPost() {
        return post;
    }

    //setter method for post variable
    public void setPost(String post) {
        this.post = post;
    }

    //getter method for date variable
    public String getDate() {
        return date;
    }

    //setter method for date variable
    public void setDate(String date) {
        this.date = date;
    }

    //getter method for time variable
    public String getTime() {
        return time;
    }

    //setter method for time variable
    public void setTime(String time) {
        this.time = time;
    }

    //getter method for category variable
    public String getCategory() {
        return category;
    }

    //setter method for category variable
    public void setCategory(String category) {
        this.category = category;
    }
}