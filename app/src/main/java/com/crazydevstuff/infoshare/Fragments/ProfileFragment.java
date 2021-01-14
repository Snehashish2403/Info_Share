package com.crazydevstuff.infoshare.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazydevstuff.infoshare.Activities.Login;
import com.crazydevstuff.infoshare.Adapters.HomeProductsAdapter;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.crazydevstuff.infoshare.ViewModel.ProductViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private TextView profileNameTV, profileNumberTV, profileEmailTV;
    private ImageView profileImageIV;
    private FirebaseAuth firebaseAuth;
    private Button logOutButton;
    private DatabaseReference reference;
    private RegisterModel model;
    private RecyclerView productsRecyclerView;
    private HomeProductsAdapter productsAdapter;
    private ProductViewModel productViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        profileEmailTV = v.findViewById(R.id.user_profileEmailTV);
        profileImageIV = v.findViewById(R.id.user_profileIV);
        profileNameTV = v.findViewById(R.id.user_profileNameTV);
        profileNumberTV = v.findViewById(R.id.user_profileNumberTV);
        logOutButton = v.findViewById(R.id.logOutButton);
        reference = FirebaseDatabase.getInstance().getReference();
        productsRecyclerView = v.findViewById(R.id.yourItemsRV);
        firebaseAuth = FirebaseAuth.getInstance();
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        setInfo();

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
    private void logOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(getContext(),Login.class));
        getActivity().finish();
    }
    private void setInfo(){
        String item="https://srmrc.nihr.ac.uk/wp-content/uploads/female-placeholder.jpg";
        Picasso.get().load(item)
                .fit()
                .centerInside()
                .into(profileImageIV);
        profileEmailTV.setText(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                model = snapshot.getValue(RegisterModel.class);
                assert model != null;
                System.out.println(model.getName() + model.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }
}