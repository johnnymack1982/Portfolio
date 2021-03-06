package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.CreateAccountFragment1;

public class CreateAccountActivity1 extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account1);

        displayFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Return to login activity
        Intent backIntent = new Intent(this, LoginActivity.class);
        startActivity(backIntent);
        finish();
    }

    // Custom methods
    // Custom method to inflate Login Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_create1, CreateAccountFragment1.newInstance(), CreateAccountFragment1.TAG)
                .commit();
    }
}
