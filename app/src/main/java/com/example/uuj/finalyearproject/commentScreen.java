package com.example.uuj.finalyearproject;

import android.os.TransactionTooLargeException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

    private DatabaseReference commentsRef;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_screen);

        PostKey = getIntent().getExtras().get("PostKey").toString();

        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Users Posts").child(PostKey).child("Post Comments");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts");
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
            public void onClick(View v)
            {
                databaseReference.child(current_UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        validateComment();

                        postCommentText.setText(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<comments> options = new FirebaseRecyclerOptions.Builder<comments>()
                .setQuery(commentsRef, comments.class)
                .build();

        FirebaseRecyclerAdapter<comments, CommentsViewHolder> adapter = new FirebaseRecyclerAdapter<comments, CommentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull comments model)
            {
                holder.comment_Text.setText(model.getComment());
                holder.date_Text.setText(model.getDate());
                holder.time_Text.setText(model.getTime());
            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_layout, viewGroup, false);
                CommentsViewHolder viewHolder = new CommentsViewHolder(view);
                return viewHolder;
            }
        };

        CommentsRecyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView comment_Text;
        TextView date_Text, time_Text;

        public CommentsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;

            comment_Text = mView.findViewById(R.id.user_comment_text);
            time_Text = mView.findViewById(R.id.comment_date);
            date_Text = mView.findViewById(R.id.comment_time);
        }
    }

    private void validateComment(){
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
            String date = currentDate.format(calendarDate.getTime());

            //code below used to create date attribute in referenced child(randomly created when post to database is made)
            //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
            Calendar calendarTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            String time = currentTime.format(calendarTime.getTime());

            DatabaseReference newComment = commentsRef.push();

            newComment.child("comment").setValue(comment);
            newComment.child("date").setValue(date);
            newComment.child("time").setValue(time);
            newComment.child("user ID").setValue(current_UserID);
        }
    }
}