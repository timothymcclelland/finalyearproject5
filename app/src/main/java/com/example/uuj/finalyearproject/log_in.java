package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in extends AppCompatActivity {

    private static final String TAG = "AnonymousAuth";

    private TextView registerText;
    private TextView forgotPasswordText;
    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Button anonymousButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.buttonLogin);
        anonymousButton = findViewById(R.id.buttonAnonymous);
        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);
        registerText = findViewById(R.id.textViewSignup);
        forgotPasswordText = findViewById(R.id.forgot_password);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(log_in.this, Register.class);
                startActivity(myIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == loginButton){
                    LoginUser();
                }
            }
        });

         anonymousButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(v == anonymousButton){
                     signInAnonymously();
                 }
             }
         });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(log_in.this, forgot_password.class);
                startActivity(myIntent);
            }
        });
    }

    //code taken from https://medium.com/@peterekeneeze/add-firebase-authentication-to-your-app-in-7minutes-c13df58994bd
    public void LoginUser(){
        String Email = emailText.getText().toString().trim();
        String Password = passwordText.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(),
                                    content.class));
                        }else {
                            Toast.makeText(log_in.this, "Unable to login. Please try again or reset password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //code taken from https://firebase.google.com/docs/auth/android/anonymous-auth?utm_source=studio
    private void signInAnonymously() {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),
                                    content.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(log_in.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END signin_anonymously]
    }
}