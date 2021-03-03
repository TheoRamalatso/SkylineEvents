package com.example.skylinestockservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skylinestockservice.General.General;
import com.example.skylinestockservice.Model.UserDef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText contactNumbLogin, passwordLogin,LEmail;
    private Button loginMain;
    private  String ParentDName = "Users";
    private ProgressDialog loadingStatusBar;
    private TextView admin, notAdmin,forgetPassword;
    private CheckBox rememberMeBox;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //..
        firebaseAuth =FirebaseAuth.getInstance();
        loginMain =(Button) findViewById(R.id.login_btn);
        contactNumbLogin=findViewById(R.id.loginEmail);
        passwordLogin=findViewById(R.id.password);
        forgetPassword= findViewById(R.id.forgetPassword);


        //...

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ResetForget.class);
                startActivity(intent);
            }
        });
         loadingStatusBar = new ProgressDialog(this);
         rememberMeBox = (CheckBox) findViewById(R.id.rememberme);
         //..
        admin = findViewById(R.id.admin); notAdmin= findViewById(R.id.notadmin);


        /**
         * PAPER
         * AS storage option
         *data structure changes are handled automatically
         * Grade->dependency
         */
        Paper.init(this);
        loginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        //setInvisible
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              loginMain.setText("Login Admin");
                /**
                 *if chosen allows admin
                 * login access
                 */
              admin.setVisibility(View.INVISIBLE);
              notAdmin.setVisibility(View.VISIBLE);
              //firebaseParentName
              ParentDName = "admin";//allows adminfield 'key
            }
        });
        //set visible_default
        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMain.setText("Login ");
                admin.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                ParentDName = "Users";//allows users 'key
            }
        });


    }
    private void LoginUser() {
        final String security =passwordLogin.getText().toString();
        final String contact =contactNumbLogin.getText().toString();
        //______________________________________________________
        if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Enter your contact number!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(security)){
            Toast.makeText(this, "fill in the password !", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingStatusBar.setTitle("LogIn Account:");
            loadingStatusBar.setMessage("verifying Credentials,Please Wait..");
            loadingStatusBar.setCanceledOnTouchOutside(false);
            loadingStatusBar.show();


            firebaseAuth.signInWithEmailAndPassword(contactNumbLogin.getText().toString(),
                    passwordLogin.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(LoginActivity.this,
                              HActivity.class);
                    startActivity(intent);

                    }
                }
            });


         //  PermitAccess_Account (contact,security);
        }
    }
    private void PermitAccess_Account(final String contact, final String security) {

        //check on Remember me
        if(rememberMeBox.isChecked()){
            //pass in Model prevalent
            //..public fields accessible
            Paper.book().write(General.UserPhoneEncrypt,contact);
            Paper.book().write(General.UserPasswordEncrypt,security);
        }


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

                         if(ParentDName.equals("admin")){

                             Toast.makeText(LoginActivity.this, "successfully logged in admin field:"
                                     , Toast.LENGTH_SHORT).show();
                             //destination
                             loadingStatusBar.dismiss();

                             Intent intent = new Intent(LoginActivity.this,
                                     AdminField.class);
                             //takes you to admin field iff parentDBName chosen
                             //.....is admin[addItems]
                             startActivity(intent);
                         }
                    else if(ParentDName.equals("Users")){
                             Toast.makeText(LoginActivity.this, "successfully logged in:"
                                     , Toast.LENGTH_SHORT).show();
                             //destination
                             loadingStatusBar.dismiss();

                             Intent intent = new Intent(LoginActivity.this,
                                     HActivity.class);
                             General.currentOnlineUser = userData;
                             startActivity(intent);


                         }




                     } }
                }
                else { //failure
                    Toast.makeText(LoginActivity.this, "Account with this"+contact
                            +" number do not exits",
                            Toast.LENGTH_SHORT).show(); loadingStatusBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Create an account if you don't have",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}