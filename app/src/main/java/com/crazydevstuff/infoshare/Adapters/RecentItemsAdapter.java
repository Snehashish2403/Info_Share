package com.crazydevstuff.infoshare.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class RecentItemsAdapter extends FirebaseRecyclerAdapter<ProductModel,RecentItemsAdapter.Holder> {

    public RecentItemsAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull ProductModel model) {
        holder.price.setText(model.getProductPrice().toString());
        holder.productDesc.setText(model.getProductDescription());
        holder.productName.setText(model.getProductName());
        holder.sellerName.setText(model.getSellerName());
        Picasso.get().load(model.getSellerImage())
                .fit()
                .centerInside()
                .into(holder.sellerImage);
        Picasso.get().load(model.getProductImage())
                .fit()
                .centerInside()
                .into(holder.item);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_items_layout,parent,false);
        return new Holder(view);
    }

    static class Holder extends RecyclerView.ViewHolder{
        ImageView sellerImage;
        TextView sellerName;
        ImageView item;
        TextView productName;
        TextView productDesc;
        TextView price;
        ImageView deleteItem;
        public Holder (@NonNull View itemView) {
            super(itemView);
            sellerImage = itemView.findViewById(R.id.yrItems_sellerImageIV);
            sellerName = itemView.findViewById(R.id.yrItems_sellerNameTV);
            item = itemView.findViewById(R.id.yrItems_itemImageIV);
            productName = itemView.findViewById(R.id.yrItems_itemNameTV);
            productDesc = itemView.findViewById(R.id.yrItems_itemDescTV);
            price = itemView.findViewById(R.id.yrItems_itemPriceTV);
            deleteItem = itemView.findViewById(R.id.delete_yourItemIV);
    }
}}
