package com.mack.john.famly_androidedition.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.login.ForgotPasswordFragment;
import com.mack.john.famly_androidedition.fragments.login.MasterLoginFragment;

public class ForgotPasswordActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_forgot_password, ForgotPasswordFragment.newInstance(), ForgotPasswordFragment.TAG)
                .commit();
    }
}
