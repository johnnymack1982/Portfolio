package com.mack.john.famly_androidedition.signup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster1Fragment;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster2Fragment;

public class SignupMaster2Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_master2);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_signup_master2, SignupMaster2Fragment.newInstance(), SignupMaster2Fragment.TAG)
                .commit();
    }
}
