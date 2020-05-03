package com.example.weddingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Models.Blog;
import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.Models.Users;
import com.example.weddingplanner.ViewHolder.BlogViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {
    private TextView emailSettings;
    private EditText nameSettings,newPassSettings,oldPassSettings, addressSettings;
    private Button saveChangesSettings;
    private DatabaseReference mDatabase;
// ...
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_settings, container, false );
        emailSettings = view.findViewById(R.id.email_settings);
        nameSettings = view.findViewById( R.id.name_change_settings );
        newPassSettings = view.findViewById( R.id.new_pass_settings );
        oldPassSettings = view.findViewById( R.id.old_pass_settings );
        addressSettings = view.findViewById( R.id.address_change_settings );
        saveChangesSettings = view.findViewById(R.id.save_changes_settings);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String email = LoginActivity.currentuser.getEmail();
        int len = email.length();
        email = email.substring(0,len-4) + ".com";
        emailSettings.setText(email);
        nameSettings.setHint(LoginActivity.currentuser.getName());
        addressSettings.setHint(LoginActivity.currentuser.getAddress());

        saveChangesSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameSettings.getText().toString();
                String newPass = newPassSettings.getText().toString();
                String oldPass = oldPassSettings.getText().toString();
                final String add = addressSettings.getText().toString();
                DatabaseReference md = mDatabase.child(LoginActivity.user_type).child(LoginActivity.currentuser.getEmail());
                String pass = LoginActivity.currentuser.getPassword();
                if(!pass.equals(oldPass))
                {
                    Toast.makeText( getActivity(), "Incorrect Password.", Toast.LENGTH_SHORT ).show();
                }
                else
                {
                    if(!name.isEmpty())
                    {
                        md.child("name").setValue(name);
                        LoginActivity.currentuser.setName(name);
                        nameSettings.setHint(LoginActivity.currentuser.getName());

                    }
                    if(!newPass.isEmpty())
                    {
                        md.child("password").setValue(newPass);
                        LoginActivity.currentuser.setPassword(newPass);
                    }
                    if(!add.isEmpty())
                    {
                        md.child("address").setValue(add);
                        LoginActivity.currentuser.setAddress(add);
                        addressSettings.setHint(LoginActivity.currentuser.getAddress());
                    }

                    mDatabase.child("Products").addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren())
                            {
                                final String cat = data.getKey().toString();
                                mDatabase.child( "Products" ).child( cat ).addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            String key = data.getKey().toString();
                                            final DatabaseReference d = mDatabase.child( "Products" ).child( cat ).child( key );
                                            d.addListenerForSingleValueEvent( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Products cl = dataSnapshot.getValue( Products.class );
                                                    String em = cl.getContact_email();
                                                    String to_match = LoginActivity.currentuser.getEmail();
                                                    int len = to_match.length();
                                                    to_match = to_match.substring(0, len-4) + ".com";
                                                    if (to_match.equals(em))
                                                    {
                                                        if(!name.isEmpty())
                                                            d.child("contact_name").setValue(name);
                                                        if(!add.isEmpty())
                                                            d.child("contact_address").setValue(add);
                                                    }



                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );




                    mDatabase.child("Blog").addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren())
                            {
                                final String cat = data.getKey().toString();
                                final DatabaseReference d = mDatabase.child( "Blog" ).child( cat );
                                d.addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Blog cl = dataSnapshot.getValue( Blog.class );
                                        String em = cl.getContact_email();
                                        String to_match = LoginActivity.currentuser.getEmail();
                                        int len = to_match.length();
                                        to_match = to_match.substring(0, len-4) + ".com";
                                        if (to_match.equals(em))
                                        {
                                            if(!name.isEmpty())
                                                d.child("contact_name").setValue(name);
                                            if(!add.isEmpty())
                                                d.child("contact_address").setValue(add);
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );



                    mDatabase.child("favourites").addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren())
                            {
                                final String cat = data.getKey();
                                assert cat != null;
                                mDatabase.child( "favourites" ).child( cat ).addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            String key = data.getKey();
                                            final DatabaseReference df = mDatabase.child( "favourites" ).child( cat ).child( key );
                                            df.addListenerForSingleValueEvent( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Products cl = dataSnapshot.getValue( Products.class );
                                                    assert cl != null;
                                                    String em = cl.getContact_email();
                                                    String to_match = LoginActivity.currentuser.getEmail();
                                                    int len = to_match.length();
                                                    to_match = to_match.substring(0, len-4) + ".com";
                                                    if (to_match.equals(em))
                                                    {
                                                        if(!name.isEmpty())
                                                            df.child("contact_name").setValue(name);
                                                        if(!add.isEmpty())
                                                            df.child("contact_address").setValue(add);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );


                    mDatabase.child("Blog Admin").addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren())
                            {
                                final String cat = data.getKey();
                                assert cat != null;
                                mDatabase.child( "Blog Admin" ).child( cat ).addListenerForSingleValueEvent( new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                            String key = data.getKey();
                                            final DatabaseReference df = mDatabase.child( "Blog Admin" ).child( cat ).child( key );
                                            df.addListenerForSingleValueEvent( new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Blog cl = dataSnapshot.getValue( Blog.class );
                                                    assert cl != null;
                                                    String em = cl.getContact_email();
                                                    String to_match = LoginActivity.currentuser.getEmail();
                                                    int len = to_match.length();
                                                    to_match = to_match.substring(0, len-4) + ".com";
                                                    if (to_match.equals(em))
                                                    {
                                                        if(!name.isEmpty())
                                                            df.child("contact_name").setValue(name);
                                                        if(!add.isEmpty())
                                                            df.child("contact_address").setValue(add);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            } );
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                } );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );



                    Toast.makeText(getActivity(), "Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        } );



        return view;
    }

}
