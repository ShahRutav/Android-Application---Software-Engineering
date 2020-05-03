package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weddingplanner.Models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productPrice, productDescription, productName, productCategory, product_contact_name, product_contact_email, product_contact_address;
    private String productID = "", state = "Normal";
    private String productCat = "";
    private CheckBox favButton;
    private DatabaseReference favDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_product_details );
        productID = getIntent().getStringExtra("pid");
        productCat = getIntent().getStringExtra("category");
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productCategory = findViewById( R.id.product_category_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        product_contact_name = findViewById(R.id.product_contact_name);
        product_contact_address = findViewById(R.id.product_contact_address);
        product_contact_email = findViewById(R.id.product_contact_email);
        favButton = findViewById(R.id.fav_button);
        favDatabase = FirebaseDatabase.getInstance().getReference().child("favourites").child(LoginActivity.currentuser.getEmail());
        favDatabase.child(productID).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Products prod = dataSnapshot.getValue(Products.class);
                    favButton.setChecked(true);
                }
                else
                {
                    favButton.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


        favButton.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productCat);
                    productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists())
                            {
                                Products products = dataSnapshot.getValue(Products.class);
                                assert products != null;
                                favDatabase.child(productID).setValue(products);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    favDatabase.child(productID).removeValue();
                }
            }
        } );
        getProductDetails(productID);


    }

    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productCat);

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
                    assert products != null;
                    Picasso.get().load(products.getImage()).into(productImage);
                    productName.setText(products.getName());
                    productCategory.setText("Category : " + products.getCategory());
                    productPrice.setText("Price Range ~ " + products.getPrice());
                    productDescription.setText(products.getDescription());
                    product_contact_name.setText("Contact name : " + products.getContact_name());
                    product_contact_address.setText("Location : " + products.getContact_address());
                    String temp = products.getContact_email();
                    if(temp != null)
                    {
                        int len = temp.length();
                        temp = temp.substring(0, len-4) + ".com";
                        product_contact_email.setText( "To Contact : " + temp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
