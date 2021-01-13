package com.crazydevstuff.infoshare.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazydevstuff.infoshare.Activities.Login;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment {

    private TextView profileNameTV, profileNumberTV, profileEmailTV;
    private ImageView profileImageIV;
    private FirebaseAuth firebaseAuth;
    private Button logOutButton;
    private DatabaseReference reference;
    private RegisterModel model;

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

        firebaseAuth = FirebaseAuth.getInstance();
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        setInfo();

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