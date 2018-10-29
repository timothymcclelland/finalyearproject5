package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class content extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        mAuth = FirebaseAuth.getInstance();

        //Referencing Java to XML resources
        //Reference toolbar as action bar and hiding title in toolbar
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Floating action button reference
        FloatingActionButton postButton = (FloatingActionButton)findViewById(R.id.float_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postContent();
            }
        });
    }

    //add post to feed
    public void postContent() {

    }

    //create menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    //Toolbar menu items corresponding method calls
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                filter();
                return true;
            case R.id.action_sort:
                sort();
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Sign out method taken directly from FirebaseAuth methods
    public void signOut(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, log_in.class));
    }

    //sort content based on date in ascending or descending order
    public void sort(){

    }

    //filter content based on category
    public void filter(){

    }
}