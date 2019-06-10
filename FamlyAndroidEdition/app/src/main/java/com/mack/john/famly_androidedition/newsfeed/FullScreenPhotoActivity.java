package com.mack.john.famly_androidedition.newsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.fragments.newsfeed.FullScreenPhotoFragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;

public class FullScreenPhotoActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);

        populateFragment();
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_full_screen_photo, FullScreenPhotoFragment.newInstance(), FullScreenPhotoFragment.TAG)
                .commit();
    }
}
