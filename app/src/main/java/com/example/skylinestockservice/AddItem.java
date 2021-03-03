package com.example.skylinestockservice;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddItem extends AppCompatActivity {
    private static final int ChooseFile = 1; // key

    private  String categoryLabel;
    private ImageView enterItemImage;
    private Button addNewItem;
    private EditText enterItemName,enterItemDescription,enterItemPrice;
    private Uri ImageUri;
    private StorageReference itemImagesReference;
    private String description , price, Iname,saveCurrentDate,saveCurrentTime;
    private String itemRandomKey;
    private String DownloadImageURI;
    private DatabaseReference itemsRef;
    private ProgressDialog progressLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        categoryLabel = getIntent().getExtras()
                .get("category").toString(); //certain item converted
        Toast.makeText(this, "Add:"+categoryLabel, Toast.LENGTH_SHORT).show();

        //...

        itemImagesReference = FirebaseStorage.getInstance().getReference().child("product image");
        itemsRef= FirebaseDatabase.getInstance().getReference().child("Items");
        //.....
        //.....
        addNewItem =(Button)findViewById(R.id.addItem);
        enterItemImage =(ImageView) findViewById(R.id.uploadImage);
        enterItemName =(EditText) findViewById(R.id.itemName);
        enterItemDescription=findViewById(R.id.itemDescription);
        enterItemPrice=findViewById(R.id.itemPrice);
        //Locate progress
        progressLoadingBar = new ProgressDialog(this);//reference to current act

        enterItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFiles();
            }
        });
        //add newProductButton
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }
    private void OpenFiles() {
        Intent filesIntent = new Intent();
        filesIntent.setAction(Intent.ACTION_GET_CONTENT);
        filesIntent.setType("image/*");
        startActivityForResult(filesIntent,ChooseFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ChooseFile && resultCode == RESULT_OK
        && data !=null ){
            ImageUri = data.getData();//get image uniform resource identifier
                                        //Strings of char(s)
            enterItemImage.setImageURI(ImageUri); //resource locator ==> set dataFrom Image
        }

    }
    //validate
    private void ValidateProductData() {
    description = enterItemDescription.getText().toString();//edit==?string
        price = enterItemPrice.getText().toString();//edit==?string
        Iname = enterItemName.getText().toString();//edit==?string
    //emptyData
    if(ImageUri == null){

        Toast.makeText(this, "item name must be provided..", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(description)) // if itemDescription is null
    {
        Toast.makeText(this, "provide Description..", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(price)) // if itemDescription is null
    {
        Toast.makeText(this, "provide price..", Toast.LENGTH_SHORT).show();
    }
    else if(TextUtils.isEmpty(Iname)) // if itemDescription is null
    {
        Toast.makeText(this, "provide Item name..", Toast.LENGTH_SHORT).show();
    }
    else {
        saveItemInformation();

    }

    }

    private void saveItemInformation() {
        //progress
        progressLoadingBar.setTitle("Add new Item:");
        progressLoadingBar.setMessage("Adding an item ,Please Wait..");
        progressLoadingBar.setCanceledOnTouchOutside(false);
        progressLoadingBar.show();





        //simpleDateFormat for date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate =new SimpleDateFormat("MMM dd, YYYY  ");
        saveCurrentDate = currentDate.format(calendar.getTime());

            //simpleDateFormat for time
        SimpleDateFormat currentTime =new SimpleDateFormat(" HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        //..
        itemRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath =itemImagesReference.child(ImageUri.getLastPathSegment()
        +itemRandomKey+" .jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        //instantiate OnFilureListiner
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddItem.this, "", Toast.LENGTH_SHORT).show();
                progressLoadingBar.dismiss(); // fade bar if reach failure
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItem.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        //say if task isn't successful
                        if (!task.isSuccessful()) {

                            //..then throw exceptional event (from prog' instruc')
                            throw task.getException();
  }

                        DownloadImageURI = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        //other case
                        if (task.isSuccessful()) {
                            DownloadImageURI = task.getResult().toString();

                            Toast.makeText(AddItem.this, "Item image Url uploaded.. ", Toast.LENGTH_SHORT).show();
                            SaveItemInformationToDB();
                        }
                    }
                });
            }
        });

    }

    private void SaveItemInformationToDB() {

        HashMap<String,Object> ItemMap = new HashMap<>();
        ItemMap.put("paid",itemRandomKey);
        ItemMap.put("date",saveCurrentDate);
        ItemMap.put("time",saveCurrentTime);
        ItemMap.put("description",description);
        ItemMap.put("Image",DownloadImageURI);
        ItemMap.put("category",categoryLabel);
        ItemMap.put("price",price);
        ItemMap.put("Iname",Iname);
        itemsRef.child(itemRandomKey).updateChildren(ItemMap)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    //..move to another activity
                    startActivity(new Intent(AddItem.this,AdminField.class));

                    progressLoadingBar.dismiss();
                    Toast.makeText(AddItem.this, "Item is successfully added!", Toast.LENGTH_SHORT).show();
                }
                else
                {     progressLoadingBar.dismiss();
                    String MS = task.getException().toString(); //task exception message
                    Toast.makeText(AddItem.this, "Error:"+MS, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}