package com.example.skylinestockservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.skylinestockservice.General.General;
import com.example.skylinestockservice.Model.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ItemDetailsActivity extends AppCompatActivity {
    private Button hireBtn;
    private ImageView itemImageView;
    private TextView itemPrice, itemDescription, itemName;
    private ElegantNumberButton numberButton;//item number selection

    private String state = "Normal";
    private String itemID = "";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        //EVERY string key need to match with item model setters and getters****
        //otherwise it will not store the keyString of a certain object
        //which will imply Null retrieval to nonNull arg
        //paid Represents itemID(product as an ID)

        itemID = getIntent().getExtras().getString("paid");//access

        hireBtn = (Button) findViewById(R.id.hireItemBtn);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        itemImageView = (ImageView) findViewById(R.id.itemImageDetails);
        itemName = (TextView) findViewById(R.id.itemNameDetails);
        itemDescription = (TextView) findViewById(R.id.itemDescriptionDetails);
        itemPrice = (TextView) findViewById(R.id.itemPriceDetails);
        getItemDetails(itemID);
        //..consider Item Details


        //...
        hireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //consider condition states
                if (state.equals("order hired")
                        || state.equals("order delivered")) {
                    Toast.makeText(ItemDetailsActivity.this,
                            "you can still hire more products"
                                    + " if your order is hired or delivered", Toast.LENGTH_SHORT).show();

                    //..if preceding c ondition not met:
                } else {
                    addingToHireList();
                }
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();

     //   CheckOrderState();
    }


    //...onStart method
//    @Override
//    protected void onStart() {
//        super.onStart();
//        orderPosition();
//    }
    private void addingToHireList() {
        //firebase
        String
                saveOngoingDate,
                saveOngoingTime;
        Calendar calendarDate = Calendar.getInstance(); //..instantiate date obj..
        SimpleDateFormat OngoingDateFormat = new SimpleDateFormat("MMM dd, yyy");//format
        saveOngoingDate = OngoingDateFormat.format(calendarDate.getTime());
        //assign format to String links format that
        // encounters date ARG with time(mm)
        SimpleDateFormat OngoingTimeFormat = new SimpleDateFormat("HH:mm:ss a");//format
        saveOngoingTime = OngoingTimeFormat.format(calendarDate.getTime());
        //assign format to String links format that
        // encounters date specifically (time) ARG with time(mm)

        //consider dataBase new schema
        final DatabaseReference hireListRef =
                FirebaseDatabase.getInstance().getReference().child("hired list");

        //..storage
        final HashMap<String, Object> hireMap = new HashMap<>();
        //put string and an object:
        hireMap.put("paid", itemID);//itemID
        hireMap.put("Iname", itemName.getText().toString());
        hireMap.put("price", itemPrice.getText().toString());
        hireMap.put("date", saveOngoingDate);
        hireMap.put("time", saveOngoingTime);
        hireMap.put("quantity", numberButton.getNumber());//android Item layout
        hireMap.put("reduce", " ");

        hireListRef.child("User View").child(General
                .currentOnlineUser.getContact()).child("Items").child(itemID)
                .updateChildren(hireMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //..positive execution
                    hireListRef.child("Admin View").child(
                            General.currentOnlineUser.getContact()).child("Items")
                            .child(itemID).updateChildren(hireMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ItemDetailsActivity.this,
                                                "Added to hire list", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ItemDetailsActivity.this,
                                                HActivity.class));//==>HActivity!

                                    }
                                }
                            });//end of onCompleteListener

                }
            }
        });//end of onCompleteListener
        //update schema with hireMap
    }

    //
////..added:
    private void getItemDetails(String itemID){

        DatabaseReference itemRef = FirebaseDatabase.getInstance().getReference().child("Items").child(itemID);
        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Items items = dataSnapshot.getValue(Items.class);
                    itemName.setText(items.getiName());
                    itemPrice.setText(items.getPrice());
                    itemDescription.setText(items.getDescription());
                    Picasso.get().load(items.getImage())
                            .into(itemImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void CheckOrderState() { //progress
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference()
                .child("hired list").child(General.currentOnlineUser.getContact());
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // instance contains data from a Firebase Database location
                if (dataSnapshot.exists()) {
                    String shippingPosition =
                            dataSnapshot.child("delivery").getValue().toString();
                    if (shippingPosition.equals("delivered")) {
                        state = "order delivered";
                    } else if (shippingPosition.equals("not delivered")) {
                        state = "order hired";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });//end of
    }
}


