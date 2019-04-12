package com.mack.john.crypjoy_androidedition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.LoggingFragment;

public class LoggingActivity extends AppCompatActivity {


    // System generated methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        // Call custom function to inflate Logging Fragment
        displayFragment();
    }



    // Custom methods
    // Custom method to inflate Logging Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_logging, LoggingFragment.newInstance(), LoggingFragment.TAG)
                .commit();
    }
}
