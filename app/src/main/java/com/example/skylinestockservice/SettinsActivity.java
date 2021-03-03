package com.example.skylinestockservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skylinestockservice.General.General;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettinsActivity extends AppCompatActivity {
//we're in settings Intent
    //consider an update
    private CircleImageView profilePicture;
    private EditText contactNumberEdit ,fullNameEdit, addressEdit;
    private TextView changeProfileP,close,save;

    private Uri imageUri ;
    private String userUrl = "";
    private StorageTask uploadSchedule;
    private StorageReference storageProfilePictureRef;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settins);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        /**
         * Setting title
         */
     profilePicture = (CircleImageView)
             findViewById(R.id.uploadImage);
     fullNameEdit=(EditText)
             findViewById(R.id.settingsNames);
        contactNumberEdit=(EditText)
                findViewById(R.id.settingsContactNo);
        addressEdit=(EditText)
                findViewById(R.id.settingsAddress);
        changeProfileP=(TextView)
                findViewById(R.id.profileImageChange);
        close=(TextView)
                findViewById(R.id.close_settings_btn);
        save=(TextView)
                findViewById(R.id.update_account_settings_btn);
        UserDetailsDisplay(profilePicture,fullNameEdit,contactNumberEdit,addressEdit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...save String restrictions

                if(checker.equals("clicked")){
                    savesUserInformation();
                }else{
                    updatesUserInformation();
                }
            }
        });
        changeProfileP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //consider a trade String
                checker = "clicked";
                //Image Dependent
                /**
                 *for a user to access files for profile
                 * crop must be included in the manifest
                 */
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                .start(SettinsActivity.this);
                        ;
                        //set a default Ratio & return back:
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...if tapped terminates
                finish();
            }
        });
    }
        //....TRANSFORMATION methods....
    private void updatesUserInformation() {
        //user information update
        //consider Database Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                            .getReference().child("Users");
        //consider unordered collection of key-value pairs
        HashMap<String , Object>CurrentHashMap = new HashMap<>();
        CurrentHashMap.put("name",fullNameEdit.getText().toString());
        CurrentHashMap.put("phoneOrder",contactNumberEdit.getText().toString());
        CurrentHashMap.put("DeliveryAddress ",addressEdit.getText().toString());
        databaseReference.child(General.currentOnlineUser.getContact()).updateChildren(CurrentHashMap);
        //using an object from a certain class to Link
        //if condition met consider a  new Activity
        startActivity(new Intent(SettinsActivity.this,
                HActivity.class));
        Toast.makeText(this, ":" +
                "profile information updated!", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //consider Image Size Restrictions
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
        && resultCode == RESULT_OK && data !=null){
            CropImage.ActivityResult activityResult =CropImage.getActivityResult(data);
            imageUri = activityResult.getUri();
            //fetch activity Url assign to imageURi
            profilePicture.setImageURI(imageUri);//from preceding set AspectRatio(s) ,Make Update!

        }
        //failure
        else {

            Toast.makeText(this, "failure," +
                    "Retry!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettinsActivity.this,
                    SettinsActivity.class));
            finish();
        }
    }

    private void savesUserInformation() {
        //nonNULL fillEdittext:
        if(TextUtils.isEmpty(fullNameEdit.getText().toString())){
            Toast.makeText(this, "please" +
                    "fill in your Full name!", Toast.LENGTH_SHORT).show();
        }
      else  if(TextUtils.isEmpty(contactNumberEdit.getText().toString())){
            Toast.makeText(this, "please" +
                    "fill in your contactNO.!", Toast.LENGTH_SHORT).show();
        }
      else if(TextUtils.isEmpty(addressEdit.getText().toString())){
            Toast.makeText(this, "please" +
                    "fill in your delivery address!", Toast.LENGTH_SHORT).show();
        }
      else if(checker.equals("clicked")){
            uploadImage();  //met
        }
    }

    private void uploadImage() {
        //for an upload consider :
        final ProgressDialog  progressStatus = new ProgressDialog(this);
        progressStatus.setTitle("Upload Profile:");
        progressStatus.setMessage("Updating, wait..");
        progressStatus.setCanceledOnTouchOutside(false);//non external execution
        progressStatus.show();
        if(imageUri !=null){//if Uri contains infor:
            //storage References
             final StorageReference fileStorageReference
            = storageProfilePictureRef.child(General.currentOnlineUser.getContact()
            +".jpg"); //CHECK****

            uploadSchedule = fileStorageReference.putFile(imageUri);
            uploadSchedule.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    //if the task isn't in success:
                    if(!task.isSuccessful()){
                        //get some Exception
                        throw  task.getException();
                    }

                    return fileStorageReference.getDownloadUrl();
                }//continuation
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    //linked to complete Action
                    //positive statement
                    if(task.isSuccessful()){

                        Uri downloadUri = (Uri) task.getResult();
                        userUrl = downloadUri.toString();
                        //assign download Uri to a String
                        DatabaseReference databaseReference
                                =FirebaseDatabase.getInstance().getReference()
                                .child("Users");
                        HashMap<String, Object>CurrentHashMap = new HashMap<>();
                        CurrentHashMap.put("name",fullNameEdit.getText().toString());
                        CurrentHashMap.put("phoneOrder  ",contactNumberEdit.getText().toString());
                        CurrentHashMap.put("DeliveryAddress ",addressEdit.getText().toString());
                        CurrentHashMap.put("image",userUrl);
                        databaseReference.child(General.currentOnlineUser.getContact()
                        ).updateChildren(CurrentHashMap);
                        progressStatus.dismiss();//...
                        startActivity(new Intent(SettinsActivity.this,HActivity.class));
                        Toast.makeText(SettinsActivity.this, "" +
                                "profile Information updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        progressStatus.dismiss();
                        Toast.makeText(SettinsActivity.this, "cannot update!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "" +
                    "file was not chosen", Toast.LENGTH_SHORT).show();
        }

    }
    //last execution of two MainButtons

    private void UserDetailsDisplay(final CircleImageView profilePicture, final EditText fullName, final EditText contactNumber, final EditText address) {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(General.currentOnlineUser.getContact());//static object currentOnline
     //new ValueEventListener
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()) {
            //if user Data exits
                //search for image first ,if it exits
            if(dataSnapshot.child("image").exists()){
                String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String contact = dataSnapshot.child("contact").getValue().toString();
           //    String addressFormal = dataSnapshot.child("DeliveryAddress").getValue().toString();

                //imageRetrieval+updateLoad
                Picasso.get().load(image).into(profilePicture);
                fullNameEdit.setText(name);
                contactNumberEdit.setText(contact);
             //  addressEdit.setText(addressFormal);
            }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });//End


    }

}