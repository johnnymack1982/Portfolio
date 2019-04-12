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

    View mView;
    JoyUtils mJoyUtils;



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
        View view = inflater.inflate(R.layout.fragment_logging, container, false);

        mView = view;

        mJoyUtils = new JoyUtils(getActivity(), mView);

        setClickListener();

        mJoyUtils.checkProgress();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_give) {
            mJoyUtils.giveJoy();
        }

        else if (view.getId() == R.id.button_get) {
            mJoyUtils.getJoy();
        }
    }



    // Custom methods
    private void setClickListener() {
        Button giveButton = mView.findViewById(R.id.button_give);
        giveButton.setOnClickListener(this);

        Button getButton = mView.findViewById(R.id.button_get);
        getButton.setOnClickListener(this);
    }
}
