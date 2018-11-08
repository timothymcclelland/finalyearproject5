package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class add_post extends AppCompatActivity {

        //Class member variables
        private EditText addPost;
        private Spinner spinner;
        private Button buttonPost;
        private String date;
        private String time;

        //Database variable
        private DatabaseReference databaseReference;

        private FirebaseAuth mAuth;
        private String current_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        //Spinner used to select category of post
        spinner = (Spinner) findViewById(R.id.category_spinner);
        //spinner uses category array specified in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        //drop down style used
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Referencing Java to XML variables in add_post.xml
        addPost = (EditText) findViewById(R.id.postEditText);
        buttonPost = (Button) findViewById(R.id.postButton);

        /*Referencing database variable to Firebase Realtime Database child "Post" which will contain */
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts");

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        //onClickListener method called to send to the Firebase Realtime database
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code below used to create date attribute in referenced child(randomly created when post to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                date = currentDate.format(calendarDate.getTime());

                //code below used to create date attribute in referenced child(randomly created when post to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                time = currentTime.format(calendarTime.getTime());

                //formatting EditText variable to String to input in Firebase Database
                String post_content = addPost.getText().toString();

                //formatting spinner item to get selected item and format it to string for input in Firebase Database
                String categorySelected = spinner.getSelectedItem().toString();

                //used https://www.youtube.com/watch?v=tOn5HsQPhUY as basis of how I should send my data to my Firebase database
                //Creates reference to auto-generated child location in Firebase database
                DatabaseReference newPost = databaseReference.push();

                //Creating children in referenced Firebase database child and set the value that will appear in the database
                newPost.child("User ID").setValue(current_user_id);
                newPost.child("Post").setValue(post_content);
                newPost.child("Category").setValue(categorySelected);
                newPost.child("Time").setValue(time);
                newPost.child("Date").setValue(date);

                //once OnClick method is completed, user will be taken back to the content activity screen
                startActivity(new Intent(add_post.this, content.class));
            }
        });
    }
}
