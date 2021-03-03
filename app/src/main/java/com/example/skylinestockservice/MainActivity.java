package com.example.skylinestockservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skylinestockservice.General.General;
import com.example.skylinestockservice.Model.UserDef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinButton;
    private ProgressDialog loadingStatusBar;
    private Button lButton;
    private  String ParentDName = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mainLocators
        joinButton = (Button) findViewById(R.id.join);
        lButton = (Button) findViewById(R.id.loginbtnMain);
        loadingStatusBar = new ProgressDialog(this);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Reg.class));
            }
        });
            lButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            });




        Paper.init(this);
        //Read paper:
        String UserPhoneEncrypt = Paper.book().read(General.UserPhoneEncrypt);
        String UserPasswordEncrypt = Paper.book().read(General.UserPasswordEncrypt);


        //NONE_EMPTY:
        if(UserPhoneEncrypt!="" && UserPasswordEncrypt!="") {

            if(!TextUtils.isEmpty(UserPhoneEncrypt) &&
                    !TextUtils.isEmpty(UserPasswordEncrypt)){
                //..permission
                AllowPermission(UserPhoneEncrypt,UserPasswordEncrypt);
                //check on progressStatus
                //...

                loadingStatusBar.setTitle("Saved!, logged in Account:");
                loadingStatusBar.setMessage("Please Hold on..");
                loadingStatusBar.setCanceledOnTouchOutside(false); //non Accessible
                loadingStatusBar.show();
            }
        }

    }
    private void AllowPermission(final String contact,final String security) {
        //DATABASE REFERENCE
        final DatabaseReference databaseRootReference;
        databaseRootReference = FirebaseDatabase.getInstance().getReference();

        databaseRootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(ParentDName).child(contact).exists()){
                    //an object initialized to dataSnapshot
                    UserDef userData = dataSnapshot.child(ParentDName).child(contact).getValue(UserDef.class);//linked to the other model
                    //retrieve data by setter && getters
                    if(userData.getContact().equals(contact)){
                        //restrictions
                        if(userData.getSecurity().equals(security)){
                            Toast.makeText(MainActivity.this, "successfully logged in:"
                                    , Toast.LENGTH_SHORT).show();
                            //destination
                            loadingStatusBar.dismiss();

                            Intent intent = new Intent(MainActivity.this,
                                    HActivity.class);
                            //recall storing Prev' information
                            General.currentOnlineUser = userData;
                            startActivity(intent);
                        } }
                }
                else { //failure
                    Toast.makeText(MainActivity.this, "Account with this"+contact
                                    +" number do not exits",
                            Toast.LENGTH_SHORT).show(); loadingStatusBar.dismiss();
                    Toast.makeText(MainActivity.this, "Create an account if you don't have",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}