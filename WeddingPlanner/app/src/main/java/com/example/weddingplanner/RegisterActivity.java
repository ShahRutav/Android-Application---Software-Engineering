package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class  RegisterActivity extends AppCompatActivity {

    List<AuthUI.IdpConfig> providers;
    private static final int MY_REQUEST_CODE = 7117;
    private FirebaseUser user;
    private EditText name, email, password, address;
    private Button register_button;
    private ToggleButton toggleButton;
    private int ALL_FILLED=0, ALL_GOOD = 0;
    private String parentDbname = "users";
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        name = findViewById(R.id.name_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        address = findViewById(R.id.address_register);
        register_button = findViewById(R.id.register_register);
        toggleButton = findViewById(R.id.toggle_register);
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
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }



    private void verify() {
        String name_user = name.getText().toString();
        String pass_user = password.getText().toString();
        String email_user = password.getText().toString();
        String address_user = address.getText().toString();
        check_fields(name_user, email_user, pass_user, address_user);
        if(ALL_FILLED == 1)
        {
            showsSignInOptions();
        }
    }

    private void add_data_to_firebase(final String name_user, final String pass_user, final String email_user, final String address_user)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child(parentDbname).child(email_user).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("email", email_user);
                    userDataMap.put("password", pass_user);
                    userDataMap.put("name", name_user);
                    userDataMap.put("address", address_user);
                    RootRef.child(parentDbname).child(email_user).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "This " + email_user + " already exists", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Please try again using email.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingBar.dismiss();
            }
        });
    }


    private void check_fields(String name_user, String email_user, String pass_user, String address_user) {
        if(name_user.isEmpty() || email_user.isEmpty() || pass_user.isEmpty() || address_user.isEmpty())
        {
            Toast.makeText(this, "Please fill all the details.", Toast.LENGTH_LONG).show();
        }
        else
        {
            ALL_FILLED = 1;
        }
    }

    private void showsSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().
                        setIsSmartLockEnabled(false).setAvailableProviders(providers).setTheme(R.style.MyTheme).build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            //get user
            user = FirebaseAuth.getInstance().getCurrentUser();
            String email_user = email.getText().toString();
            String name_user = name.getText().toString();
            String pass_user = password.getText().toString();
            String address_user = address.getText().toString();
            //user.getEmail() gives us the email.
            if(email_user.equals(user.getEmail()))
            {
                ALL_GOOD = 1;
                Toast.makeText(RegisterActivity.this, "Mail id : " + user.getEmail() + " Name : " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                loadingBar.setTitle("Creating Account");
                loadingBar.setMessage("Please wait.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                int len = email_user.length();
                email_user = email_user.substring(0,len-4) + ",com";
                add_data_to_firebase(name_user, pass_user, email_user, address_user);
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "We couldn't verify your email-id, please try again.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "Error " , Toast.LENGTH_SHORT).show();
        }
    }
}
