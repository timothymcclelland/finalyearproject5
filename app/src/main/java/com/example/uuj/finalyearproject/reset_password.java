package com.example.uuj.finalyearproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset_password extends AppCompatActivity {

    private static final String TAG = "EmailPasswordAuth";

    Button resetPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == resetPassword) {
                    sendResetPasswordEmail();
                }
            }
        });
    }

    private void sendResetPasswordEmail() {
        final String email = ((EditText) findViewById(R.id.editTextEmailReset))
                .getText().toString();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(reset_password.this,
                                    "Reset password code has been emailed to "
                                            + email,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Error in sending reset password code",
                                    task.getException());
                            Toast.makeText(reset_password.this,
                                    "There is a problem with reset password, try later.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}