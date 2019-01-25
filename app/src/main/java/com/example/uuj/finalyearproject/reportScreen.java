package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class reportScreen extends AppCompatActivity {

        //Class member variables
        private EditText report_information;
        private Spinner spinner;
        private Button buttonReport;
        private String date;
        private String time;
        private String currentUserID;
        private String PostKey;

        private FirebaseAuth mAuth;

        //Firebase Database variable
        private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();

        //Spinner used to select category of post
        spinner = (Spinner) findViewById(R.id.report_reason_spinner);
        //spinner uses category array specified in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.report_reason_array, android.R.layout.simple_spinner_dropdown_item);
        //drop down style used
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Referencing Java to XML variables in add_post.xml
        report_information = (EditText) findViewById(R.id.editTextReportInformation);
        buttonReport = (Button) findViewById(R.id.report_button);

        /*Referencing database variable to Firebase Realtime Database child "Post" which will contain */
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User Post Reports").child(PostKey).child("Post Reports");

        //onClickListener method called to send to the Firebase Realtime database
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code below used to create date attribute in referenced child(randomly created when post to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                date = currentDate.format(calendarDate.getTime());

                //code below used to create date attribute in referenced child(randomly created when post to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                time = currentTime.format(calendarTime.getTime());

                //formatting EditText variable to String to input in Firebase Database
                String report_information_content = report_information.getText().toString();

                //formatting spinner item to get selected item and format it to string for input in Firebase Database
                String reportReasonSelected = spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(report_information_content))
                {
                    Toast.makeText(reportScreen.this, "Enter report information", Toast.LENGTH_SHORT).show();
                }
                else{
                    //used https://www.youtube.com/watch?v=tOn5HsQPhUY as basis of how I should send my data to my Firebase database
                    //Creates reference to auto-generated child location in Firebase database
                    DatabaseReference newReport = databaseReference.push();

                    //Creating children in referenced Firebase database child and set the value that will appear in the database
                    newReport.child("report information").setValue(report_information_content);
                    newReport.child("report reason").setValue(reportReasonSelected);
                    newReport.child("time").setValue(time);
                    newReport.child("date").setValue(date);
                    newReport.child("user id").setValue(currentUserID);

                    //once OnClick method is completed, user will be taken back to the content activity screen
                    startActivity(new Intent(reportScreen.this, content.class));
                }
            }
        });
    }
}
