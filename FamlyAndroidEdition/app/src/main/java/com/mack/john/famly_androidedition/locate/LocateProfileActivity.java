package com.mack.john.famly_androidedition.locate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.fragments.locate.LocateProfileFragment;

public class LocateProfileActivity extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locate_profile);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_locate_profile, LocateProfileFragment.newInstance(), LocateProfileFragment.TAG)
                .commit();
    }
}
