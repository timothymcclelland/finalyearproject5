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

        //Shared Preferences used to store the users selected sort by preference
        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "Ascending");

        /*sort the posts in the content screen in ascending order by reversing the layout and setting the stack of the contents to
        start from the end*/
        if(mSorting.equals("Ascending")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            /*sort the posts in the content screen in descending order by not reversing the layout and not setting the stack of the contents to
        start from the end*/
        }else if(mSorting.equals("Descending")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }

        /*assigning java RecyclerView instance to xml item and setting to fixed size so
        that width or height does not change based on the content in it
         */
        viewRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        viewRecycler.setHasFixedSize(true);
        viewRecycler.setLayoutManager(mLayoutManager);

        //setting the database node in Firebase to Users Posts
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users Posts");

        //Referencing Java to XML resources
        //Reference toolbar as action bar and hiding title in toolbar
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Floating action button reference
        /*Once user has clicked the postButton and input their data in the add_post screen, this data will be displayed in the content screen by
        calling DisplayPosts method*/
        FloatingActionButton postButton = (FloatingActionButton)findViewById(R.id.float_post);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(content.this, add_post.class));
            }
        });
        DisplayPosts();
    }

    /* DisplayPosts method calls recyclerview adapter to retrieve data from Firebase database and input into the cardview defined
    within post_view.xml*/
    private void DisplayPosts() {
        //RecyclerOptions set the options that the RecyclerAdapter will use to retrieve the data from the database
        FirebaseRecyclerOptions<post> options = new FirebaseRecyclerOptions.Builder<post>()
                .setQuery(databaseReference, post.class)
                .build();

        /*RecyclerAdapter uses the post class and the getter and setter methods defined within to set the viewHolder data to the
        data retrieved from the database*/
        /* RecyclerAdapter is used to bind the data retrieved from the database for use by the PostViewHolder class to display it in the defined view*/
        FirebaseRecyclerAdapter<post, PostViewHolder> adapter = new FirebaseRecyclerAdapter<post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull post model)
            {
                holder.post_Text.setText(model.getPost());
                holder.category_Text.setText(model.getCategory());
            }

            @NonNull
            @Override
            //method used to take data from database and display it in the post_view.xml cardview holder
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_view, viewGroup, false);
                PostViewHolder viewHolder = new PostViewHolder(view);
                return viewHolder;
            }
        };
        //sets the recyclerview to the recycleradapter defined above
        viewRecycler.setAdapter(adapter);
        //initiates the recycleradapter to pull the data from the database
        adapter.startListening();
    }

    //ViewHolder used to reference each post_view xml resource and allow repetition of these resources as required by the RecyclerAdapter
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