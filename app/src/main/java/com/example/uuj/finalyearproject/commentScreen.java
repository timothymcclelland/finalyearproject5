package com.example.uuj.finalyearproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.LineNumberReader;

public class commentScreen extends AppCompatActivity {

    private RecyclerView CommentsRecyclerView;
    private ImageButton postCommentButton;
    private EditText PostCommentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);

        CommentsRecyclerView = findViewById(R.id.commentRecyclerView);
        CommentsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsRecyclerView.setLayoutManager(linearLayoutManager);

        PostCommentText = findViewById(R.id.comment_text);
        postCommentButton = findViewById(R.id.post_comment_button);

    }
}
