package com.crazydevstuff.infoshare.Adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazydevstuff.infoshare.Interfaces.RecentItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecentItemsAdapter extends FirebaseRecyclerAdapter<ProductModel,RecentItemsAdapter.Holder> {
    private RecentItemsAdapterActionListener actionListener;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private AlertDialog.Builder builder;
    private Context context;
    public RecentItemsAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options, RecentItemsAdapterActionListener actionListener,Context context) {
        super(options);
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        storage=FirebaseStorage.getInstance();
        this.actionListener = actionListener;
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, final int position, @NonNull ProductModel model) {
        holder.price.setText("₹"+model.getProductPrice().toString());
        holder.productDesc.setText(model.getProductDescription());
        holder.productName.setText(model.getProductName());
        holder.sellerName.setText(model.getSellerName());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClicked(v.getId(),position);
                if(position!=RecyclerView.NO_POSITION&&position<=getItemCount()-1){
                    showAlert(getSnapshots().getSnapshot(position).getValue(ProductModel.class));
                }
            }
        });


        Picasso.get().load(model.getSellerImage())
                .fit()
                .centerInside()
                .into(holder.sellerImage);
        Picasso.get().load(model.getProductImage())
                .fit()
                .centerInside()
                .into(holder.item);
    }

    private void showAlert(final ProductModel productModel){
        builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete the post ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StorageReference imageRef=storage.getReferenceFromUrl(productModel.getProductImage());
                        final String key=productModel.getKey();
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databaseReference.child(key).removeValue();
                                Toast.makeText(context,"Successfully deleted the product!",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Failed To delete!",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(context,"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete the post?");
        alert.show();
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
