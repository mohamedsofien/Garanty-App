package com.example.fossien.Garanty.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fossien.Garanty.data.Product;
import com.example.fossien.Garanty.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MacBook on 19/07/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public CategoryAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolder((CategoryHolder) holder, position);
    }

    private void bindViewHolder(CategoryHolder holder, int position) {
            Product product = productList.get(position);
            holder.productText.setText(product.getName());
            Picasso.with(context).load(product.getImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
