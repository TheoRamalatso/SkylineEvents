package com.example.skylinestockservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetForget extends AppCompatActivity {
     Button sendReset ;
     EditText mail;
     Toolbar toolbar;
     ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_forget);

        firebaseAuth =FirebaseAuth.getInstance();
//        toolbar = findViewById(R.id.toolbar2);
        progressBar = findViewById(R.id.progressBar2);
        sendReset = (Button) findViewById(R.id.send);
        mail = (EditText)findViewById(R.id.resetEmail);
//        toolbar.setTitle("password Reset");
        sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailString = mail.getText().toString();
                if(TextUtils.isEmpty(EmailString)){
                    Toast.makeText(ResetForget.this, "Please provide a valid email !", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(EmailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ResetForget.this, "Check your Email for password reset" +
                                        ".", Toast.LENGTH_SHORT).show();

                                //update password on firebase



                            startActivity(new Intent(ResetForget.this,LoginActivity.class));
                            }
                            else{
                                String ExceptionMessage = task.getException().getMessage();
                                Toast.makeText(ResetForget.this, "Error " +ExceptionMessage+
                                        "Couldn't send.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}