package com.crazydevstuff.infoshare.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.crazydevstuff.infoshare.Adapters.HomeProductsAdapter;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private HomeProductsAdapter productsAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home,container,false);
        String des = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat";

        ProductModel p1 = new ProductModel();
        p1.setProductDescription(des);
        p1.setProductImage(BitmapFactory.decodeResource(getResources(),R.raw.books));
        p1.setProductName("Books");
        p1.setProductPrice(2000);
        p1.setSellerImage(BitmapFactory.decodeResource(getResources(),R.drawable.sellerpic));
        p1.setSellerName("User_1");

        List<ProductModel> productModelList = new ArrayList<>();
        productModelList.add(p1);
        productModelList.add(p1);
        productModelList.add(p1);
        productModelList.add(p1);
        productModelList.add(p1);
        productModelList.add(p1);
        // Inflate the layout for this fragment
        productsRecyclerView =(RecyclerView) v.findViewById(R.id.productsRV);
        productsAdapter = new HomeProductsAdapter(productModelList,getContext());
        productsRecyclerView.setAdapter(productsAdapter);

        return v;
    }



}