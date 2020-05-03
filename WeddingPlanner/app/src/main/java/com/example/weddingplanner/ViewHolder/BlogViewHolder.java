package com.example.weddingplanner.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Interface.ItemClickListner;
import com.example.weddingplanner.R;

public class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtBlogTitle, txtBlogDescription, txtOwnerName, txtOwnerContact;
    public ImageView imageView;
    public ItemClickListner listner;


    public BlogViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById( R.id.blog_image);
        txtBlogTitle = (TextView) itemView.findViewById(R.id.blog_title);
        txtBlogDescription = (TextView) itemView.findViewById(R.id.blog_description);
        txtOwnerContact = (TextView) itemView.findViewById(R.id.owner_contact);
        txtOwnerName = (TextView) itemView.findViewById(R.id.owner_name);

    }

    public void setItemClickListner(ItemClickListner listnerBlog)
    {
        this.listner = listnerBlog;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
