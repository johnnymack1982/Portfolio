package com.mack.john.famly_androidedition.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.fragments.signup.SignupMaster1Fragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;

public class SignupMaster1Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_master1);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_signup_master1, SignupMaster1Fragment.newInstance(), SignupMaster1Fragment.TAG)
                .commit();
    }
}
