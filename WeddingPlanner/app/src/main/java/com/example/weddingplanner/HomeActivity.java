package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        String fragment = getIntent().getStringExtra("fragment");
        Spinner spinner = (Spinner) findViewById(R.id.dropdown_menu_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_array, R.layout.color_spinner_layout);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        BottomNavigationView bottomNav = findViewById( R.id.bottom_navigation );
        bottomNav.setOnNavigationItemSelectedListener( navListener );
        if(fragment.equals("home"))
        {
            LoginActivity.current_in_home = "home";
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new HomeFragment()).commit();
        }
        if(fragment.equals("checklist"))
        {
            LoginActivity.current_in_home = "checklist";
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new ChecklistFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch(item.getItemId())
                    {
                        case R.id.navigation_home:
                            LoginActivity.current_in_home = "home";
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_checklist:
                            LoginActivity.current_in_home = "checklist";
                            selectedFragment = new ChecklistFragment();
                            break;
                        case R.id.navigation_blogs:
                            LoginActivity.current_in_home = "blog";
                            selectedFragment = new BlogsFragment();
                            break;
                        case R.id.navigation_fav:
                            LoginActivity.current_in_home = "fav";
                            selectedFragment = new FavFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        String selected_item = parent.getSelectedItem().toString();
        switch (selected_item) {
            case "Main":
                Fragment selectedFragment = null;
                switch (LoginActivity.current_in_home) {
                    case "home":
                        selectedFragment = new HomeFragment();
                        break;
                    case "checklist":
                        selectedFragment = new ChecklistFragment();
                        break;
                    case "blog":
                        selectedFragment = new BlogsFragment();
                        break;
                    case "fav":
                        selectedFragment = new FavFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, selectedFragment).commit();
                break;
            case "Settings":
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new SettingsFragment() ).commit();
                break;
            case "Logout":
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    protected void onStart()
//    {
//
//        super.onStart();
//        FirebaseRecyclerOptions<Products> options =
//                new FirebaseRecyclerOptions.Builder<Products>()
//                        .setQuery(ProductsRef, Products.class)
//                        .build();
//
//
//        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
//                    {
//                        holder.txtProductName.setText(model.getPname());
//                        holder.txtProductDescription.setText(model.getDescription());
//                        holder.txtProductPrice.setText("Price = " + model.getPrice());
//                        Picasso.get().load(model.getImage()).into(holder.imageView);
//                    }
//
//                    @NonNull
//                    @Override
//                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//                    {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
//                        ProductViewHolder holder = new ProductViewHolder(view);
//                        return holder;
//                    }
//                };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
}
