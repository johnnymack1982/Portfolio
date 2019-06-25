package com.mack.john.famly_androidedition.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster2Fragment;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster3Fragment;

public class SignupMaster3Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_master3);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_signup_master3, SignupMaster3Fragment.newInstance(), SignupMaster2Fragment.TAG)
                .commit();
    }
}
