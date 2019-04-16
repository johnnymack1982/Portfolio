package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mack.john.crypjoy_androidedition.fragments.DailyDetailsFragment;

public class DailyDetailsActivity extends AppCompatActivity {



    // System generated methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        displayFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayFragment();
    }

    // Custome methods
    private void displayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_daily_details, DailyDetailsFragment.newInstance(), DailyDetailsFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent sendingIntent = getIntent();

        String sending = sendingIntent.getStringExtra("SENDING");

        if(sending != null && sending.equals("lifetime")) {
            overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }
    }
}
