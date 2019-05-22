package com.mack.john.famly_androidedition.signup.profile.parent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.profile.parent.AddParent1Fragment;
import com.mack.john.famly_androidedition.fragments.signup.profile.parent.AddParent2Fragment;

public class AddParent2Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent2);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_add_parent2, AddParent2Fragment.newInstance(), AddParent2Fragment.TAG)
                .commit();
    }
}
