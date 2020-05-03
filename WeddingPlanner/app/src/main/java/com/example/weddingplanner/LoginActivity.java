package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.weddingplanner.Models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email, pass;
    private Button login;
    private ToggleButton toggleButton;
    private String parentDbname = "users";
    private ProgressDialog loadingBar;
    public static Users currentuser;
    public static String current_in_home = "";
    public static String user_type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.pass_login);
        login = findViewById(R.id.login_login);
        toggleButton = findViewById(R.id.toggle_login);
        loadingBar = new ProgressDialog(this);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    parentDbname = "admins";
                }
                else
                {
                    parentDbname = "users";
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_user = email.getText().toString();
                String pass_user = pass.getText().toString();
                verify(email_user,pass_user);
            }
        });
    }

    private void verify(String email_user, String pass_user) {
        if(email_user.isEmpty())
        {
            Toast.makeText(this, "Please enter your email-id.", Toast.LENGTH_SHORT).show();
        }
        else if(pass_user.isEmpty())
        {
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
        }
        else if(email_user.length() <= 5)
        {
            Toast.makeText(this, "Incorrect email-id.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            int len = email_user.length();
            email_user = email_user.substring(0,len-4) + ",com";
            check_details(email_user, pass_user);
        }
    }

    private void check_details(final String email_user, final String pass_user)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbname).child(email_user).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbname).child(email_user).getValue(Users.class);
                    if(!usersData.getEmail().equals(email_user))
                    {
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Your account has been blocked by the admin.", Toast.LENGTH_SHORT).show();
                    }
                    else if(!usersData.getPassword().equals(pass_user))
                    {
                        Toast.makeText(LoginActivity.this, "Invalid Password. Please try again.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        if(parentDbname.equals("users"))
                        {
                            user_type = "users";
                            loadingBar.dismiss();
                            currentuser = usersData;
                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                            Intent intent = new Intent(LoginActivity.this, CategoryProducts.class);
                            intent.putExtra("fragment", "home");
                            startActivity(intent);
                        }
                        else if(parentDbname.equals("admins"))
                        {
                            user_type = "admins";
                            loadingBar.dismiss();
                            currentuser = usersData;
                            Toast.makeText(LoginActivity.this, "Login Successful as admin.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                        }

                    }
                }
                else if(parentDbname.equals("users"))
                {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Account with this " + email_user + " number does not exist. Please register first.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "No such admin exists " + email_user, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
