package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminBlogActivity extends AppCompatActivity {
    private String saveCurrentDate, saveCurrentTime, downloadImageUrl;
    private ImageView InputBlogImage;
    private EditText InputBlogTitle, InputBlogDescription;
    private Button AddBlogButton;
    private String BlogTitle, BlogDescription, BlogRandomKey, ContactAddress, ContactName, ContactEmail;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    private BottomNavigationView bottomNav;
    Uri ImageUri;
    private static final int GalleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_blog );
        bottomNav = findViewById( R.id.bottom_navigation_admin_only_home_blog);
        bottomNav.setOnNavigationItemSelectedListener( navListener );

        InputBlogImage = findViewById(R.id.select_blog_image);
        InputBlogTitle = findViewById( R.id.blog_title );
        InputBlogDescription = findViewById( R.id.blog_description );
        AddBlogButton = findViewById( R.id.add_blog_button );
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Blog Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Blog");
        loadingBar = new ProgressDialog(this);
        ContactName = LoginActivity.currentuser.getName();
        ContactAddress = LoginActivity.currentuser.getAddress();
        ContactEmail = LoginActivity.currentuser.getEmail();
        int len = ContactEmail.length();
        ContactEmail = ContactEmail.substring( 0, len-4) + ".com";
        InputBlogImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        } );

        AddBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateBlogDetails();
            }
        });
    }

    private void ValidateBlogDetails() {
        BlogTitle = InputBlogTitle.getText().toString();
        BlogDescription = InputBlogDescription.getText().toString();
        if(ImageUri == null)
        {
            Toast.makeText(this, "Please add Image.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(BlogTitle))
        {
            Toast.makeText(this, "Please add Title.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(BlogDescription))
        {
            Toast.makeText(this, "Please add Description.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreBlogInformation();
        }
    }


    private void StoreBlogInformation() {
        loadingBar.setTitle("Storing Information");
        loadingBar.setMessage("Please wait.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        BlogRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + BlogRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                String message = e.toString();
                Toast.makeText(AdminBlogActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminBlogActivity.this, "Product Image uploaded successfully.", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminBlogActivity.this, "Got image url successfully" , Toast.LENGTH_LONG).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        final HashMap<String, Object > blogMap = new HashMap<>();
        blogMap.put("pid", BlogRandomKey);
        blogMap.put("date", saveCurrentDate);
        blogMap.put("time", saveCurrentTime);
        blogMap.put("title", BlogTitle);
        blogMap.put("description", BlogDescription);
        blogMap.put("image", downloadImageUrl);
        blogMap.put("contact_email", ContactEmail);
        blogMap.put("contact_address", ContactAddress);
        blogMap.put("contact_name", ContactName);

        ProductRef.child(BlogRandomKey).updateChildren(blogMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    ProductRef.getParent().child("Blog Admin").child(LoginActivity.currentuser.getEmail()).child(BlogRandomKey).updateChildren(blogMap).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(AdminBlogActivity.this, "Information stored successfully." , Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AdminBlogActivity.this, AdminMainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                String error = task.getException().toString();
                                Toast.makeText(AdminBlogActivity.this, "Error : " + error , Toast.LENGTH_LONG).show();
                            }
                        }
                    } );
                }
                else
                {
                    loadingBar.dismiss();
                    String error = task.getException().toString();
                    Toast.makeText(AdminBlogActivity.this, "Error : " + error , Toast.LENGTH_LONG).show();
                }
            }
        });
        /*ProductRef.push().setValue(blogMap);
        loadingBar.dismiss();
        Toast.makeText(AdminBlogActivity.this, "Information stored successfully." , Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AdminBlogActivity.this, AdminMainActivity.class);
        startActivity(intent);*/
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            InputBlogImage.setImageURI(ImageUri);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.navigation_blog_admin_only_home) {
                        Intent intent = new Intent( AdminBlogActivity.this, AdminMainActivity.class );
                        startActivity( intent );
                    }
                    return true;
                }
            };

}