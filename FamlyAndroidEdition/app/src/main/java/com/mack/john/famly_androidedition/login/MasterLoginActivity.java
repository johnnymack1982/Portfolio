package com.mack.john.famly_androidedition.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.login.MasterLoginFragment;

public class MasterLoginActivity extends AppCompatActivity {




    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_master);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_login_master, MasterLoginFragment.newInstance(), MasterLoginFragment.TAG)
                .commit();
    }
}
