package com.mack.john.crypjoy_androidedition;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.LifetimeDetailsFragment;

public class LifetimeDetailsActivity extends AppCompatActivity {



    // Class properties
    private static final String TAG = "LifetimeDetailsActivity";



    // System generated methods
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifetime_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        displayFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayFragment();
    }

    // Custom methods
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_lifetime_details, LifetimeDetailsFragment.newInstance(), LifetimeDetailsFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
    }
}
