package com.example.uuj.finalyearproject;

//android and google imports
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//firebase imports
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in extends AppCompatActivity {

    //Class member variables
    private TextView registerText;
    private TextView forgotPasswordText;
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Button anonymousButton;

    //Firebase variables
    private FirebaseAuth mAuth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        //Firebase authentication instance called to enable login and anonymous sign in methods to connect with authentication system
        mAuth = FirebaseAuth.getInstance();

        //Referencing Java to XML resources
        loginButton = findViewById(R.id.buttonLogin);
        anonymousButton = findViewById(R.id.buttonAnonymous);
        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);
        registerText = findViewById(R.id.textViewSignup);
        forgotPasswordText = findViewById(R.id.forgotPassword);

        //registerText textview links to Register activity
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(log_in.this, Register.class);
                startActivity(myIntent);
            }
        });

        //loginButton runs LoginUser method
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == loginButton){
                    LoginUser();
                }
            }
        });

        //anonymousButton runs signInAnonymously method
        anonymousButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(v == anonymousButton){
                     signInAnonymously();
                 }
             }
        });

        //forgotPasswordText textview links to reset activity
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent2 = new Intent(log_in.this, reset.class);
                startActivity(myIntent2);
            }
        });
    }

    //code taken from https://medium.com/@peterekeneeze/add-firebase-authentication-to-your-app-in-7minutes-c13df58994bd
    //method used to log in user with email and password
    public void LoginUser(){
        String Email = emailText.getText().toString().trim();
        String Password = passwordText.getText().toString().trim();

        //Checks if email EditText is empty and displays message
        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
        }
        //Checks if password EditText is empty and displays message
        else if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show();
        }
        else {
            //method to allow user to sign in to application with email and password that is stored in Firebase Authentication system
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                //Checks if sign in was successful and gets the users ID, then sends the user to content screen
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(),
                                            content.class));
                                } else {
                                    //Displays error message to user if their credentials are incorrect or inadequate
                                    Toast.makeText(log_in.this, "Unable to login. Please try again or reset password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                    e.printStackTrace();
                            }
                        }
                    });
        }
    }

    //code taken from https://firebase.google.com/docs/auth/android/anonymous-auth?utm_source=studio
    //method used to sign in user with a randomly generated user ID
    private void signInAnonymously() {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),
                                    content.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(log_in.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END signin_anonymously]
    }
}