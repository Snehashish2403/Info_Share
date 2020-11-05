package com.crazydevstuff.infoshare.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.crazydevstuff.infoshare.Fragments.FavFragment;
import com.crazydevstuff.infoshare.Fragments.HomeFragment;
import com.crazydevstuff.infoshare.Fragments.ProfileFragment;
import com.crazydevstuff.infoshare.R;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar navigationBar ;
    private Fragment fragment;
    private FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar = findViewById(R.id.bottomNavigationView);
        container = findViewById(R.id.container);
        navigationBar.setItemEnabled(1,true);
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
                container=null;
                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                }
            }
        });
    }
}
