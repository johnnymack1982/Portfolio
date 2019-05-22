package com.mack.john.famly_androidedition.signup.profile.child;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.signup.profile.child.AddChild1Fragment;
import com.mack.john.famly_androidedition.fragments.signup.profile.child.AddChild2Fragment;

public class AddChild2Activity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child2);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_add_child2, AddChild2Fragment.newInstance(), AddChild2Fragment.TAG)
                .commit();
    }
}
