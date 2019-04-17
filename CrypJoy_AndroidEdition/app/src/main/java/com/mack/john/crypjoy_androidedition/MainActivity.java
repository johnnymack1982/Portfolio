package com.mack.john.crypjoy_androidedition;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mack.john.crypjoy_androidedition.utilities.LocationUtils;

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
