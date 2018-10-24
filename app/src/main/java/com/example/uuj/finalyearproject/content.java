package com.example.uuj.finalyearproject;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class content extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        toolbar.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        bottomNav.findViewById(R.id.bottom_navigation);
    }
}