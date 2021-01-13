package com.crazydevstuff.infoshare.Fragments;

import android.app.Application;
import android.content.Context;
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

import com.crazydevstuff.infoshare.Adapters.HomeProductsAdapter;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.crazydevstuff.infoshare.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private HomeProductsAdapter productsAdapter;
    private ProductViewModel productViewModel;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);

        productsRecyclerView =(RecyclerView) v.findViewById(R.id.productsRV);

        String des = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat";
        String item="https://www.imindq.com/Portals/0/EasyDNNNews/273/950600p488EDNmainimg-book-mind-mapping.jpg";
        String seller="https://srmrc.nihr.ac.uk/wp-content/uploads/female-placeholder.jpg";
        ProductModel p1 = new ProductModel("Books",des,item,"User_1",2000,seller);
        ProductModel p2 = new ProductModel("Books",des,item,"User_2",5000,seller);

        productsAdapter = new HomeProductsAdapter();
        productViewModel=new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ProductViewModel.class);
        productViewModel.deleteAllCache();
        productViewModel.addCache(p1);
        productViewModel.addCache(p2);
        productViewModel.addCache(p1);
        productViewModel.addCache(p2);
        productViewModel.getAllCachedProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                productsAdapter.setProductModelList(productModels);
            }
        });
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productsRecyclerView.setHasFixedSize(true);
        productsRecyclerView.setAdapter(productsAdapter);
        return v;
    }



}