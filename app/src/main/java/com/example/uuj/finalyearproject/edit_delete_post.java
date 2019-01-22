package com.example.uuj.finalyearproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class edit_delete_post extends AppCompatActivity {

    //Class member variables
    private TextView editPostContent, editPostCategory;
    private Button editButton, deleteButton;

    //Firebase Database variable
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    private String PostKey, currentUserID, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_post);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts").child(PostKey);

        editPostContent = findViewById(R.id.edit_delete_post_text);
        editPostCategory = findViewById(R.id.edit_delete_post_category);
        editButton = findViewById(R.id.editPostButton);
        deleteButton = findViewById(R.id.deletePostButton);

        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){

                if(dataSnapshot.exists())
                {
                    final String post = dataSnapshot.child("post").getValue().toString();
                    final String category = dataSnapshot.child("category").getValue().toString();
                    userID = dataSnapshot.child("user id").getValue().toString();

                    editPostContent.setText(post);
                    editPostCategory.setText(category);

                    if(currentUserID.equals(userID))
                    {
                        editButton.setVisibility(View.VISIBLE);
                        deleteButton.setVisibility(View.VISIBLE);
                    }

                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editPost(post);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.removeValue();
                //once OnClick method is completed, user will be taken back to the content activity screen
                Intent deletePostIntent = new Intent(edit_delete_post.this, content.class);
                deletePostIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(deletePostIntent);
                Toast.makeText(edit_delete_post.this, "Post deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPost(String post)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(edit_delete_post.this);
        View mView = getLayoutInflater().inflate(R.layout.edit_dialog, null);
        final EditText editPost = mView.findViewById(R.id.post_edit_text);
        editPost.setText(post);
        final Spinner editSpinner = mView.findViewById(R.id.post_edit_category);
        //spinner uses category array specified in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        //drop down style used
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editSpinner.setAdapter(adapter);
        // Get the layout inflater

        //code below used to create date attribute in referenced child(randomly created when post to database is made)
        //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        final String date = currentDate.format(calendarDate.getTime());

        //code below used to create date attribute in referenced child(randomly created when post to database is made)
        //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String time = currentTime.format(calendarTime.getTime());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child("category").setValue(editSpinner.getSelectedItem().toString());
                databaseReference.child("post").setValue(editPost.getText().toString());
                databaseReference.child("time").setValue(time);
                databaseReference.child("date").setValue(date);
                Toast.makeText(edit_delete_post.this, "Post Updated", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
