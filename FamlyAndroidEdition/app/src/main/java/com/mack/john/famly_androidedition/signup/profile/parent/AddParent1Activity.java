package com.mack.john.famly_androidedition.signup.profile.parent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.profile.parent.AddParent1Fragment;

public class AddParent1Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent1);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_add_parent1, AddParent1Fragment.newInstance(), AddParent1Fragment.TAG)
                .commit();
    }
}
