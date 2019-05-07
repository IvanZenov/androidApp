package com.example.zevs.mykursach2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zevs.mykursach2.Fragment.ChatFragment;
import com.example.zevs.mykursach2.Fragment.ProfileFragment;
import com.example.zevs.mykursach2.Fragment.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.zevs.mykursach2.R.color.selector;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    Fragment selectedfragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navigation = findViewById(R.id.navigation);

        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                new ProfileFragment()).commit();



    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.navigation_events:
                            selectedfragment = new SearchFragment();
                            break;
                        case R.id.navigation_chat:
                            selectedfragment = new ChatFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedfragment = new ProfileFragment();
                            break;
                    }
                    if (selectedfragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                                selectedfragment).commit();
                    }
                    return true;
                }
            };
}
