package com.crazydevstuff.infoshare.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.crazydevstuff.infoshare.FavFragment;
import com.crazydevstuff.infoshare.HomeFragment;
import com.crazydevstuff.infoshare.ProfileFragment;
import com.crazydevstuff.infoshare.R;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar navigationBar ;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar = findViewById(R.id.bottomNavigationView);
        navigationBar.setItemSelected(1,true);
        getSupportFragmentManager().beginTransaction().add(R.id.container,new HomeFragment()).commit();
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
                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
                }
            }
        });
    }
}
