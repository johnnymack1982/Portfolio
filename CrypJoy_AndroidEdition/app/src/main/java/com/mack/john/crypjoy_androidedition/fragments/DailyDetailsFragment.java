package com.mack.john.crypjoy_androidedition.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.LifetimeDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.MapActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.AddButtonUtils;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

import java.util.Objects;

public class DailyDetailsFragment extends Fragment implements View.OnClickListener, Dialog.OnClickListener {



    // Class properties
    public static final String TAG = "DailyDetailsFragment";

    private static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;

    private Joy mDailyJoy;

    private FloatingActionButton mAddButton;

    private AddButtonUtils mAddButtonUtils;



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

        // Instantiate Add button utilities
        mAddButtonUtils = new AddButtonUtils();

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
        // Hide Add button with animation
        mAddButtonUtils.hide(mAddButton);

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
        inflater.inflate(R.menu.menu_main, menu);
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

        mAddButton = addButton;

        // Show Add button with animation
        mAddButtonUtils.show(mAddButton);
    }

    // Call custom method to display progress for the current day
    private void displayProgress(View view) {
        // Define and start view header animation
        Animation welcomeTextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_text);
        welcomeTextAnimation.reset();
        TextView welcomeText = view.findViewById(R.id.text_welcome);
        welcomeText.startAnimation(welcomeTextAnimation);

        // Update Joy Given progress bar
        ProgressBar joyGivenProgressBar = view.findViewById(R.id.progressBar_joyGiven);
        joyGivenProgressBar.setMax(mDailyJoy.getGiveGoal() * 100);

        // Animate Joy Given progress bar progress
        ObjectAnimator joyGivenProgressAnimation = ObjectAnimator.ofInt(joyGivenProgressBar,
                "progress", mDailyJoy.getGiveProgress() * 100);
        joyGivenProgressAnimation.setDuration(1000);
        joyGivenProgressAnimation.setStartDelay(750);
        joyGivenProgressAnimation.setInterpolator(new BounceInterpolator());
        joyGivenProgressAnimation.start();

        // Update Joy Given text display
        TextView joyGivenDisplay = view.findViewById(R.id.display_joyGiven);
        joyGivenDisplay.setText(mDailyJoy.displayGiven());

        // Define and start Joy Given display animation
        Animation joyGivenAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_score);
        joyGivenAnimation.setStartOffset(500);
        joyGivenAnimation.reset();
        joyGivenDisplay.startAnimation(joyGivenAnimation);

        // Update Joy Received text display
        TextView joyReceivedDisplay = view.findViewById(R.id.display_joyReceived);
        joyReceivedDisplay.setText(mDailyJoy.displayReceived());

        // Define and start Joy Received display animation
        Animation joyReceivedAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_score);
        joyReceivedAnimation.setStartOffset(1000);
        joyReceivedAnimation.reset();
        joyReceivedDisplay.startAnimation(joyReceivedAnimation);

        // Update Pay It Forward progress bar
        ProgressBar payItForwardProgressBar = view.findViewById(R.id.progressbar_payItForward);
        payItForwardProgressBar.setMax(mDailyJoy.getPayItForwardGoal() * 100);

        // Animate Pay It Forward progress bar progress
        ObjectAnimator payItForwardProgressAnimation = ObjectAnimator.ofInt(payItForwardProgressBar,
                "progress", mDailyJoy.getPayItForwardProgress() * 100);
        payItForwardProgressAnimation.setDuration(1000);
        payItForwardProgressAnimation.setStartDelay(1750);
        payItForwardProgressAnimation.setInterpolator(new BounceInterpolator());
        payItForwardProgressAnimation.start();

        // Update Pay It Forward text display
        TextView payItForwardDisplay = view.findViewById(R.id.display_payItForward);
        payItForwardDisplay.setText(mDailyJoy.displayPayItForward());

        // Define and start Pay It Forward display animation
        Animation payItForwardAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_score);
        payItForwardAnimation.setStartOffset(1500);
        payItForwardAnimation.reset();
        payItForwardDisplay.startAnimation(payItForwardAnimation);
    }
}
