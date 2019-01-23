package com.example.uuj.finalyearproject;

import android.os.TransactionTooLargeException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class commentScreen extends AppCompatActivity {

    private RecyclerView CommentsRecyclerView;
    private ImageButton postCommentButton;
    private EditText postCommentText;
    private String PostKey, current_UserID;

    private DatabaseReference databaseReference, commentsRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);

        PostKey = getIntent().getExtras().get("PostKey").toString();

        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts").child("user id");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Users Posts").child(PostKey).child("Post Comments");

        CommentsRecyclerView = findViewById(R.id.commentRecyclerView);
        CommentsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsRecyclerView.setLayoutManager(linearLayoutManager);

        postCommentText = findViewById(R.id.comment_text);
        postCommentButton = findViewById(R.id.post_comment_button);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateComment();
            }
        });
    }

    private void validateComment() {
        String comment = postCommentText.getText().toString();

        if(TextUtils.isEmpty(comment))
        {
            Toast.makeText(this, "Please input a comment", Toast.LENGTH_SHORT).show();
        }
        else
        {
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

            final String CommentPostKey = current_UserID;

            HashMap commentsMap = new HashMap();
            commentsMap.put("user ID", current_UserID);
            commentsMap.put("comment", comment);
            commentsMap.put("date", date);
            commentsMap.put("time", time);
            commentsRef.child(CommentPostKey).updateChildren(commentsMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(commentScreen.this, "Comment Added", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(commentScreen.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}