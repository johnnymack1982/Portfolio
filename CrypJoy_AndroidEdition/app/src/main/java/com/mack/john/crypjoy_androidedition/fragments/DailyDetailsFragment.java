package com.mack.john.crypjoy_androidedition.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.LifetimeDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.MapActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

import java.util.Objects;

public class DailyDetailsFragment extends Fragment implements View.OnClickListener, Dialog.OnClickListener {



    // Class properties
    public static final String TAG = "DailyDetailsFragment";

    private static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;

    private Joy mDailyJoy;



    // Constructor
    public DailyDetailsFragment() {}



    // System generated methods
    public static DailyDetailsFragment newInstance() {
        Bundle args = new Bundle();

        DailyDetailsFragment fragment = new DailyDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout
        View view = inflater.inflate(R.layout.fragment_daily_details, container, false);

        // Check for location permissions. If they don't exist, request them
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);
        }

        // Indicate that the current activity should display an options menu
        setHasOptionsMenu(true);

        // Call custom method to set button click listener
        setClickListener(view);

        // Load progress for the current day
        JoyUtils joyUtils = new JoyUtils(getActivity());
        mDailyJoy = joyUtils.getDailyJoy();

        // Call custom method to display progress for the current day
        displayProgress(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked the Add button, launch the Logging activity
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(getActivity(), LoggingActivity.class);
            startActivity(addIntent);
        }

        // If user clicked the Lifetime button, load the lifetime progress activity
        else if(view.getId() == R.id.button_lifetime) {
            Intent lifetimeDetailsIntent = new Intent(getActivity(), LifetimeDetailsActivity.class);
            startActivity(lifetimeDetailsIntent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        System.exit(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate options menu
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If user clicked the map button, launch the map activity
        if(item.getItemId() == R.id.action_map) {
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);
            startActivity(mapIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Create new dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set positive and negative buttons
        builder.setPositiveButton(getString(R.string.ok), this);

        // Set title and message
        builder.setTitle(getString(R.string.location_permissions_required));
        builder.setMessage("Unfortunately, this application requires the use of location permissions.");

        // Build and show dialog
        AlertDialog getJoyDialog = builder.create();
        getJoyDialog.show();
    }




    // Custom methods
    // Custom method to set button click listener
    private void setClickListener(View view) {
        // Set Add button click listener
        FloatingActionButton addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        // Set Lifetime button click listener
        Button lifetimeButton = view.findViewById(R.id.button_lifetime);
        lifetimeButton.setOnClickListener(this);
    }

    // Call custom method to display progress for the current day
    private void displayProgress(View view) {
        // Update Joy Given progress bar
        ProgressBar joyGivenProgressBar = view.findViewById(R.id.progressBar_joyGiven);
        joyGivenProgressBar.setMax(mDailyJoy.getGiveGoal());
        joyGivenProgressBar.setProgress(mDailyJoy.getGiveProgress());

        // Update Joy Given text display
        TextView joyGivenDisplay = view.findViewById(R.id.display_joyGiven);
        joyGivenDisplay.setText(mDailyJoy.displayGiven());

        // Update Joy Received text display
        TextView joyReceivedDisplay = view.findViewById(R.id.display_joyReceived);
        joyReceivedDisplay.setText(mDailyJoy.displayReceived());

        // Update Pay It Forward progress bar
        ProgressBar payItForwardProgressBar = view.findViewById(R.id.progressbar_payItForward);
        payItForwardProgressBar.setMax(mDailyJoy.getPayItForwardGoal());
        payItForwardProgressBar.setProgress(mDailyJoy.getPayItForwardProgress());

        // Update Pay It Forward text display
        TextView payItForwardDisplay = view.findViewById(R.id.display_payItForward);
        payItForwardDisplay.setText(mDailyJoy.displayPayItForward());
    }
}
