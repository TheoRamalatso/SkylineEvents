package com.example.skylinestockservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.skylinestockservice.General.General;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HireConfirmationDetails extends AppCompatActivity {
    private String grandAmount ="";
    private EditText nameEditText,
                     contactEditText,
                     addressEditText,
                     suburbEditText;
    private Button   confirmOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_confirmation_details);
        grandAmount = getIntent().getStringExtra("Grand Worth = R");
        Toast.makeText(this, "All cost = R "+grandAmount, Toast.LENGTH_SHORT).show();

        nameEditText = findViewById(R.id.DeliveryName);
        contactEditText= findViewById(R.id.ContactNumber);
        addressEditText= findViewById(R.id.DeliveryAddress);
        suburbEditText= findViewById(R.id.Suburb);
        confirmOrderBtn =(Button)findViewById(R.id.confirm_final_order_btn);
        //-------------------------

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }

    private void verify() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){

            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(contactEditText.getText().toString())){

            Toast.makeText(this, "Enter your contact number!", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(addressEditText.getText().toString())){

            Toast.makeText(this, "Enter your delivery address!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(suburbEditText.getText().toString())){

            Toast.makeText(this, "Enter your suburb name!", Toast.LENGTH_SHORT).show();
        }
        else{ hireConfirmation();}
    }

    private void hireConfirmation() {
      final   String
                saveOngoingDate,
                saveOngoingTime;
        Calendar calendarDate =Calendar.getInstance(); //..instantiate date obj..
        SimpleDateFormat OngoingDateFormat= new SimpleDateFormat("MMM dd, yyy");//format
        saveOngoingDate = OngoingDateFormat.format(calendarDate.getTime());
        //assign format to String links format that
        // encounters date ARG with time(mm)
        SimpleDateFormat OngoingTimeFormat = new SimpleDateFormat("HH:mm:ss a");//format
        saveOngoingTime = OngoingTimeFormat.format(calendarDate.getTime());

        final DatabaseReference hireListRef = FirebaseDatabase.getInstance().getReference()
                .child("hired list").child(General.currentOnlineUser.getContact());
        HashMap<String,Object>hireMap = new HashMap<>();
        hireMap.put("totalAmount",grandAmount);
        hireMap.put("Iname",nameEditText.getText().toString());
        hireMap.put("contact",contactEditText.getText().toString());
        hireMap.put("date",saveOngoingDate);
        hireMap.put("time",saveOngoingTime);
        hireMap.put("address",addressEditText.getText().toString());//android Item layout
        hireMap.put("suburb",suburbEditText.getText().toString());//android Item layout
        hireMap.put("reduce"," ");
        hireMap.put("delivery","not delivered");

        hireListRef.updateChildren(hireMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //new onComplete
if(task.isSuccessful()){
    FirebaseDatabase.getInstance().getReference().child("hired list")
            .child("User View").child(General.currentOnlineUser.getContact())
            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                Toast.makeText(HireConfirmationDetails.this, "hire has been considered", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HireConfirmationDetails.this,HActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));finish();

            }
        }
    });
}
            }
        });//endOf


    }
}