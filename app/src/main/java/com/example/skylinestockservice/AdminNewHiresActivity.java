package com.example.skylinestockservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.skylinestockservice.Model.AdminHires;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewHiresActivity extends AppCompatActivity {
    private  RecyclerView hiredList;
    //recyclerView for hired list
    private DatabaseReference
    hiredListReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_hires);
        //hire list reference == orders
        hiredListReference = FirebaseDatabase
                .getInstance().getReference().child("hired list");
        hiredList =
                findViewById(R.id.hiredList1);//located of recycled(new Activity )
        hiredList.setLayoutManager(new LinearLayoutManager(this)
        );



    }

    @Override
    protected void onStart() {
        super.onStart();

            //options from class(AdminHires)
        //RecyclerOptions
        FirebaseRecyclerOptions<AdminHires> options =
          new FirebaseRecyclerOptions.Builder<AdminHires>()
        .setQuery(hiredListReference,AdminHires.class).build();
            ///Firebase recycler var Option
        //RecyclerAdapter
        /**
         * hiredListRefence_#class that maps to the type of objects stored in the Firebase
         * AdminHIres_#class that contains the Views in the layout that is shown for each object
         */
        FirebaseRecyclerAdapter <AdminHires,AdminHiresViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AdminHires, AdminHiresViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminHiresViewHolder adminHiresViewHolder, final int i, @NonNull final AdminHires adminHires) {
                //use object form adminHiresView
                adminHiresViewHolder.userName.setText("Name : " + adminHires.getIname());
                adminHiresViewHolder.userContactNumber.setText("Phone: "+ adminHires.getContact());
                adminHiresViewHolder.userDateTime.setText("hired at : "+adminHires.getTime()
                        + " "+adminHires.getDate());
                adminHiresViewHolder.userTotalPrice.setText("Total Amount = R "+adminHires.getTotalAmount());
                adminHiresViewHolder.userShippingAddress.setText("Delivery Address : "+adminHires.getAddress()
                        +" , "+adminHires.getSuburb());
                adminHiresViewHolder.ShowHiredBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { // i for position
                        String Id = getRef(i).getKey(); //gets position reference
                        Intent intent = new Intent(AdminNewHiresActivity.this,AdminClientItemsActivity.class);
                        intent.putExtra("id",Id);
                        startActivity(intent);
                    }
                });
                adminHiresViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options [] = new CharSequence[]{
                                "Yes",
                                "No"
                        };
                        //order state ans
                        // Referenced to current Activity
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewHiresActivity.this);
                        builder.setTitle("is this item transferred ?");
                        //character sequence passed  & diLogOnClick:
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if( which == 0){
                                    //position (i) as reference
                                    String Id = getRef(i).getKey(); //returns path as String
                                    RemoverHire(Id);
                                }
                                else{
                                    //..Reach End
                                    finish();
                                }

                            }
                        });builder.show();
                        /**
                         * Creates an {@link AlertDialog} with the arguments supplied to this
                         * builder and immediately displays the dialog
                         * Calling this method is functionally identical to:
                         *     AlertDialog dialog = builder.create();
                         *     dialog.show()
                         */
                    }
                });

            }




            @NonNull
            @Override
            public AdminHiresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiredlist
                        ,parent ,false );
                return new AdminHiresViewHolder(view);
            }
        };
//recycleView
    hiredList.setAdapter(firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();

    }


    public static class AdminHiresViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userContactNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button ShowHiredBtn;

        public AdminHiresViewHolder(@NonNull View itemView) {
            super(itemView);
            //links superClass with ResourceLocator
            userName = itemView.findViewById(R.id.hire_user_name1);
            //findView = extends from AppCompatActivity
            userContactNumber = itemView.findViewById(R.id.hire_phone_number1) ;
            userTotalPrice = itemView.findViewById(R.id.hire_total_price1);
            userDateTime = itemView.findViewById(R.id.hire_date_time1);
            userShippingAddress = itemView.findViewById(R.id.hire_address_city1);
            ShowHiredBtn = itemView.findViewById(R.id.show_all_items_btn);



        }
    }
    private void RemoverHire(String Id) {
        hiredListReference.child(Id).removeValue();
    }



}