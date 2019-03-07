//used same principles as reset_password_screen.java and comment_screen.java classes in the creation of this java class

package com.example.uuj.finalyearproject;

//android and java imports
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

//firebase imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//java imports
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class report_screen extends AppCompatActivity {

        //Class member variables
        private EditText report_information;
        private Spinner spinner;
        private Button buttonReport;
        private String date;
        private String time;
        private String currentUserID;
        private String PostKey;

        //Firebase Authentication variable
        private FirebaseAuth mAuth;

        //Firebase Database variable
        private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_screen);

        //methods below used to get current user_model ID from the Firebase Authentication system
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //used to get position of specific post_model that a user_model has selected to report on
        PostKey = getIntent().getExtras().get("PostKey").toString();

        //Spinner used to select reason of report
        spinner = (Spinner) findViewById(R.id.report_reason_spinner);
        //spinner uses report reason array specified in strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.report_reason_array, android.R.layout.simple_spinner_dropdown_item);
        //drop down style used
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Referencing Java to XML variables in report_screen    report_information = (EditText) findViewById(R.id.editTextReportInformation);
        buttonReport = (Button) findViewById(R.id.report_button);

        /*Referencing database variable to Firebase Realtime Database child "user_model Post Reports" which will contain all user_model's reports for each comment*/
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_model Post Reports").child(PostKey).child("Post Reports");

        //onClickListener method called to send data to the Firebase Realtime database
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //code below used to create date attribute in referenced child(randomly created when post_model to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                date = currentDate.format(calendarDate.getTime());

                //code below used to create date attribute in referenced child(randomly created when post_model to database is made)
                //code below taken from https://www.youtube.com/watch?v=LBiii5baeas&list=PLxefhmF0pcPnTQ2oyMffo6QbWtztXu1W_&index=21
                Calendar calendarTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                time = currentTime.format(calendarTime.getTime());

                //formatting EditText variable to String to input in Firebase Database
                String report_information_content = report_information.getText().toString();

                //formatting spinner item to get selected item and format it to string for input in Firebase Database
                String reportReasonSelected = spinner.getSelectedItem().toString();

                 /*Toast message that displays if report_information_content editText field is
                 left empty when user_model tries to submit a report*/
                if(TextUtils.isEmpty(report_information_content))
                {
                    Toast.makeText(report_screen.this, "Enter report information", Toast.LENGTH_SHORT).show();
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
                    newReport.child("user_model id").setValue(currentUserID);

                    //once OnClick method is completed, user_model will be taken back to the content_screen activity screen
                    startActivity(new Intent(report_screen.this, content_screen.class));
                }
            }
        });
    }
}
