package com.crazydevstuff.infoshare.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crazydevstuff.infoshare.Adapters.FavItemsAdapter;
import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private RecyclerView favItemsRV;
    private FavItemsAdapter favItemsAdapter;
    private List<ProductModel> favProductsList =  new ArrayList<>();
    private List<String> favItemsStrings = new ArrayList<>();
    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fav, container, false);

        favItemsRV = view.findViewById(R.id.favFragmentRV);
        mAuth = FirebaseAuth.getInstance();
        fetchFavItemsIds();
        reference = null;
        fetchFavItemsIntoRV();
        favItemsAdapter = new FavItemsAdapter();
        favItemsAdapter.setFavItemsList(favProductsList);
        favItemsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        favItemsRV.setHasFixedSize(true);
        favItemsRV.setAdapter(favItemsAdapter);
        return view;
    }

    private void fetchFavItemsIntoRV() {
        reference = FirebaseDatabase.getInstance().getReference("uploads");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ProductModel model = snapshot.getValue(ProductModel.class);
                    favProductsList.add(model);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void fetchFavItemsIds() {

        reference  = FirebaseDatabase.getInstance().getReference();
        reference.child("favourite_items").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    favItemsStrings.add(dataSnapshot.getValue(String.class));
                    System.out.println("_____"+dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}