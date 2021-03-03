package com.example.skylinestockservice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Reg extends AppCompatActivity {
   private EditText nameFill,contactNumber,password,mEmail;
    private Button Acc;
    private ProgressDialog loadingStatusBar;
    private String MobilePattern = "[0-9]{10}";
    private FirebaseAuth firebaseAuth;
    private  String uid;
     ProgressBar progressBar;


    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        //

        Acc = findViewById(R.id.createAcc);
        nameFill=(EditText) findViewById(R.id.registerName);
        contactNumber=findViewById(R.id.registerContactDigits);
        password=findViewById(R.id.registerpassword);
        mEmail=findViewById(R.id.registerEmail);
        loadingStatusBar= new ProgressDialog(this);
        firebaseAuth =FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);


        Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    progressBar.setVisibility(View.VISIBLE);

                CreateAcc();
            }
        }); }
    private void CreateAcc() {
        String email = mEmail.getText().toString();
        String name =nameFill.getText().toString();
        String contact =contactNumber.getText().toString();
        String security =password.getText().toString(); // contains strings
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(contact) && TextUtils.isEmpty(security)){
            Toast.makeText(this, "Please provide your details!", Toast.LENGTH_SHORT).show();
        }
        //number restriction
         if(contact.matches(MobilePattern)) {

            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
        }
     else   if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Enter your contact number!", Toast.LENGTH_SHORT).show();
        }
     else     if(TextUtils.isEmpty(security)){
            Toast.makeText(this, "fill in the password !", Toast.LENGTH_SHORT).show();
        }



     else{

         loadingStatusBar.setTitle("create Account:");
         loadingStatusBar.setMessage("varifying Credentials,Please Wait..");
         loadingStatusBar.setCanceledOnTouchOutside(false);
         loadingStatusBar.show();

                 ValidateContactNumber(name, contact, security,email);
             uid =firebaseAuth.getCurrentUser().getUid(); //userIdentity

            firebaseAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                loadingStatusBar.dismiss();
                                startActivity(new Intent(Reg.this,
                                        LoginActivity.class));
                                Toast.makeText(Reg.this, "Aunthenticated successfully", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(Reg.this, "Error"+message, Toast.LENGTH_LONG).show();
                            }
                            loadingStatusBar.dismiss();
                        }
                    });
        }
    }
    private void ValidateContactNumber(final String name, final String contact, final String security,final String email) {

        final DatabaseReference databaseRootReference;
        databaseRootReference = FirebaseDatabase.getInstance().getReference();
        databaseRootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check if the user not entered (existing details)..
                if(!(dataSnapshot.child("Users").child(contact).exists())) {
                    HashMap<String, Object> userstorageMap = new HashMap<>();
                    userstorageMap.put("uid",uid);
                    userstorageMap.put("contact", contact);
                    userstorageMap.put("name", name);
                    userstorageMap.put("email",email);
                 //   userstorageMap.put("email", email);
                    databaseRootReference.child("Users").child(contact)
                            .updateChildren(userstorageMap);
                }
                //Redun'_elemination
                else{
                    Toast.makeText(Reg.this, "The "+contact+"already exits!",
                            Toast.LENGTH_SHORT).show();
                   //then dismiss the status bar
                    loadingStatusBar.dismiss();  // progress on NuLL
                    Toast.makeText(Reg.this, "Enter a new contact number",
                            Toast.LENGTH_SHORT).show();
                    //return-Main
                    Intent intent = new Intent(Reg.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}