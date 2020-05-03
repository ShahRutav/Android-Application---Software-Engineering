package com.example.weddingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private ImageView catering_button;
    private ImageView venues_button;
    private ImageView photography_button;
    private ImageView jewellery_button;
    private ImageView transportation_button;
    private ImageView cards_button, bridal_button, groom_button;

    private ImageView catering_image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        catering_button = findViewById( R.id.imageView_catering );
        venues_button = findViewById(R.id.imageView_venues);
        photography_button = findViewById(R.id.imageView_photography);
        jewellery_button = findViewById(R.id.imageView_jewellery);
        transportation_button = findViewById(R.id.imageView_transportation);
        cards_button = findViewById(R.id.imageView_cards);
        bridal_button = findViewById(R.id.imageView_bridal);
        groom_button = findViewById(R.id.imageView_groom);
        TextView admin_cat_title = findViewById(R.id.admin_category_title);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_admin_onlyhome_addproduct);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        catering_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Catering");
                startActivity(intent);
            }
        } );

        venues_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Venues");
                startActivity(intent);
            }
        } );
        photography_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Photography");
                startActivity(intent);
            }
        } );
        jewellery_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Jewellery");
                startActivity(intent);
            }
        } );
        transportation_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Transportation");
                startActivity(intent);
            }
        } );
        cards_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Wedding Cards");
                startActivity(intent);
            }
        } );
        bridal_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Bridal Accessories");
                startActivity(intent);
            }
        } );
        groom_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminNewProductActivity.class);
                intent.putExtra("category", "Groom Accessories");
                startActivity(intent);
            }
        } );
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId())
                    {
                        case R.id.navigation_home_admin_onlyhome:
                            Intent intent = new Intent(AdminActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                            break;
                    }
                    return true;
                }
            };
}
