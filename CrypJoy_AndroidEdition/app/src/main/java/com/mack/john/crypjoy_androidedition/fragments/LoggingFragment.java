package com.mack.john.crypjoy_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mack.john.crypjoy_androidedition.R;

public class LoggingFragment extends Fragment {



    // Class properties
    public static final String TAG = "LoggingFragment";



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

        return view;
    }
}
