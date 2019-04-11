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
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.LoggingUtils;

import java.util.ArrayList;
import java.util.Date;

public class LoggingFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LoggingFragment";

    View mView;
    LoggingUtils mLoggingUtils;



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

        mLoggingUtils = new LoggingUtils(getActivity(), mView);

        setClickListener();

        mLoggingUtils.checkProgress();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_give) {
            mLoggingUtils.giveJoy();
        }

        else if (view.getId() == R.id.button_get) {
            mLoggingUtils.getJoy();
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
