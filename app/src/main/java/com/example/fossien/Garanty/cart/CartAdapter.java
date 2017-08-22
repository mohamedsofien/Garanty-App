package com.example.fossien.Garanty.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fossien.Garanty.DatabaseHandler;
import com.example.fossien.Garanty.ItemDetailActivity;
import com.example.fossien.Garanty.R;
import com.example.fossien.Garanty.data.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fossien on 19/07/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Product> mDataset;

    public Activity mActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CartAdapter(Activity mAct, List<Product> myDataset) {
        mActivity = mAct;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.cart_layout_model, parent, false);
        //TextView v = view.findViewById(R.id.myText);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset.get(position));
        ImageView productImg = holder.mView.findViewById(R.id.roundImage);
        final TextView productName = holder.mView.findViewById(R.id.nameTxt);
        TextView productId = holder.mView.findViewById(R.id.idTxt);
        TextView productPrice = holder.mView.findViewById(R.id.priceTxt);
        FloatingActionButton cartAddBtn =  holder.mView.findViewById(R.id.cartAddBtn);

        final Product mProduct = mDataset.get(position);

        cartAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(mActivity);
                db.deleteProduct(mProduct.getName());
                mDataset = db.getAllproducts();
                notifyDataSetChanged();
            }
        });

        if (mProduct != null) {
            productId.setText("this product's ID is: " + mProduct.getMarque());
            productName.setText("this is a " + mProduct.getName());
            productPrice.setText(mProduct.getPrice() + "$");
            Picasso.with(mActivity).load(mProduct.getImage()).into(productImg);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ItemDetailActivity.class);
                intent.putExtra("PRODUCT", mProduct);
                mActivity.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}