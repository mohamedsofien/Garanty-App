package com.example.fossien.Garanty.brand;

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

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public BrandAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new BrandHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindViewHolder((BrandHolder) holder, position);
    }

    private void bindViewHolder(BrandHolder holder, int position) {
            Product product = productList.get(position);
            holder.productText.setText(product.getName());
            holder.descriptionText.setText(product.getMarque());
            String price = product.getPrice() + "$";
            holder.priceText.setText(price);
            Picasso.with(context).load(product.getImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
