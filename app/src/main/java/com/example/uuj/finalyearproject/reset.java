package com.example.uuj.finalyearproject;

//android imports
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Firebase imports
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset extends AppCompatActivity {

    //code for class taken directly from https://grokonez.com/android/firebase-authentication-send-reset-password-email-forgot-password-android

    //Class member variables
    private EditText edtEmail;
    private Button btnResetPassword;

    //Firebase Authentication variables
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        //Referencing Java to XML resources
        edtEmail = findViewById(R.id.editTextEmailReset);
        btnResetPassword = findViewById(R.id.buttonReset);

        //reference to Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        /*When Button to reset password is clicked, it gets text from edtEmil editText field and makes
        it a string which is then used within the sendPasswordResetEmail method as the email to send the reset password link to*/
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //converts EditText to string
                String email = edtEmail.getText().toString().trim();

                //checks if EditText is empty and displays a message if it is to make user aware
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Email Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //method to send reset password link to user
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //checks if email has been sent to user and displays a message to let the user know
                                if (task.isSuccessful()) {
                                    Toast.makeText(reset.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //once task has completed successfully, user is sent back to log_in activity screen to attempt to login with their new password
                                    startActivity(new Intent(getApplicationContext(), log_in.class));
                                } else {
                                    //message displayed if reset email has not sent successfully. This may relate to wrong email being entered or user not being
                                    // connected to the internet
                                    Toast.makeText(reset.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}