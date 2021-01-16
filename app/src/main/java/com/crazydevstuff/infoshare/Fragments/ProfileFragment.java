package com.crazydevstuff.infoshare.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Activities.Login;
import com.crazydevstuff.infoshare.Activities.MainActivity;
import com.crazydevstuff.infoshare.Adapters.HomeProductsAdapter;
import com.crazydevstuff.infoshare.Adapters.RecentItemsAdapter;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.crazydevstuff.infoshare.ViewModel.ProductViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private RecyclerView productsRecyclerView;
    private RecentItemsAdapter recentItemsAdapter;
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
        getRecentItems();
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
        profileEmailTV.setText(firebaseAuth.getCurrentUser().getEmail());

    }

   private void getRecentItems(){
        String emailSeller = firebaseAuth.getCurrentUser().getEmail();
        Query query = FirebaseDatabase.getInstance().getReference().child("uploads").orderByChild("sellerEmail").equalTo(emailSeller);
        FirebaseRecyclerOptions<ProductModel> options = new FirebaseRecyclerOptions.Builder<ProductModel>().setQuery(query,ProductModel.class).build();

        recentItemsAdapter = new RecentItemsAdapter(options);
        productsRecyclerView.setAdapter(recentItemsAdapter);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        recentItemsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recentItemsAdapter.stopListening();
    }


}