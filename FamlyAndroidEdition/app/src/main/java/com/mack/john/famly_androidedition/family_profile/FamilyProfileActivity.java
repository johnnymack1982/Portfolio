package com.mack.john.famly_androidedition.family_profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.fragments.family_profile.FamilyProfileFragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;

public class FamilyProfileActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_profile);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_family_profile, FamilyProfileFragment.newInstance(), FamilyProfileFragment.TAG)
                .commit();
    }
}
