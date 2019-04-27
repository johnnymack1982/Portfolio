package com.mack.john.crypjoy_androidedition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.LoadingFragment;
import com.mack.john.crypjoy_androidedition.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
