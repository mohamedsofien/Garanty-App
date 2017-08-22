package com.example.fossien.Garanty.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fossien.Garanty.R;

/**
 * Created by MacBook on 22/07/17.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {

    public TextView productText;
    public ImageView productImage;


    public CategoryHolder(View view) {
        super(view);

        productImage = (ImageView) view.findViewById(R.id.image_category);
        productText = (TextView) view.findViewById(R.id.name_category);
    }
}