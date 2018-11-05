package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.Adapter;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class content extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView viewRecycler;
    private DatabaseReference mDatabase;
    private String currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        viewRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        viewRecycler.setHasFixedSize(true);
        viewRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        currentUser = mAuth.getCurrentUser().getUid();

        //Referencing Java to XML resources
        //Reference toolbar as action bar and hiding title in toolbar
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Floating action button reference
        FloatingActionButton postButton = (FloatingActionButton)findViewById(R.id.float_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(content.this, add_post.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<post> options = new FirebaseRecyclerOptions.Builder<post>().setQuery(mDatabase.child(currentUser), post.class).build();

        FirebaseRecyclerAdapter<post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull post model) {
                holder.postInfo.setText(model.getPost());
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_view, viewGroup, false);
                PostViewHolder holder = new PostViewHolder(view);
                return holder;
            }
        };

        viewRecycler.setAdapter(firebaseRecyclerAdapter);

        firebaseRecyclerAdapter.startListening();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView postInfo;

        public PostViewHolder(View itemView) {
            super(itemView);

            postInfo = itemView.findViewById(R.id.postText);
        }
    }

    //create menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    //Toolbar menu items corresponding method calls
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                filter();
                return true;
            case R.id.action_sort:
                sort();
                return true;
            case R.id.action_sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Sign out method taken directly from FirebaseAuth methods
    public void signOut(){
        mAuth.signOut();
        finish();
        startActivity(new Intent(this, log_in.class));
    }

    //sort content based on date in ascending or descending order
    public void sort(){

    }

    //filter content based on category
    public void filter(){

    }
}