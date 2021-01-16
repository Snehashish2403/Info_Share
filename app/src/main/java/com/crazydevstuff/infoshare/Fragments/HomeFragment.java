package com.crazydevstuff.infoshare.Fragments;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crazydevstuff.infoshare.Activities.MainActivity;
import com.crazydevstuff.infoshare.Activities.MakeProduct;
import com.crazydevstuff.infoshare.Adapters.HomeProductsAdapter;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.crazydevstuff.infoshare.ViewModel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private HomeProductsAdapter productsAdapter;
    private ProductViewModel productViewModel;
    private FloatingActionButton fab;
    private DatabaseReference databaseReference;
    private ValueEventListener databaseListener;
    private List<ProductModel> fetchedProds=new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);

        productsRecyclerView =(RecyclerView) v.findViewById(R.id.productsRV);
        fab = v.findViewById(R.id.fab);
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");
        productsAdapter = new HomeProductsAdapter();
        productViewModel=new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ProductViewModel.class);
        databaseListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productViewModel.deleteAllCache();
                fetchedProds.clear();
                for(DataSnapshot postSnapShot:snapshot.getChildren()){
                    ProductModel product=postSnapShot.getValue(ProductModel.class);
                    fetchedProds.add(product);
                }
                for(ProductModel temp: fetchedProds){
                    productViewModel.addCache(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productViewModel.getAllCachedProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {

                productsAdapter.setProductModelList(productModels);
            }
        });
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(productsAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=MainActivity.username;
                Intent intent=new Intent(getContext(),MakeProduct.class);
                intent.putExtra("username",username);
                startActivity(intent);

            }
        });
        return v;
    }



}