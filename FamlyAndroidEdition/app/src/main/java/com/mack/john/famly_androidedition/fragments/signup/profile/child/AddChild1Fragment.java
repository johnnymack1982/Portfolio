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
        View view = inflater.inflate(R.layout.fragment_add_child1, container, false);

        mGenderInput = view.findViewById(R.id.spinner_gender);

        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_date_picker || view.getId() == R.id.input_dob) {
            pickDate();
        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(cancelIntent);
        }

        else if(view.getId() == R.id.button_continue) {
            mGenderId = mGenderInput.getSelectedItemPosition();

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
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        mDob = calendar.getTime();

        String displayDate = (month + 1) + "/" + dayOfMonth + "/" + year;

        mDobInput.setText(displayDate);

        mValidDob = true;
    }




    // System generated methods
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

    private void setTextChangeListener(final View view) {
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

                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

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

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mDobInput = view.findViewById(R.id.input_dob);

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

                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

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

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

                    if(mValidFirstName && mValidDob && mValidPin && mPinsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

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

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void pickDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show();
    }
}
