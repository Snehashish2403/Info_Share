package com.crazydevstuff.infoshare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeProductsAdapter extends RecyclerView.Adapter<HomeProductsAdapter.ViewHolder> {
    private List<ProductModel> productModelList=new ArrayList<>();
    private ItemsAdapterActionListener itemsAdapterActionListener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_product_layout,parent,false);
        final ViewHolder mHolder = new ViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.price.setText("₹"+productModelList.get(position).getProductPrice().toString());
        holder.productDesc.setText(productModelList.get(position).getProductDescription());
        holder.productName.setText(productModelList.get(position).getProductName());
        holder.sellerName.setText(productModelList.get(position).getSellerName());
        Query query=FirebaseDatabase.getInstance().getReference("users-list").orderByChild("email").equalTo(productModelList.get(position).getSellerEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Picasso.get().load(dataSnapshot.getValue(RegisterModel.class).getProf_pic())
                            .fit()
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .into(holder.sellerImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsAdapterActionListener.onViewClicked(v.getId(),position);
            }
        });
        Picasso.get().load(productModelList.get(position).getProductImage())
                .fit()
                .centerInside()
                .into(holder.item);    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void setProductModelList(List<ProductModel> list){
        this.productModelList=list;
        notifyDataSetChanged();
    }

    public void setFavItemsListener(ItemsAdapterActionListener itemsAdapterActionListener){
        this.itemsAdapterActionListener = itemsAdapterActionListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sellerImage;
        TextView sellerName;
        ImageView item;
        TextView productName;
        TextView productDesc;
        TextView price;
        ImageView addToFav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerImage = itemView.findViewById(R.id.prod_sellerIV);
            sellerName = itemView.findViewById(R.id.prod_sellerNameTV);
            item = itemView.findViewById(R.id.prod_imageIV);
            productName = itemView.findViewById(R.id.prod_nameTV);
            productDesc = itemView.findViewById(R.id.prod_descrpTv);
            price = itemView.findViewById(R.id.prod_priceTV);
            addToFav = itemView.findViewById(R.id.prod_addToFavIV);

        }
    }
}
