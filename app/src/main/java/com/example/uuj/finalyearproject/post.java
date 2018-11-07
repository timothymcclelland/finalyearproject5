package com.example.uuj.finalyearproject;

public class post {

    public String post;
    public String date;
    public String time;
    public String category;

    public post(){

    }

    public post(String post, String date, String time, String category) {
        this.post = post;
        this.date = date;
        this.time = time;
        this.category = category;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}