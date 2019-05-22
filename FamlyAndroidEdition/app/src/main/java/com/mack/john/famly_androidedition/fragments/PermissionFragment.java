package com.mack.john.famly_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mack.john.famly_androidedition.R;

public class PermissionFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class Properties
    public static final String TAG = "PermissionFragment";

    EditText mRequestInput;

    Button mCancelButton;
    Button mRequestButton;



    // System generated methods
    public static PermissionFragment newInstance() {
        Bundle args = new Bundle();

        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);

        setClickListener(view);
        setFocusListener(view);

        return view;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mRequestButton.setVisibility(View.VISIBLE);
        }

        else {
            mCancelButton.setVisibility(View.GONE);
            mRequestButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            mRequestInput.clearFocus();
        }

        else if(view.getId() == R.id.button_request) {
            mRequestInput.clearFocus();
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        mCancelButton = view.findViewById(R.id.button_cancel);
        mRequestButton = view.findViewById(R.id.button_request);

        mCancelButton.setOnClickListener(this);
        mRequestButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mRequestInput = view.findViewById(R.id.input_request);

        mRequestInput.setOnFocusChangeListener(this);
    }
}
