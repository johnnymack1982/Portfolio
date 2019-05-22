package com.mack.john.famly_androidedition.locate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.fragments.locate.LocateSelectionFragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;

public class LocateSelectionActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_selection);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_locate_selection, LocateSelectionFragment.newInstance(), LocateSelectionFragment.TAG)
                .commit();
    }
}
