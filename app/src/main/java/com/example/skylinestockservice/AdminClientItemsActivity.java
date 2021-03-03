package com.example.skylinestockservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skylinestockservice.Model.Hire;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ViewItem.HireVIew;
import ViewItem.ViewItem;

public class AdminClientItemsActivity extends AppCompatActivity {
    private RecyclerView itemList;
    RecyclerView.LayoutManager layoutManager; // layout var
    private DatabaseReference hiredListRef;
    private String userIdentity = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //*     onCreate()  called when the when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_items);


    userIdentity = getIntent().getStringExtra("id"); // get String from prev Intend
        //Id string *(Arg)
        itemList = findViewById(R.id.itemsList);
        itemList.setHasFixedSize(true); // bool Arg
        /**
         * since RecyclerView can perform several optimizations if it can know in advance that RecyclerView's
         *  size is not affected by the adapter contents
         *
         * hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
         */
        layoutManager = new LinearLayoutManager(this);//referenced to current activity
        itemList.setLayoutManager(layoutManager);
        //use Defined recyclerView to set a layout
        //consider database References
        hiredListRef = FirebaseDatabase.getInstance().getReference()
                .child("hired list").child("Admin View").child(userIdentity)
                .child("Items"); //..Accessing items from firebase
            //Recall userIdentity contains String from previous intent

    }

    @Override
    protected void onStart() {
        super.onStart();//* Dispatch onStart() to all fragments.
            /**
             * onStart() call makes the activity visible to the user, as the app prepares
             * for the activity to enter the foreground and become interactive
             *
             *  basically onStart() is called when the activity is becoming visible to the user.
             */
      // Var to assign(option)..RecyclerOptions
        FirebaseRecyclerOptions <Hire> options
                = new FirebaseRecyclerOptions.Builder<Hire>()
                .setQuery(hiredListRef,Hire.class).build();
        /**
         * setQuery  Set the Firebase query to listen
         * to, along with a class to which snapshots are parsed
         *  snapshot is a picture of the data at a particular database reference at
         *  a single point in time. Calling val() / getValue() on a snapshot returns the
         */
        //The firebaseRecyclerAdapter contains the  hire model and the
        //The class that is extended to the Recycler
        //of which it contains the item details and a set of itemClick Listener
            // HireView


        FirebaseRecyclerAdapter<Hire, HireVIew> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<Hire, HireVIew>(options) {
            //contains query of fireBase
            //consider the fireBaseRecyclerAdapter
            @Override
            protected void onBindViewHolder(@NonNull HireVIew hireVIew, int i, @NonNull Hire hire) {
                //accessing var(s) in viewItem
                hireVIew.itemPriceOfTextView.setText("Price R " + hire.getPrice());
                hireVIew.itemNameOfTextView.setText(hire.getIname());
                hireVIew.itemQuantityOfTextView.setText("Quantity: " +hire.getQuantity());
                //add quantity

            }

            @NonNull
            @Override
            public HireVIew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //choose a specific layout
                // consider L_INFLATER _instantiate layout XML file into its corresponding view objects
                View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.hire_items_layout,parent,false
                );
                HireVIew hireVIew = new HireVIew(view);
                return  hireVIew;

            }
        };

//use the recycleList to set on the firebase Adapter
        itemList.setAdapter(firebaseRecyclerAdapter);
        //start
        firebaseRecyclerAdapter.startListening();
    }

}