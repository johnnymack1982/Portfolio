package com.mack.john.famly_androidedition.fragments.signup.profile.child;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;
import com.mack.john.famly_androidedition.signup.profile.child.AddChild2Activity;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

import java.util.Calendar;
import java.util.Date;

public class AddChild1Fragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {



    // Class properties
    public static final String TAG = "AddChild1Fragment";

    public static final String EXTRA_FIRST_NAME = "extra_first_name";
    public static final String EXTRA_DOB = "extra_dob";
    public static final String EXTRA_GENDER_ID = "extra_gender_id";
    public static final String EXTRA_PIN = "extra_pin";

    EditText mDobInput;
    Spinner mGenderInput;

    String mFirstName;
    Date mDob;
    int mGenderId;
    String mPinInput;
    int mPin;

    boolean mValidFirstName = false;
    boolean mValidDob = false;
    boolean mValidPin = false;
    boolean mPinsMatch = false;



    // System generated methods
    public static AddChild1Fragment newInstance() {
        Bundle args = new Bundle();

        AddChild1Fragment fragment = new AddChild1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_add_child1, container, false);

        // Reference gender input field
        mGenderInput = view.findViewById(R.id.spinner_gender);

        // Call custom methods to set click and text change listeners
        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked date picker field, call custom method to open date picker
        if(view.getId() == R.id.button_date_picker || view.getId() == R.id.input_dob) {
            pickDate();
        }

        // If user clicked cancel button, return to previous activity
        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(cancelIntent);
        }

        // If user clicked continue button...
        else if(view.getId() == R.id.button_continue) {
            // Reference gender input
            mGenderId = mGenderInput.getSelectedItemPosition();

            // Send input to next activity and launch
            Intent continueIntent = new Intent(getActivity(), AddChild2Activity.class);
            continueIntent.putExtra(EXTRA_FIRST_NAME, mFirstName);
            continueIntent.putExtra(EXTRA_DOB, mDob);
            continueIntent.putExtra(EXTRA_GENDER_ID, mGenderId);
            continueIntent.putExtra(EXTRA_PIN, mPin);

            startActivity(continueIntent);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Reference calender and get selected date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        // Reference selected date
        mDob = calendar.getTime();

        // Display selected date
        String displayDate = (month + 1) + "/" + dayOfMonth + "/" + year;
        mDobInput.setText(displayDate);

        // Indicate valid date of birth input
        mValidDob = true;
    }




    // System generated methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        ImageButton calendarButton = view.findViewById(R.id.button_date_picker);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_continue);
        EditText dobInput = view.findViewById(R.id.input_dob);

        calendarButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        dobInput.setOnClickListener(this);
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

                    // If all fields have valid input, enable continue button
                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
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

                    // Disable continue butto
                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Reference date of birth input field
        mDobInput = view.findViewById(R.id.input_dob);

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
                    mPinInput = pinInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidPin = true;

                    // If all fields have valid input, enable continue button
                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
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

        // Set pin confirm input listener
        final EditText pinConfirmInput = view.findViewById(R.id.input_confirm_pin);
        pinConfirmInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input matches pin...
                if(InputUtils.pinsMatch(pinInput.getText().toString(), pinConfirmInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), pinConfirmInput);

                    // Reference password confirmation input in class properties
                    mPin = Integer.valueOf(pinInput.getText().toString().trim());

                    // Indicate input is valid
                    mPinsMatch = true;

                    // If all fields have valid input, enable continue button
                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
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
                    EditTextUtils.turnRed(getActivity(), pinConfirmInput);

                    // Indicate input is invalid
                    mPinsMatch = false;

                    // Disable continue button
                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Custom method to launch date picker
    private void pickDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show();
    }
}
