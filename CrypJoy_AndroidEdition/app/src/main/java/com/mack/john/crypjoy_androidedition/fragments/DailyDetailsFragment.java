package com.mack.john.crypjoy_androidedition.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.LifetimeDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

public class DailyDetailsFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "DailyDetailsFragment";

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
