package com.example.gerard.socialapp.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gerard.socialapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView photo;
    public TextView author;
    public TextView content;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        photo = itemView.findViewById(R.id.comm_photo);
        author = itemView.findViewById(R.id.comm_author);
        content = itemView.findViewById(R.id.comm_content);

    }
}
