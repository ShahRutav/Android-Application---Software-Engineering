package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static int admin_main_back_story = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_main );
        Spinner spinner = (Spinner) findViewById(R.id.dropdown_menu_spinner_admin);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_array_admin, R.layout.color_spinner_layout_admin);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        admin_main_back_story = 1;

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_admin);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId())
                    {
                        case R.id.navigation_home_admin:
                            Intent intent = new Intent(AdminMainActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.navigation_blogs_admin:
                            Intent intent2 = new Intent(AdminMainActivity.this, AdminBlogActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.navigation_addproduct_admin:
                            Intent intent3 = new Intent(AdminMainActivity.this, AdminActivity.class);
                            startActivity(intent3);
                            break;
                    }
                    return true;
                }
            };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected_item = parent.getSelectedItem().toString();
        switch (selected_item) {
            case "Main":
                if(admin_main_back_story == 1) {
                    break;
                }
                admin_main_back_story = 1;
                Intent intent = new Intent( AdminMainActivity.this, AdminMainActivity.class );
                startActivity( intent );
                break;
            case "Your Services" :
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new MyProductsFragment() ).commit();
                admin_main_back_story = 0;
                break;
            case "Settings":
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new SettingsFragment() ).commit();
                admin_main_back_story = 0;
                break;
            case "Logout":
                intent = new Intent( AdminMainActivity.this, MainActivity.class );
                startActivity( intent );
                admin_main_back_story = 0;
                break;
            case "Your Blogs":
                getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container, new MyBlogFragment() ).commit();
                admin_main_back_story = 0;
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
