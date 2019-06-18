package com.mack.john.famly_androidedition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mack.john.famly_androidedition.fragments.MainFragment;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;

public class MainActivity extends AppCompatActivity {



    // Class properties
    private static final String TAG = "MainActivity";

    private static final int LOCATION_PERMISSION_REQUEST = 4;



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateFragment();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }

        AccountUtils.checkAuth(this);
    }



    // Custom methods
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_main, MainFragment.newInstance(), MainFragment.TAG)
                .commit();
    }
}
