package com.mack.john.famly_androidedition.signup.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster2Fragment;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster3Fragment;
import com.mack.john.famly_androidedition.fragments.signup.profile.AddProfileFragment;

public class AddProfileActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_add_profile, AddProfileFragment.newInstance(), AddProfileFragment.TAG)
                .commit();
    }
}
