package com.example.uuj.finalyearproject;

//android and google imports
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

//firebase imports
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    //Class member variables
    private TextView loginText;
    private EditText email;
    private EditText password;
    private Button register;

    //Firebase Authentication variable
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Referencing Java to XML resources
        loginText = findViewById(R.id.textViewSignin);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.buttonRegister);

        //reference to Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        //register button runs RegisterUser method
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == register){
                    RegisterUser();
                }
            }
        });

        //loginText textview links to log_in activity
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == loginText)
                startActivity(new Intent(getApplicationContext(),
                        log_in.class));
            }
        });
    }

    //code taken from https://medium.com/@peterekeneeze/add-firebase-authentication-to-your-app-in-7minutes-c13df58994bd
    //RegisterUser method creates a user account in Firebase authentication with the email and password they enter
    public void RegisterUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        //Checks if email EditText is empty and displays message
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
            return;
        } else  if
        //Checks if password EditText is empty and displays message
        (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //method to create User with email and password in Firebase Authentication system
            mAuth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                //check if successful
                                if (task.isSuccessful()) {
                                    //User is successfully registered and logged in
                                    //start Profile Activity here
                                    Toast.makeText(Register.this, "Registration successful",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), content.class));
                                } else {
                                    //User unsuccessful in registering, message displayed
                                    Toast.makeText(Register.this, "Unable to register, please try again",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}