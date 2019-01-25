package com.example.uuj.finalyearproject;

//followed tutorial on youtube in producing this class, https://www.youtube.com/watch?v=hX5867tnXFk&index=43&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_

//model class used to retrieve the data from the database and set it to the referenced variables for use by the RecyclerAdapter method
public class comments {

    //class variables referenced in Firebase Database
    public String comment;
    public String date;
    public String time;

    //default constructor
    public comments(){

    }

    //post constructor class
    public comments(String comment, String date, String time) {
        this.comment = comment;
        this.date = date;
        this.time = time;
    }

    //getter method for comment variable
    public String getComment() {
        return comment;
    }

    //setter method for comment variable
    public void setComment(String comment) {
        this.comment = comment;
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
}
