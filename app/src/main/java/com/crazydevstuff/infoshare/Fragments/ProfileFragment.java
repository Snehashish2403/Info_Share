package com.crazydevstuff.infoshare.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Activities.Login;
import com.crazydevstuff.infoshare.Activities.MainActivity;
import com.crazydevstuff.infoshare.Adapters.RecentItemsAdapter;
import com.crazydevstuff.infoshare.Interfaces.ItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements ItemsAdapterActionListener {

    private TextView profileNameTV, profileNumberTV, profileEmailTV;
    private CircleImageView profileImageIV;
    private ImageView changePic;
    private FirebaseAuth firebaseAuth;
    private Button logOutButton;
    private Uri filePath;
    private DatabaseReference dbReference;
    private StorageReference storageReference;
    private StorageTask task;
    private RecyclerView productsRecyclerView;
    private RecentItemsAdapter recentItemsAdapter;
    private FirebaseRecyclerOptions<ProductModel> options;

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
        dbReference = FirebaseDatabase.getInstance().getReference("users-list");
        storageReference= FirebaseStorage.getInstance().getReference("profilePics");
        productsRecyclerView = v.findViewById(R.id.yourItemsRV);
        changePic = v.findViewById(R.id.chaneProfilePicIV);
        firebaseAuth = FirebaseAuth.getInstance();
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        setInfo();
        getRecentItems();

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePic();
            }
        });

        return v;
    }
    private void logOut(){
        firebaseAuth.signOut();
        startActivity(new Intent(getContext(),Login.class));
        getActivity().finish();
    }
    private void setInfo(){
        dbReference.child(firebaseAuth.getUid()).child("prof_pic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profPic=snapshot.getValue(String.class);
                Picasso.get().load(profPic)
                        .fit()
                        .placeholder(R.drawable.placeholder)
                        .centerInside()
                        .into(profileImageIV);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        profileEmailTV.setText(firebaseAuth.getCurrentUser().getEmail());
        profileNameTV.setText(MainActivity.username);
    }

   private void getRecentItems(){
        String emailSeller = firebaseAuth.getCurrentUser().getEmail();
        Query query = FirebaseDatabase.getInstance().getReference().child("uploads").orderByChild("sellerEmail").equalTo(emailSeller);
        options = new FirebaseRecyclerOptions
                .Builder<ProductModel>()
                .setQuery(query,ProductModel.class)
                .build();

        recentItemsAdapter = new RecentItemsAdapter(options,this,getContext());
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





    private void updateProfilePic(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == 1
                && resultCode == -1
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            profileImageIV.setVisibility(View.VISIBLE);
        }
        setPicture(filePath);
    }
    private void setPicture(Uri uri){
        if(uri!=null){
            String userUid=firebaseAuth.getUid();
            final StorageReference reference=storageReference.child(userUid+".jpg");
            reference.delete();
            task=reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dbReference.child(firebaseAuth.getUid()).child("prof_pic").setValue(uri.toString());
                            Toast.makeText(getContext(),"Upload successful!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Upload failed!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onViewClicked(int clickedViewId, int clickedItemPosition, String itemKey) {

    }

    @Override
    public void onViewLongClicked(int clickedViewId, int clickedItemPosition) {

    }
}