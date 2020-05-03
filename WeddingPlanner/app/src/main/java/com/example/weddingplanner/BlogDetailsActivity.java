package com.example.weddingplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weddingplanner.Models.Blog;
import com.example.weddingplanner.Models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogDetailsActivity extends AppCompatActivity {
    private ImageView blogImage;
    private TextView blogDescription, blogTitle, blog_contact_name, blog_contact_email, blog_contact_address;
    private String blogID = "", state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_blog_details );
        blogID = getIntent().getStringExtra("pid");
        blogImage = (ImageView) findViewById(R.id.blog_image_details);
        blogTitle = (TextView) findViewById(R.id.blog_title_details);
        blogDescription = (TextView) findViewById(R.id.blog_description_details);
        blog_contact_name = findViewById(R.id.blog_contact_name);
        blog_contact_address = findViewById(R.id.blog_contact_address);
        blog_contact_email = findViewById(R.id.blog_contact_email);
        getBlogDetails(blogID);
    }

    private void getBlogDetails(String BlogID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Blog");

        productsRef.child(BlogID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Blog blog_check = dataSnapshot.getValue(Blog.class);
                    assert blog_check != null;
                    Picasso.get().load(blog_check.getImage()).into(blogImage);
                    blogTitle.setText(blog_check.getTitle());
                    blogDescription.setText(blog_check.getDescription());
                    blog_contact_name.setText("Contact name : " + blog_check.getContact_name());
                    blog_contact_address.setText("Location : " + blog_check.getContact_address());
                    String temp = blog_check.getContact_email();
                    if(temp != null)
                    {
                        int len = temp.length();
                        temp = temp.substring(0, len-4) + ".com";
                        blog_contact_email.setText( "To Contact : " + temp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
