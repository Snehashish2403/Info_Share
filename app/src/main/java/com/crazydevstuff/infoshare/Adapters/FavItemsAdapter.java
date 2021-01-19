package com.crazydevstuff.infoshare.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavItemsAdapter extends RecyclerView.Adapter<FavItemsAdapter.FavViewHolder> {
    private List<ProductModel> favItemsList = new ArrayList<>();
    private ItemsAdapterActionListener mListener;

    public void setFavItemsList(List<ProductModel> favItemsList) {
        this.favItemsList = favItemsList;
        notifyDataSetChanged();
    }

    public FavItemsAdapter(ItemsAdapterActionListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_fragment_item_layout,parent,false);
        final FavViewHolder mholder = new FavViewHolder(view);
        return mholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavViewHolder holder, final int position) {
        holder.sellerName.setText(favItemsList.get(position).getSellerName());
        holder.price.setText( "₹" + favItemsList.get(position).getProductPrice());
        holder.productDesc.setText(favItemsList.get(position).getProductDescription());
        holder.productName.setText(favItemsList.get(position).getProductName());


        Query picQuery = FirebaseDatabase.getInstance().getReference("users-list").orderByChild("email").equalTo(favItemsList.get(position).getSellerEmail());
        picQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                Picasso.get().load(dataSnapshot.getValue(RegisterModel.class).getProf_pic()).fit().placeholder(R.drawable.placeholder).centerInside().into(holder.sellerImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Picasso.get().load(favItemsList.get(position).getProductImage()).fit().centerInside().into(holder.item);


        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onViewClicked(v.getId(),position,favItemsList.get(position).getItemKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favItemsList.size();
    }

    static class FavViewHolder extends RecyclerView.ViewHolder{

        ImageView sellerImage;
        TextView sellerName;
        ImageView item;
        TextView productName;
        TextView productDesc;
        TextView price;
        ImageView deleteItem;


        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerImage = itemView.findViewById(R.id.fav_sellerIV);
            sellerName = itemView.findViewById(R.id.fav_sellerNameTV);
            item = itemView.findViewById(R.id.fav_imageIV);
            productName = itemView.findViewById(R.id.fav_nameTV);
            productDesc = itemView.findViewById(R.id.fav_descrpTv);
            price = itemView.findViewById(R.id.fav_priceTV);
            deleteItem = itemView.findViewById(R.id.fav_deleteItemIV);
        }
    }
}
