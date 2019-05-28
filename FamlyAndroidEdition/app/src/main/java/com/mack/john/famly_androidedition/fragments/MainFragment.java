package com.mack.john.famly_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.utils.AccountUtils;

public class MainFragment extends Fragment {



    // Class properties
    public static final String TAG = "MainFragment";



    // System generated methods
    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        AccountUtils.checkAuth(getActivity());

        return view;
    }
}
