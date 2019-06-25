package com.mack.john.famly_androidedition.fragments.signup.profile.parent;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;
import com.mack.john.famly_androidedition.signup.profile.child.AddChild2Activity;
import com.mack.john.famly_androidedition.signup.profile.parent.AddParent2Activity;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddParent1Fragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {



    // Class properites
    public static final String TAG = "AddParent1Fragment";

    public static final String EXTRA_FIRST_NAME = "extra_first_name";
    public static final String EXTRA_DOB = "extra_dob";
    public static final String EXTRA_GENDER_ID = "extra_gender_id";
    public static final String EXTRA_ROLE_ID = "extra_role_id";
    public static final String EXTRA_PIN = "extra_pin";

    EditText mDobInput;
    Spinner mGenderInput;
    Spinner mRoleInput;

    String mFirstName;
    Date mDob;
    int mGenderId;
    int mRoleId;
    String mPinInput;
    int mPin;

    boolean mValidFirstName = false;
    boolean mValidDob = false;
    boolean mValidPin = false;
    boolean mPinsMatch = false;



    // System generated methods
    public static AddParent1Fragment newInstance() {
        Bundle args = new Bundle();

        AddParent1Fragment fragment = new AddParent1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_add_parent1, container, false);

        // Reference gender and family role input fields
        mGenderInput = view.findViewById(R.id.spinner_gender);
        mRoleInput = view.findViewById(R.id.input_family_role);

        // Call custom methods to set click and text change listeners
        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked date input field, call custom method to show date picker
        if(view.getId() == R.id.button_date_picker || view.getId() == R.id.input_dob) {
            pickDate();
        }

        // If user clicked cancel button, return to previous activity
        else if(view.getId() == R.id.button_cancel) {
            Intent loginIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(loginIntent);
        }

        // If user clicked continue button...
        else if(view.getId() == R.id.button_continue) {
            // Reference gender and family role input
            mGenderId = mGenderInput.getSelectedItemPosition();
            mRoleId = mRoleInput.getSelectedItemPosition();

            // Send user input to next activity and launch
            Intent continueIntent = new Intent(getActivity(), AddParent2Activity.class);
            continueIntent.putExtra(EXTRA_FIRST_NAME, mFirstName);
            continueIntent.putExtra(EXTRA_DOB, mDob);
            continueIntent.putExtra(EXTRA_GENDER_ID, mGenderId);
            continueIntent.putExtra(EXTRA_ROLE_ID, mRoleId);
            continueIntent.putExtra(EXTRA_PIN, mPin);

            startActivity(continueIntent);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        // Get selected date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        mDob = calendar.getTime();

        // Display selected date
        String displayDate = (month + 1) + "/" + dayOfMonth + "/" + year;
        mDobInput.setText(displayDate);

        // Indicate date of birth input is valid
        mValidDob = true;
    }




    // Custom methods
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

                    // Disable continue button
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

        // Set pin input listener
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

    // Custom method to show date picker
    private void pickDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
        datePickerDialog.setOnDateSetListener(this);
        datePickerDialog.show();
    }
}
