package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in extends AppCompatActivity{

    anonymous anonymousLogin = new anonymous();

    private TextView registerText;
    private TextView forgotPasswordText;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        System.out.print(anonymousLogin);

        registerText = findViewById(R.id.textViewSignup);
        forgotPasswordText = findViewById(R.id.forgotpassword);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(log_in.this, Register.class);
                startActivity(myIntent);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(log_in.this, forgotpassword.class);
                startActivity(myIntent);
            }
        });
    }
}