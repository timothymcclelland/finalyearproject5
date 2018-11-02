package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_post extends AppCompatActivity {

        private EditText addPost;
        private Button buttonPost;

        private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        addPost = (EditText) findViewById(R.id.postEditText);
        buttonPost = (Button) findViewById(R.id.postButton);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post");

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String post_content = addPost.getText().toString().trim();

                DatabaseReference newPost = databaseReference.push();

                newPost.child("Post").setValue(post_content);

                startActivity(new Intent(add_post.this, content.class));
            }
        });

    }
}
