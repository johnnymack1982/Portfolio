package com.mack.john.crypjoy_androidedition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mack.john.crypjoy_androidedition.fragments.LoadingFragment;

public class MainActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call custom method to inflate Loading fragment
        displayFragment();
    }



    // Custom methods
    // Custom method to inflate Loading Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_loading, LoadingFragment.newInstance(), LoadingFragment.TAG)
                .commit();
    }
}
