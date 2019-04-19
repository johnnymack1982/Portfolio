package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {



    // System generated methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Call custom method to set click listener for Add button
        setClickListener();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(this, LoggingActivity.class);
            startActivity(addIntent);
        }
    }

    // Custom methods
    // Custom method to set click listener for Add button
    private void setClickListener() {
        FloatingActionButton addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
    }
}
