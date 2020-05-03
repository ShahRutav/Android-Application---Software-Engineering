package com.example.weddingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {
    private ImageView catering_button;
    private ImageView venues_button;
    private ImageView photography_button;
    private ImageView jewellery_button;
    private ImageView transportation_button;
    private ImageView cards_button, bridal_button, groom_button;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_home, container, false );
        catering_button = view.findViewById( R.id.imageView_catering );
        venues_button = view.findViewById(R.id.imageView_venues);
        photography_button = view.findViewById(R.id.imageView_photography);
        jewellery_button = view.findViewById(R.id.imageView_jewellery);
        transportation_button = view.findViewById(R.id.imageView_transportation);
        cards_button = view.findViewById(R.id.imageView_cards);
        bridal_button = view.findViewById(R.id.imageView_bridal);
        groom_button = view.findViewById(R.id.imageView_groom);
        catering_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Catering");
                startActivity(intent);
            }
        } );

        venues_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Venues");
                startActivity(intent);
            }
        } );
        photography_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Photography");
                startActivity(intent);
            }
        } );
        jewellery_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Jewellery");
                startActivity(intent);
            }
        } );
        transportation_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Transportation");
                startActivity(intent);
            }
        } );
        cards_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Wedding Cards");
                startActivity(intent);
            }
        } );
        bridal_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Bridal Accessories");
                startActivity(intent);
            }
        } );
        groom_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryProducts.class);
                intent.putExtra("category", "Groom Accessories");
                startActivity(intent);
            }
        } );


        return view;
    }
}


