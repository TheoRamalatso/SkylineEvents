package com.example.skylinestockservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skylinestockservice.Model.Items;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewItem.ViewItem;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

//import com.rey.material.widget.TextView;

public class HActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String grandAmount ="";
    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductsRef;
    NavigationView navigationView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private  DatabaseReference ItemRef;
    private String type = ""; private String uid;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h);
        ItemRef = FirebaseDatabase.getInstance().getReference().child("Items");
        firebaseAuth = FirebaseAuth.getInstance();
        Paper.init(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HOME PAGE");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                startActivity(new Intent(HActivity.this,HireActivity.class));
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ///............................
        ///..............................
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //added
//        navigationView.setNavigationItemSelectedListener(this);
        View  headerView = navigationView.getHeaderView(0);
        //access the textview for displaying the name
        TextView userNameTextView = findViewById(R.id.userProfileName);

        uid =firebaseAuth.getCurrentUser().getUid();
        CircleImageView profileImageCircleView = headerView.findViewById(R.id.profile_image);
      //  userNameTextView.setText(General.currentOnlineUser.getContact());

       // Picasso.get().load(General.currentOnlineUser.getImage()).placeholder(R.drawable.profil)
              //  .into(profileImageCircleView);
        //replaces current:

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }
    @Override
    protected void onStart() {
        super.onStart();
        //takes Recycler options from class ***Items** which returns MULT Item details
        //clipData to firebase
        FirebaseRecyclerOptions<Items>options =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(ItemRef,Items.class).build();
        //builder which is linked to the dependencies
        FirebaseRecyclerAdapter<Items, ViewItem>adapter =
                //def firebaseRecyclerAdapter consist of options
                new FirebaseRecyclerAdapter<Items, ViewItem>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ViewItem viewItem, int i, @NonNull final Items items) {
                        //display the textView
                        //class model' items'
                        //class items
                        viewItem.textItemName.setText(items.getiName());
                        viewItem.textItemDescription.setText(items.getDescription());
                        viewItem.textItemPrice.setText("Price = R"+items.getPrice());

                        //picasso [retrieve images from database {implementation:ACCESS}]
                        //using object viewItem
                        Picasso.get().load(items.getImage()).into(viewItem.itemImageView);

                        //imageView Ids
                        viewItem.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(HActivity.this,
                                        ItemDetailsActivity.class);
                                intent.putExtra("paid",items.getPaid());
                                startActivity(intent);
                                //EVERY string key need to match with item model setters and getters
                            }

                        });




                    }

                    @NonNull
                    @Override
                    public ViewItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        //accessing layout
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.items_layout,parent,false);
                        ViewItem holder = new ViewItem(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);// an addapter of recy
        adapter.startListening();
        //previews
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.h, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_hire) {
            startActivity(new Intent(HActivity.this,HireActivity.class));
        }
        else if(id == R.id.profile_image){
            Toast.makeText(this, "profile settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HActivity.this, SettinsActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_category) {

        }
        else if (id == R.id.nav_settings) {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HActivity.this, SettinsActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent = new Intent(HActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}