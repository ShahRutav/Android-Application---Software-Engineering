package com.example.weddingplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryProducts extends AppCompatActivity {
    private DatabaseReference ProductsRef;
    private RecyclerView RecyclerViewHome;
    private String CategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_category_products );
        CategoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(CategoryProducts.this, CategoryName, Toast.LENGTH_SHORT ).show();

        RecyclerViewHome = (RecyclerView)findViewById( R.id.recycler_menu );
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(CategoryName);
        RecyclerViewHome.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CategoryProducts.this);
        RecyclerViewHome.setLayoutManager(layoutManager);



        final FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price Range ~ " + model.getPrice());
                        holder.txtProductAddress.setText("Location : " + model.getContact_address());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                Intent intent = new Intent(CategoryProducts.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("category", model.getCategory());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        RecyclerViewHome.setAdapter(adapter);
        adapter.startListening();
    }
}
