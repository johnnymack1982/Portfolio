package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    // Class properties



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Temporarily launch into currently developed portions of the application
        // This code will be replaced further along in the development cycle
        Intent intent = new Intent(this, DailyDetailsActivity.class);
        startActivity(intent);
    }
}
