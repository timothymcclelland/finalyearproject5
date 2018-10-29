package com.example.uuj.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset extends AppCompatActivity {

    //code for class taken directly from https://grokonez.com/android/firebase-authentication-send-reset-password-email-forgot-password-android
    private EditText edtEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        //Referencing Java to XML resources
        edtEmail = (EditText) findViewById(R.id.editTextEmailReset);
        btnResetPassword = (Button) findViewById(R.id.buttonReset);

        mAuth = FirebaseAuth.getInstance();

        /*When Button to reset password is clicked, it gets text from edtEmil editText field and makes
        it a string which is then used within the sendPasswordResetEmail method as the email to send the reset password link to*/
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //method to send reset password link to user
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(reset.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), log_in.class));
                                } else {
                                    Toast.makeText(reset.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}