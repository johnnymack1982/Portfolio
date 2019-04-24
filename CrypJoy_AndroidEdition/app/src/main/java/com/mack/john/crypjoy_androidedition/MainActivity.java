package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mack.john.crypjoy_androidedition.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayFragment();
    }



    // Custom methods
    // Custom method to inflate Login Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_login, LoginFragment.newInstance(), LoginFragment.TAG)
                .commit();
    }
}
