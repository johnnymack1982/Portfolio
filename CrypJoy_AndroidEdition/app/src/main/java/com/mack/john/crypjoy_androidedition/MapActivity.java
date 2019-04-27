package com.mack.john.crypjoy_androidedition;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mack.john.crypjoy_androidedition.utilities.AddButtonUtils;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {



    // Class properties
    private FloatingActionButton mAddButton;
    private AddButtonUtils mAddButtonUtils;



    // System generated methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAddButtonUtils = new AddButtonUtils();

        // Call custom method to set click listener for Add button
        setClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAddButtonUtils.show(mAddButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_secondary, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        mAddButtonUtils.hide(mAddButton);

        // If user clicked the Add button, launch the Logging activity
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(this, LoggingActivity.class);
            startActivity(addIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout) {
            FirebaseUtils.logout(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Hide Add button with animation
        mAddButtonUtils.hide(mAddButton);
    }

    // Custom methods
    // Custom method to set click listener for Add button
    private void setClickListener() {
        FloatingActionButton addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        // Show Add button with animation
        mAddButton = addButton;
        mAddButtonUtils.show(mAddButton);
    }
}
