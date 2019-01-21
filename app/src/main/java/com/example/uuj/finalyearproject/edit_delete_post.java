package com.example.uuj.finalyearproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_delete_post extends AppCompatActivity {

    //Class member variables
    private EditText editPost;
    private Spinner editSpinner;
    private Button editButton, deleteButton;

    //Firebase Database variable
    private DatabaseReference databaseReference;

    private String PostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_post);

        PostKey = getIntent().getExtras().get("PostKey").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts").child("PostKey");

        editPost = findViewById(R.id.edit_post_text);
        editSpinner = findViewById(R.id.edit_category_spinner);
        deleteButton = findViewById(R.id.deletePostButton);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post_text = dataSnapshot.child()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
    }
}
