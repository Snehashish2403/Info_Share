
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

import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecentItemsAdapter extends FirebaseRecyclerAdapter<ProductModel,RecentItemsAdapter.Holder> {
    private ItemsAdapterActionListener actionListener;
    private FirebaseStorage storage;
    private DatabaseReference databaseReference;
    private AlertDialog.Builder builder;
    private Context context;
    private FirebaseAuth auth;
    public RecentItemsAdapter(@NonNull FirebaseRecyclerOptions<ProductModel> options, ItemsAdapterActionListener actionListener,Context context) {
        super(options);
        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        storage=FirebaseStorage.getInstance();
        this.actionListener = actionListener;
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final Holder holder, final int position, @NonNull final ProductModel model) {
        holder.price.setText("₹"+model.getProductPrice().toString());
        holder.productDesc.setText(model.getProductDescription());
        holder.productName.setText(model.getProductName());
        holder.sellerName.setText(model.getSellerName());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClicked(v.getId(),position,model.getItemKey());
                if(position!=RecyclerView.NO_POSITION&&position<=getItemCount()-1){
                    showAlert(getSnapshots().getSnapshot(position).getValue(ProductModel.class));
                }
            }
        });
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users-list").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String url =snapshot.child("prof_pic").getValue(String.class);
                Picasso.get().load(url)
                        .fit()
                        .centerInside()
                        .placeholder(R.drawable.placeholder)
                        .into(holder.sellerImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                        final String key=productModel.getItemKey();
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
