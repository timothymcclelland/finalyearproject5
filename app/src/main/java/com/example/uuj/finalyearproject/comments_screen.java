/*followed the following tutorials in the creation of this java class
https://www.youtube.com/watch?v=Jnsrcbe9MCQ&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=41
https://www.youtube.com/watch?v=oE-BObhBn2k&index=42&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_
https://www.youtube.com/watch?v=hX5867tnXFk&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=43
https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
https://www.youtube.com/watch?v=tOn5HsQPhUY

 */

package com.example.uuj.finalyearproject;

//android and java imports
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

//firebase imports
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//java imports
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class comments_screen extends AppCompatActivity {

    //Class member variables
    private RecyclerView CommentsRecyclerView;
    private ImageButton postCommentButton;
    private EditText postCommentText;
    private String PostKey, current_UserID;

    //Firebase Database variables
    private DatabaseReference commentsRef, databaseReference;

    //Firebase Authentication variable
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_screen);

        //used to get position of specific post that a user has selected to comment on
        PostKey = getIntent().getExtras().get("PostKey").toString();

        //methods below used to get current user ID from the Firebase Authentication system
        mAuth = FirebaseAuth.getInstance();
        current_UserID = mAuth.getCurrentUser().getUid();

        /*Referencing database variables to Firebase Realtime Database children "user Post Reports" which
         will contain all users' posts and their comments for each post*/
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Users Posts").child(PostKey).child("Post Comments");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts");

        /*assigning java RecyclerView instance to xml item and setting to fixed size so
        that width or height does not change based on the content_screen in it and setting the stack of the contents to
        start from the end
        Also sort the comments in the comments_screen screen in ascending order by reversing the layout
         */
        CommentsRecyclerView = findViewById(R.id.commentRecyclerView);
        CommentsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsRecyclerView.setLayoutManager(linearLayoutManager);

        //Referencing Java to XML variables in comments_screen.xml
        postCommentText = findViewById(R.id.comment_text);
        postCommentButton = findViewById(R.id.post_comment_button);

        //onClickListener method called to send data to the Firebase Realtime database
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                databaseReference.child(current_UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //comment is validated to ensure that text has been entered
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

    /* onStart method calls recyclerview adapter to retrieve data from Firebase database and input into the layout defined
    within comments_layout.xml*/
    protected void onStart()
    {
        super.onStart();
        //RecyclerOptions set the options that the RecyclerAdapter will use to retrieve the data from the database
        FirebaseRecyclerOptions<comments_model> options = new FirebaseRecyclerOptions.Builder<comments_model>()
                .setQuery(commentsRef, comments_model.class)
                .build();

          /*RecyclerAdapter uses the comments_model class and the getter and setter methods defined within to set the viewHolder data to the
        data retrieved from the database*/
        /* RecyclerAdapter is used to bind the data retrieved from the database for use by the CommentsViewHolder class to display it in the defined view*/
        FirebaseRecyclerAdapter<comments_model, CommentsViewHolder> adapter = new FirebaseRecyclerAdapter<comments_model, CommentsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull comments_model model)
            {
                holder.comment_Text.setText(model.getComment());
                holder.date_Text.setText(model.getDate());
                holder.time_Text.setText(model.getTime());
            }

            @NonNull
            @Override
            //method used to take data from database and display it in the comments_layout.xml layout
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_layout, viewGroup, false);
                CommentsViewHolder viewHolder = new CommentsViewHolder(view);
                return viewHolder;
            }
        };
        //sets the recyclerview to the recycleradapter defined above
        CommentsRecyclerView.setAdapter(adapter);
        //initiates the recycleradapter to pull the data from the database
        adapter.startListening();
    }

    //ViewHolder used to reference each comments_layout xml resource and allow repetition of these resources as required by the RecyclerAdapter
    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView comment_Text;
        TextView date_Text, time_Text;

        //referencing variables to xml
        public CommentsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mView = itemView;

            comment_Text = mView.findViewById(R.id.user_comment_text);
            time_Text = mView.findViewById(R.id.comment_date);
            date_Text = mView.findViewById(R.id.comment_time);
        }
    }

    /* method to validate the comment and then send the data entered to the Firebase Database*/
    private void validateComment(){
        String comment = postCommentText.getText().toString();

         /*Toast message that displays if comment editText field is
         left empty when user tries to add a comment*/
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

            //used https://www.youtube.com/watch?v=tOn5HsQPhUY as basis of how I should send my data to my Firebase database
            //Creates reference to auto-generated child location in Firebase database
            DatabaseReference newComment = commentsRef.push();

            //Creating children in referenced Firebase database child and set the value that will appear in the database
            newComment.child("comment").setValue(comment);
            newComment.child("date").setValue(date);
            newComment.child("time").setValue(time);
            newComment.child("user_ID").setValue(current_UserID);
        }
    }
}