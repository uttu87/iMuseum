package com.android.iseasoft.imuseum.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.android.iseasoft.imuseum.R;

public class MainActivity extends AppCompatActivity{
    private ActionBar mBottomActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomActionBar = getSupportActionBar();
        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.bottom_bar);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(MuseumHighLightFragment.getInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Fragment fragment;

            switch (item.getItemId()){
                case R.id.navigation_home:
                    loadFragment(MuseumHighLightFragment.getInstance());
                    return true;
                case R.id.navigation_place:
                    loadFragment(MuseumMapFragment.getInstance());
                    return true;
                case R.id.navigation_scanner:

                    //loadFragment(CartFragment.getInstance());
                    return true;
                case R.id.navigation_folder:
                    //loadFragment(ProfileFragment.getInstance());
                    return true;
                case R.id.navigation_setting:
                    //loadFragment(ProfileFragment.getInstance());
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

}