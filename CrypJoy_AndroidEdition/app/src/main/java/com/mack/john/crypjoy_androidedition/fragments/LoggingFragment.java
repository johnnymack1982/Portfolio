package com.mack.john.crypjoy_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

public class LoggingFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LoggingFragment";

    private View mView;
    private JoyUtils mJoyUtils;



    // Constructor
    public LoggingFragment() {}



    // System generated methods
    public static LoggingFragment newInstance() {
        Bundle args = new Bundle();

        LoggingFragment fragment = new LoggingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout
        View view = inflater.inflate(R.layout.fragment_logging, container, false);

        // Set the class View property to match this view
        mView = view;

        // Create new JoyUtils object and pass layout view to allow it to reference UI and make
        // any necessary changes
        mJoyUtils = new JoyUtils(getActivity(), view);

        // Call custom method to set click listener for "Give Joy" and "Get Joy" buttons
        setClickListener();

        // Call custom utility method to check current progress
        // This will cause the UI to react accordingly, altering the progress message and
        // enabling or disabling the buttons depending on the user's current progress
        mJoyUtils.checkProgress();

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked "Give Joy," call custom utility method to react accordingly
        if (view.getId() == R.id.button_give) {
            mJoyUtils.giveJoy();
        }

        // If user clicked "Get Joy," call custom utility method to react accordingly
        else if (view.getId() == R.id.button_get) {
            mJoyUtils.getJoy();
        }
    }



    // Custom methods
    // Custom method to set click listeners for Give and Get buttons
    private void setClickListener() {
        // Reference Give and Get buttons and set their click listeners
        Button giveButton = mView.findViewById(R.id.button_give);
        giveButton.setOnClickListener(this);

        Button getButton = mView.findViewById(R.id.button_get);
        getButton.setOnClickListener(this);
    }
}
