package com.mack.john.famly_androidedition.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.login.MasterLoginFragment;
import com.mack.john.famly_androidedition.fragments.login.ProfileLoginFragment;

public class ProfileLoginActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_login);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_profile_login, ProfileLoginFragment.newInstance(), ProfileLoginFragment.TAG)
                .commit();
    }
}
