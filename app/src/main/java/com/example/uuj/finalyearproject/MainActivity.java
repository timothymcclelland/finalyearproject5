package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registerUser(View view) {
       Intent i = new Intent(MainActivity.this, Register.class);
       startActivity(i);
    }

    public void userLogin(View view) {
        Intent i = new Intent(MainActivity.this, log_in.class);
        startActivity(i);
    }
}

