package com.mack.john.famly_androidedition.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mack.john.famly_androidedition.R;

public class NewsFeedFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class properties
    public static final String TAG = "NewsFeedFragment";

    ImageButton mCameraButton;
    Button mCancelButton;
    Button mUpdateButton;

    EditText mPostInput;



    // System generated methods
    public static NewsFeedFragment newInstance() {
        Bundle args = new Bundle();

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        setClickListener(view);
        setFocusListener(view);

        return view;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mUpdateButton.setVisibility(View.VISIBLE);
        }

        else {
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_camera) {

        }

        else if(view.getId() == R.id.button_cancel) {
            mPostInput.clearFocus();
        }

        else if(view.getId() == R.id.button_update) {
            mPostInput.clearFocus();
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        mCameraButton = view.findViewById(R.id.button_camera);
        mCancelButton = view.findViewById(R.id.button_cancel);
        mUpdateButton = view.findViewById(R.id.button_update);

        mCameraButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mPostInput = view.findViewById(R.id.input_post);

        mPostInput.setOnFocusChangeListener(this);
    }
}
