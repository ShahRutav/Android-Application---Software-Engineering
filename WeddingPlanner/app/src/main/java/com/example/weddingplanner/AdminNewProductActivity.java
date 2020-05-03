package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewProductActivity extends AppCompatActivity {
    private String CategoryName, saveCurrentDate, saveCurrentTime, downloadImageUrl;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private Button AddProductButton;
    private String ProductName, ProductDescription, ProductPrice, ProductRandomKey, ContactAddress, ContactName, ContactEmail;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    Uri ImageUri;
    private static final int GalleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_new_product );
        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(AdminNewProductActivity.this, CategoryName, Toast.LENGTH_LONG).show();
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        AddProductButton = (Button) findViewById(R.id.add_product_button);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        loadingBar = new ProgressDialog(this);
        ContactName = LoginActivity.currentuser.getName();
        ContactAddress = LoginActivity.currentuser.getAddress();
        ContactEmail = LoginActivity.currentuser.getEmail();
        int len = ContactEmail.length();
        ContactEmail = ContactEmail.substring( 0, len-4) + ".com";

        InputProductImage.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        } );

        AddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductDetails();
            }
        });
    }

    private void ValidateProductDetails() {
        ProductName = InputProductName.getText().toString();
        ProductDescription = InputProductDescription.getText().toString();
        ProductPrice = InputProductPrice.getText().toString();
        if(ImageUri == null)
        {
            Toast.makeText(this, "Please add Image.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProductName))
        {
            Toast.makeText(this, "Please add Name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProductDescription))
        {
            Toast.makeText(this, "Please add Description.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProductPrice))
        {
            Toast.makeText(this, "Please add Price.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingBar.setTitle("Storing Information");
        loadingBar.setMessage("Please wait.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ProductRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + ProductRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                String message = e.toString();
                Toast.makeText(AdminNewProductActivity.this, "Error" + e, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminNewProductActivity.this, "Product Image uploaded successfully.", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(AdminNewProductActivity.this, "Got image url successfully" , Toast.LENGTH_LONG).show();
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        final HashMap<String, Object > productMap = new HashMap<>();
        productMap.put("pid", ProductRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", ProductName);
        productMap.put("description", ProductDescription);
        productMap.put("price", ProductPrice);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("contact_email", ContactEmail);
        productMap.put("contact_address", ContactAddress);
        productMap.put("contact_name", ContactName);
        ProductRef.child(LoginActivity.currentuser.getEmail()).child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    ProductRef.child(CategoryName).child(ProductRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(AdminNewProductActivity.this, "Information stored successfully." , Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AdminNewProductActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                String error = task.getException().toString();
                                Toast.makeText(AdminNewProductActivity.this, "Error : " + error , Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
//                    loadingBar.dismiss();
                    String error = task.getException().toString();
                    Toast.makeText(AdminNewProductActivity.this, "Error : " + error , Toast.LENGTH_LONG).show();
                }
            }
        } );

        /*ProductRef.child(LoginActivity.currentuser.getEmail()).push().setValue(productMap);
        ProductRef.child(CategoryName).push().setValue(productMap);
        loadingBar.dismiss();
        Toast.makeText(AdminNewProductActivity.this, "Information stored successfully." , Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AdminNewProductActivity.this, AdminActivity.class);
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
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
