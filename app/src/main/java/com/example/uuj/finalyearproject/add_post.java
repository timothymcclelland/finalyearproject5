package com.example.uuj.finalyearproject;

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
        private Button postButton;

        private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        addPost.findViewById(R.id.postEditText);
        postButton.findViewById(R.id.postButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postActivity();
            }
        });

    }

    private void postActivity() {
        String post_content = addPost.getText().toString().trim();

        if(TextUtils.isEmpty(post_content)){
            
        }
    }
}
