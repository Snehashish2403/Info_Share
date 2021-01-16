package com.crazydevstuff.infoshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crazydevstuff.infoshare.Fragments.FavFragment;
import com.crazydevstuff.infoshare.Fragments.HomeFragment;
import com.crazydevstuff.infoshare.Fragments.ProfileFragment;
import com.crazydevstuff.infoshare.Models.ProductModel;
import com.crazydevstuff.infoshare.Models.RegisterModel;
import com.crazydevstuff.infoshare.R;

import com.crazydevstuff.infoshare.ViewModel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static String username;
    private ChipNavigationBar navigationBar ;
    private Fragment fragment;
    private FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar = findViewById(R.id.bottomNavigationView);
        container = findViewById(R.id.container);
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.dash :
                        fragment = new HomeFragment();
                        break;
                    case R.id.fav :
                        fragment = new FavFragment();
                        break;
                    case R.id.user :
                        fragment = new ProfileFragment();
                        break;
                    default:
                        fragment = null;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    }
}
