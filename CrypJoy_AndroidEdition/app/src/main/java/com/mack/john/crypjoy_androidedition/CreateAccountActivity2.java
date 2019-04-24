package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.CreateAccountFragment1;
import com.mack.john.crypjoy_androidedition.fragments.CreateAccountFragment2;

public class CreateAccountActivity2 extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account2);

        displayFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
        finish();
    }

    // Custom methods
    // Custom method to inflate Login Fragment
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_create2, CreateAccountFragment2.newInstance(), CreateAccountFragment2.TAG)
                .commit();
    }
}
