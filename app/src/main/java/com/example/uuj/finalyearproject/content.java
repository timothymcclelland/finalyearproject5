package com.example.uuj.finalyearproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class content extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager;
    private SharedPreferences mSharedPref;
    private FirebaseAuth mAuth;
    private RecyclerView viewRecycler;
    private DatabaseReference databaseReference;
    private String currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "Ascending");

        if(mSorting.equals("Ascending")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }else if(mSorting.equals("Descending")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }


        viewRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        viewRecycler.setHasFixedSize(true);
        viewRecycler.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts");

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
        DisplayPosts();
    }

    private void DisplayPosts() {
        //RecyclerOptions
        FirebaseRecyclerOptions<post> options = new FirebaseRecyclerOptions.Builder<post>()
                .setQuery(databaseReference, post.class)
                .build();

        //RecyclerAdapter object
        FirebaseRecyclerAdapter<post, PostViewHolder> adapter = new FirebaseRecyclerAdapter<post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull post model)
            {
                holder.post_Text.setText(model.getPost());
                holder.category_Text.setText(model.getCategory());
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_view, viewGroup, false);
                PostViewHolder viewHolder = new PostViewHolder(view);
                return viewHolder;
            }
        };
        viewRecycler.setAdapter(adapter);
        adapter.startListening();
    }

    //ViewHolder used to display each item in the recyclerView
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView post_Text, category_Text;

        public PostViewHolder(View itemView) {
            super(itemView);

            post_Text = itemView.findViewById(R.id.post_text);
            category_Text = itemView.findViewById(R.id.post_category);
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
        String[] sortOptions = {"Ascending", "Descending"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 0 means Ascending and 1 means descending
                        if (which==0){
                            //sort ascending
                            //Edit Shared Preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", "Ascending"); //where sort is key & ascending is value
                            editor.apply(); //apply/save value in Shared Preferences
                            recreate();
                        }
                        else if(which==1){

                        }
                    }
                });
        builder.show();
    }

    //filter content based on category
    public void filter(){

    }


}