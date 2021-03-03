package com.example.skylinestockservice;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylinestockservice.General.General;
import com.example.skylinestockservice.Model.Hire;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ViewItem.HireVIew;

public class HireActivity extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerView.LayoutManager layoutManager;
private TextView worth,message; private Button proceed;
    private int grandWorth = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
     recyclerView = findViewById(R.id.hireList);
     recyclerView.setHasFixedSize(true);
        /**
        *Avoid unnecessary layout passes by
         *  setting setHasFixedSize to true when changing
         *  the contents of the adapter does not change
         *  its height or the width.
         *  COLLAPSE
            */
        layoutManager = new LinearLayoutManager(this);//..of current context
        recyclerView.setLayoutManager(layoutManager);
        proceed = findViewById(R.id.proceed);
        worth = findViewById(R.id.worthP);
        message = findViewById(R.id.message);







        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //moves..
                Intent intent = new Intent(HireActivity.this,HireConfirmationDetails.class );
                //keys
               intent.putExtra("Grand Worth = R",String.valueOf(grandWorth));
                startActivity(intent); finish();    // throw new RuntimeException

            }
        });
    }
    //onStart

    @Override
    protected void onStart() {
        super.onStart();
        CheckPosition();
        final DatabaseReference hireListRef = FirebaseDatabase.getInstance().getReference()
                .child("hired list");//itemDetails(firebase)child(main)
        //firebaseRecycler goes to HIRE class )_declaration
        FirebaseRecyclerOptions<Hire> options = new FirebaseRecyclerOptions.Builder<Hire>()
                .setQuery(hireListRef.child("User View").child(General.currentOnlineUser.getContact()
              ).child("Items"),Hire.class).build(); //firebase hairachy
        //...
        //..two arg(s) 1st_The Java class that maps to the type of objects stored in the Firebase location.
        // ...2nd class that contains the Views in the layout tha
        //new recycler that consist of options
        FirebaseRecyclerAdapter<Hire, HireVIew> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Hire, HireVIew>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HireVIew hireVIew, final int i, @NonNull final Hire hire) {
                //A ViewHolder describes an item view and metadata about its place within the RecyclerView.
                hireVIew.itemNameOfTextView.setText(hire.getIname());
                //accessing from HireClass (Setters & getters )
                hireVIew.itemPriceOfTextView.setText("Price R "+ hire.getPrice());
                hireVIew.itemQuantityOfTextView.setText("Quantity"+hire.getQuantity());


                //...matching of price and quantity
                int monoItemPrice = Integer.valueOf(hire.getPrice())* Integer.valueOf(hire.getQuantity());

                grandWorth =grandWorth+ monoItemPrice;
                worth.setText("grand hiring price = R"+String.valueOf(grandWorth));
                //extends recycleView
                //so for every recycleView Item needs to get accessed

                hireVIew.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options [] =
                        new CharSequence[]{
                          "Edit","Remove"

                        };
            //Dialog for EditOptions
                        AlertDialog.Builder builder = new AlertDialog.Builder(HireActivity.this);
                        builder.setTitle("Hire Options");
                        //items id and an onclicklistener
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0 ){
                                    Intent intent = new Intent(HireActivity.this,
                                            ItemDetailsActivity.class); //[product as identity@
                                    intent.putExtra("paid",hire.getPaid());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    hireListRef.child("User View").child(General.currentOnlineUser.getContact())
                                            .child("Items").child(hire.getPaid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                //new completeListener added:
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            Toast.makeText(HireActivity.this, "item Removed!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(HireActivity.this,HActivity.class));
                                        }

                                                }
                                            });
                                }
                            }
                        });//now display a builder
                        builder.show();
                }
                });

            }
            //new recycler that consist of options
            @NonNull
            @Override
            public HireVIew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //formal view

                View view
                        = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.hire_items_layout,parent,false);
                HireVIew hireVIew = new HireVIew(view);
                return hireVIew;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();// RecyclerView.Adapter listens to a Firebase query
         
    }

    private void CheckPosition(){

        DatabaseReference hireRef=FirebaseDatabase.getInstance().getReference().child("hired list")
             .child(General.currentOnlineUser.getContact());
        hireRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //child_FIREBASE
                if(dataSnapshot.exists()){
                    String deliveringPosition =dataSnapshot.child("delivery").getValue()
                            .toString();
                    String userName = dataSnapshot.child("Iname").getValue().toString();
                    if(deliveringPosition.equals("delivered")){
                        worth.setText("Hello " +userName+" your oder is sent");
                        recyclerView.setVisibility(recyclerView.GONE);
                        //..message
                        message.setVisibility(View.VISIBLE);//view is visible
                        message.setText("your order has been sent,you will receive it to mentioned address ");
                        proceed.setVisibility(View.GONE);//view is invisible
                        Toast.makeText(HireActivity.this, "you can still hire more items", Toast.LENGTH_SHORT).show();
                        
                    }
                    else if(deliveringPosition.equals("not delivered")){
                        worth.setText("delivering position : not sent");
                        recyclerView.setVisibility(View.GONE);
                        
                        message.setVisibility(View.VISIBLE);
                        proceed.setVisibility(View.GONE);//view is invisible
                        Toast.makeText(HireActivity.this, "you can still hire more items", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}