package com.example.weddingplanner.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weddingplanner.Interface.ItemClickListner;
import com.example.weddingplanner.R;

public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView description;
    public CheckBox stat;
    public ItemClickListner listner;
    public LinearLayout ll_checklist;


    public ChecklistViewHolder(View itemView)
    {
        super(itemView);
        description = itemView.findViewById(R.id.task_checklist);
        stat = itemView.findViewById(R.id.status_button);

    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
