package com.mack.john.famly_androidedition.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.locate.LocateSelectionActivity;

public class CheckinFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class properties
    public static final String TAG = "CheckinFragment";

    EditText mCheckinInput;

    ImageButton mCameraButton;
    Button mCheckinButton;
    Button mFindButton;



    // System generated methods
    public static CheckinFragment newInstance() {
        Bundle args = new Bundle();

        CheckinFragment fragment = new CheckinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

        setClickListener(view);
        setFocusListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_checkin) {

        }

        else if(view.getId() == R.id.button_find) {
            Intent findIntent = new Intent(getActivity(), LocateSelectionActivity.class);
            startActivity(findIntent);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

    }



    // Custom methods
    private void  setClickListener(View view) {
        mCameraButton = view.findViewById(R.id.button_photo);
        mCheckinButton = view.findViewById(R.id.button_checkin);
        mFindButton = view.findViewById(R.id.button_find);

        mCameraButton.setOnClickListener(this);
        mCheckinButton.setOnClickListener(this);
        mFindButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mCheckinInput = view.findViewById(R.id.input_checkin);

        mCheckinInput.setOnFocusChangeListener(this);
    }
}
