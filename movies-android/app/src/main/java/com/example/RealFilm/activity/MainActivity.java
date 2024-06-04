package com.example.RealFilm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

//import com.example.doantotnghiep_xemphim.databinding.ActivityMainBinding;
import com.example.RealFilm.R;
import com.example.RealFilm.fragment.HomeFragment;
import com.example.RealFilm.fragment.UserProfileFragment;
import com.example.RealFilm.fragment.SearchFragment;
import com.example.RealFilm.fragment.SettingFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    //ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    BadgeDrawable badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        badge = bottomNavigationView.getOrCreateBadge(R.id.profile);
//        badge.setNumber(9);
//        badge.setBackgroundColor(getResources().getColor(R.color.red));
//        badge.setBadgeTextColor(getResources().getColor(R.color.white));

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new UserProfileFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new SettingFragment());
                    break;

            }

            return true;
        });


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment,fragment);
        fragmentTransaction.commit();
    }
}