package com.crazydevstuff.infoshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;

import java.util.List;

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.ViewHolder> {
    private List<ProductModel> productModelList;
    private Context context;

    public HomeProductsAdapter(List<ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.price.setText(productModelList.get(position).getProductPrice().toString());
        holder.productDesc.setText(productModelList.get(position).getProductDescription());
        holder.productName.setText(productModelList.get(position).getProductName());
        holder.sellerName.setText(productModelList.get(position).getSellerName());
        holder.sellerImage.setImageBitmap(productModelList.get(position).getSellerImage());
        holder.item.setImageBitmap(productModelList.get(position).getProductImage());
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sellerImage;
        TextView sellerName;
        ImageView item;
        TextView productName;
        TextView productDesc;
        TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerImage = itemView.findViewById(R.id.prod_sellerIV);
            sellerName = itemView.findViewById(R.id.prod_sellerNameTV);
            item = itemView.findViewById(R.id.prod_imageIV);
            productName = itemView.findViewById(R.id.prod_nameTV);
            productDesc = itemView.findViewById(R.id.prod_descrpTv);
            price = itemView.findViewById(R.id.prod_priceTV);
        }
    }
}