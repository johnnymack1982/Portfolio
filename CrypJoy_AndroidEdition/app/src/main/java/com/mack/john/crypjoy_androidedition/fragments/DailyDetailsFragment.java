package com.mack.john.crypjoy_androidedition.fragments;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.LifetimeDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.MapActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

public class DailyDetailsFragment extends Fragment implements View.OnClickListener, Dialog.OnClickListener {



    // Class properties
    public static final String TAG = "DailyDetailsFragment";

    public static final int REQUEST_LOCATION_PERMISSIONS = 0x01001;

    Joy mDailyJoy;



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
        View view = inflater.inflate(R.layout.fragment_daily_details, container, false);

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);
        }

        setHasOptionsMenu(true);

        setClickListener(view);

        JoyUtils joyUtils = new JoyUtils(getActivity());
        mDailyJoy = joyUtils.getDailyJoy();

        displayProgress(view, mDailyJoy);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(getActivity(), LoggingActivity.class);
            startActivity(addIntent);
        }

        else if(view.getId() == R.id.button_lifetime) {
            Intent lifetimeDetailsIntent = new Intent(getActivity(), LifetimeDetailsActivity.class);
            startActivity(lifetimeDetailsIntent);
            getActivity().overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        System.exit(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    private void setClickListener(View view) {
        FloatingActionButton addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Button lifetimeButton = view.findViewById(R.id.button_lifetime);
        lifetimeButton.setOnClickListener(this);
    }

    private void displayProgress(View view, Joy joy) {
        ProgressBar joyGivenProgressBar = view.findViewById(R.id.progressBar_joyGiven);
        joyGivenProgressBar.setMax(joy.getGiveGoal());
        joyGivenProgressBar.setProgress(joy.getGiveProgress());

        TextView joyGivenDisplay = view.findViewById(R.id.display_joyGiven);
        joyGivenDisplay.setText(joy.displayGiven());

        TextView joyReceivedDisplay = view.findViewById(R.id.display_joyReceived);
        joyReceivedDisplay.setText(joy.displayReceived());

        ProgressBar payItForwardProgressBar = view.findViewById(R.id.progressbar_payItForward);
        payItForwardProgressBar.setMax(joy.getPayItForwardGoal());
        payItForwardProgressBar.setProgress(joy.getPayItForwardProgress());

        TextView payItForwardDisplay = view.findViewById(R.id.display_payItForward);
        payItForwardDisplay.setText(joy.displayPayItForward());
    }
}
