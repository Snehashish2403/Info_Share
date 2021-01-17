package com.crazydevstuff.infoshare.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

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
import com.crazydevstuff.infoshare.Adapters.RecentItemsAdapter;
import com.crazydevstuff.infoshare.Interfaces.RecentItemsAdapterActionListener;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

public class ProfileFragment extends Fragment implements RecentItemsAdapterActionListener {

    private TextView profileNameTV, profileNumberTV, profileEmailTV;
    private CircleImageView profileImageIV;
    private ImageView changePic;
    private FirebaseAuth firebaseAuth;
    private Button logOutButton;
    private Uri filePath;
    private DatabaseReference reference;
    private RecyclerView productsRecyclerView;
    private RecentItemsAdapter recentItemsAdapter;
    private AlertDialog.Builder builder;
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

        recentItemsAdapter = new RecentItemsAdapter(options,this);
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


    @Override
    public void onViewClicked(int clickedViewId, int clickedItemPosition) {
        if(clickedViewId == R.id.delete_yourItemIV){
            showAlertDialog(clickedItemPosition);
        }
    }

    private void showAlertDialog(int clickedItemPosition){
        builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete this post ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(getContext(),"you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Toast.makeText(getContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete Item Post");
        alert.show();
    }

    private void updateProfilePic(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                && resultCode == 0
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getActivity().getContentResolver(),
                                filePath);
                profileImageIV.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewLongClicked(int clickedViewId, int clickedItemPosition) {

    }
}