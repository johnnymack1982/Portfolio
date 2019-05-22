package com.mack.john.famly_androidedition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;

public class MainActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateFragment();

        // TODO: Implement authentication method to check for existing user
        Intent intent = new Intent(this, MasterLoginActivity.class);
        startActivity(intent);
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_main, MainFragment.newInstance(), MainFragment.TAG)
                .commit();
    }
}
