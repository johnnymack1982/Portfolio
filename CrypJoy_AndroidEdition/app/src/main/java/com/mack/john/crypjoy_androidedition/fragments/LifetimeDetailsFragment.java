package com.mack.john.crypjoy_androidedition.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.MapActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

import java.text.DecimalFormat;
import java.util.Objects;

public class LifetimeDetailsFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LifetimeDetailsFragment";



    // Constructor
    public LifetimeDetailsFragment() {}



    // System generated methods
    public static LifetimeDetailsFragment newInstance() {
        Bundle args = new Bundle();

        LifetimeDetailsFragment fragment = new LifetimeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifetime_details, container, false);

        // Indicate that screen should so an options menu
        setHasOptionsMenu(true);

        // Call custom method to set button click listener
        setClickListener(view);

        // Load lifetime progress
        JoyUtils joyUtils = new JoyUtils(getActivity());
        Joy lifetimeJoy = joyUtils.getLifetimeJoy();

        // Call custom method to display lifetime progress
        displayLifetimeProgress(view, lifetimeJoy);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked Add button, launch the Logging Activity
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(getActivity(), LoggingActivity.class);
            startActivity(addIntent);
        }

        // If user clicked Daily button, launch daily details activity
        else if(view.getId() == R.id.button_daily) {
            Intent dailyDetailsIntent = new Intent(getActivity(), DailyDetailsActivity.class);
            dailyDetailsIntent.putExtra("SENDING", "lifetime");

            startActivity(dailyDetailsIntent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // Inflate options menu layout
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If user clicked map button, launch the map activity
        if(item.getItemId() == R.id.action_map) {
            Intent mapIntent = new Intent(getActivity(), MapActivity.class);
            startActivity(mapIntent);
        }

        return super.onOptionsItemSelected(item);
    }




    // Custom methods
    // Custom method to set button click listeners
    private void setClickListener(View view) {
        // Set click listener for Add button
        FloatingActionButton addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        // Set click listener for Daily button
        Button dailyButton = view.findViewById(R.id.button_daily);
        dailyButton.setOnClickListener(this);
    }

    // Custom method to display lifetime progress
    private void displayLifetimeProgress(View view, Joy joy) {
        // Create string formatter to make scores easier to read
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String displayText;

        // Display lifetime Joy Given
        TextView lifetimeGiven = view.findViewById(R.id.display_lifetime_given);
        displayText = formatter.format(joy.getGiveProgress());
        lifetimeGiven.setText(displayText);

        // Display lifetime Joy Received
        TextView lifetimeReceived = view.findViewById(R.id.display_lifetime_received);
        displayText = formatter.format(joy.getGetProgress());
        lifetimeReceived.setText(displayText);

        // Display lifetime Pay It Forward progress
        TextView lifetimePaidForward = view.findViewById(R.id.display_lifetime_payItForward);
        displayText = formatter.format(joy.getPayItForwardProgress());
        lifetimePaidForward.setText(displayText);
    }
}
