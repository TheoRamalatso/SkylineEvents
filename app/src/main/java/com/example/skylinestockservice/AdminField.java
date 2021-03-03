package com.example.skylinestockservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminField extends AppCompatActivity {

    private Button Logout, CheckHire, maintainItems;
ImageView marquees,glasses,flowers, flooring,crockey,cutlery,furniture;
ImageView  decor,drapping,heatingequipment;
ImageView   linen,outdoorfurniture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_field);
//        final int[] Ids = {R.id.marquees, R.id.glasses, R.id.flowers,
//                R.id.flooring,R.id.crockey,R.id.cutlery,R.id.furniture,
//                R.id.decor,R.id.decor,R.id.drapping,R.id.heatingequipment,
//                R.id.linen,R.id.outdoorfurniture};
         Logout =findViewById(R.id.admin_logout_btn);
         CheckHire = findViewById(R.id.checkHire);
         maintainItems= findViewById(R.id.maintain);


    Logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //to Main
            startActivity(new Intent(AdminField.this,MainActivity.class)
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)  );
            finish();


        }
    });
    CheckHire.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //admin new AdminHireActivity
            //create
            Intent intent = new Intent(AdminField.this, AdminNewHiresActivity.class);
            startActivity(intent);
        }
    });
    maintainItems.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        startActivity(new Intent(AdminField.this,MainActivity.class)
                .putExtra("Admin","Admin"));
        }
    });




         marquees =findViewById(R.id.marquees);
         glasses =findViewById(R.id.glasses);
         flowers =findViewById(R.id.flowers);
         flooring =findViewById(R.id.flooring);
         crockey =findViewById(R.id.crockey);
         cutlery =findViewById(R.id.cutlery);
         furniture =findViewById(R.id.furniture);
         decor =findViewById(R.id.decor);
         drapping =findViewById(R.id.drapping);
         heatingequipment =findViewById(R.id.heatingequipment);
         linen =findViewById(R.id.linen);
         outdoorfurniture =findViewById(R.id.outdoorfurniture);

        marquees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                .putExtra("category","marquees"));
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","glasses"));
            }
        });
        flowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","flowers"));
            }
        });
        flooring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","flooring"));
            }
        });
        crockey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","crockey"));
            }
        });
        cutlery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","cutlery"));
            }
        });
        furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","furniture"));
            }
        });
        decor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","decor"));
            }
        });
        drapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","drapping"));
            }
        });
        heatingequipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","heatingequipment"));
            }
        });
        linen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","linen"));
            }
        });
        outdoorfurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminField.this,AddItem.class)
                        .putExtra("category","outdoorfurniture"));
            }
        });


    }
}