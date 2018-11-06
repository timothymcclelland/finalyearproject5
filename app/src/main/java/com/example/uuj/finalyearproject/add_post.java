package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class add_post extends AppCompatActivity {

        private EditText addPost;
        private Spinner spinner;
        private Button buttonPost;
        private String date;
        private String time;

        private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        spinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        addPost = (EditText) findViewById(R.id.postEditText);
        buttonPost = (Button) findViewById(R.id.postButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post");

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendarDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                date = currentDate.format(calendarDate.getTime());

                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                time = currentTime.format(calendarTime.getTime());

                String post_content = addPost.getText().toString().trim();
                String categorySelected = spinner.getSelectedItem().toString();

                DatabaseReference newPost = databaseReference.push();

                newPost.child("Post").setValue(post_content);
                newPost.child("Category").setValue(categorySelected);
                newPost.child("Time").setValue(time);
                newPost.child("Date").setValue(date);
                startActivity(new Intent(add_post.this, content.class));
            }
        });

    }
}
