package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.ForgotPasswordFragment;

public class ForgotPasswordActivity extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Call custom method to inflate Forgot Password Activity
        displayFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Return to Login activity
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }



    // Custom methods
    // Custom method to inflate Forgot Password Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_forgot_password, ForgotPasswordFragment.newInstance(), ForgotPasswordFragment.TAG)
                .commit();
    }
}
