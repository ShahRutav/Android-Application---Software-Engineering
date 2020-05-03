package com.example.weddingplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Models.Blog;
import com.example.weddingplanner.Models.Products;
import com.example.weddingplanner.ViewHolder.BlogViewHolder;
import com.example.weddingplanner.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BlogsFragment extends Fragment {
    private DatabaseReference BlogRef;
    private RecyclerView RecyclerViewHome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_blogs, container, false );

        RecyclerViewHome = (RecyclerView)view.findViewById(R.id.recycler_menu_blog);
        BlogRef = FirebaseDatabase.getInstance().getReference().child("Blog");
        RecyclerViewHome.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewHome.setLayoutManager(layoutManager);

        final FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(BlogRef, Blog.class)
                        .build();


        final FirebaseRecyclerAdapter<Blog, BlogViewHolder> adapter =
                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BlogViewHolder holder, int position, @NonNull final Blog model)
                    {
                        holder.txtBlogTitle.setText(model.getTitle());
                        holder.txtBlogDescription.setText(model.getDescription());
                        holder.txtOwnerContact.setText(model.getContact_email());
                        holder.txtOwnerName.setText(model.getContact_name());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                Intent intent = new Intent(getActivity(), BlogDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_items_layout, parent, false);
                        BlogViewHolder holder = new BlogViewHolder(view);
                        return holder;
                    }
                };
        RecyclerViewHome.setAdapter(adapter);
        adapter.startListening();
        return view;
    }

}
