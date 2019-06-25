package com.mack.john.famly_androidedition.fragments.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class ProfileLoginFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ProfileLoginFragment";

    String mFirstName;
    String mProfilePin;

    boolean mValidFirstName;
    boolean mValidPin;

    View mView;



    // System generated methods
    public static ProfileLoginFragment newInstance() {
        Bundle args = new Bundle();

        ProfileLoginFragment fragment = new ProfileLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_profile_login, container, false);

        // Reference view
        mView = view;

        // Call custom methods to set click and text change listeners
        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked cancel button, return to previous activity
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        // If user clicked continue button, attempt to log in to selected profile
        else if(view.getId() == R.id.button_continue) {
            AccountUtils.loginProfile(getActivity(), mFirstName, mProfilePin, mView);
        }
    }




    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference buttons in layout
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_continue);

        // Reference progress indicator and hide by default
        ProgressBar loadingProgress = view.findViewById(R.id.progress_loading);
        loadingProgress.setVisibility(View.GONE);

        // Set click listener for buttons
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }

    // Custom method to set text change listener
    private void setTextChangeListener(final View view) {
        // Set first name input listener
        final EditText firstNameInput = view.findViewById(R.id.input_first_name);
        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validName(firstNameInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), firstNameInput);

                    // Reference input in class properties
                    mFirstName = firstNameInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidFirstName = true;

                    // if all fields have valid input, enable continue button
                    if(mValidFirstName && mValidPin) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    // Otherwise, disable continue button
                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), firstNameInput);

                    // Indicate input is invalid
                    mValidFirstName = false;

                    // Disable continue button
                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set pin input listener
        final EditText pinInput = view.findViewById(R.id.input_profile_pin);
        pinInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validPin(pinInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), pinInput);

                    // Reference input in class properties
                    mProfilePin = pinInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidPin = true;

                    // If all fields have valid input, enable continue button
                    if(mValidFirstName && mValidPin) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    // Otherwise, disable continue button
                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), pinInput);

                    // Indicate input is invalid
                    mValidPin = false;

                    // Disable continue button
                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
