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
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoggingActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;

import java.text.DecimalFormat;

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

        setClickListener(view);

        JoyUtils joyUtils = new JoyUtils(getActivity());

        Joy lifetimeJoy = joyUtils.getLifetimeJoy();

        displayLifetimeProgress(view, lifetimeJoy);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(getActivity(), LoggingActivity.class);
            startActivity(addIntent);
        }

        else if(view.getId() == R.id.button_daily) {
            Intent dailyDetailsIntent = new Intent(getActivity(), DailyDetailsActivity.class);
            dailyDetailsIntent.putExtra("SENDING", "lifetime");

            startActivity(dailyDetailsIntent);
            getActivity().overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        FloatingActionButton addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(this);

        Button dailyButton = view.findViewById(R.id.button_daily);
        dailyButton.setOnClickListener(this);
    }

    private void displayLifetimeProgress(View view, Joy joy) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String displayText;

        TextView lifetimeGiven = view.findViewById(R.id.display_lifetime_given);
        displayText = formatter.format(joy.getGiveProgress());
        lifetimeGiven.setText(displayText);

        TextView lifetimeReceived = view.findViewById(R.id.display_lifetime_received);
        displayText = formatter.format(joy.getGetProgress());
        lifetimeReceived.setText(displayText);

        TextView lifetimePaidForward = view.findViewById(R.id.display_lifetime_payItForward);
        displayText = formatter.format(joy.getPayItForwardProgress());
        lifetimePaidForward.setText(displayText);
    }
}
