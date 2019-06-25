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



    // System generated methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Call custom method to populate fragment
        populateFragment();
    }



    // Custom methods
    // Custom method to populate fragment
    private void populateFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_main, MainFragment.newInstance(), MainFragment.TAG)
                .commit();
    }
}
