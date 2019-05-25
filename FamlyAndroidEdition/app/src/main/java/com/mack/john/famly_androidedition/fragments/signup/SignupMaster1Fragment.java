package com.mack.john.famly_androidedition.fragments.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
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

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.signup.SignupMaster2Activity;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class SignupMaster1Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "SignupMaster1Fragment";

    public static final String EXTRA_FAMILY_NAME = "extra_family_name";
    public static final String EXTRA_STREET_ADDRESS = "extra_street_address";
    public static final String EXTRA_POSTAL_CODE = "extra_postal_code";

    String mFamilyName;
    String mStreetAddress;
    int mPostalCode;

    private boolean mValidFamilyName = false;
    boolean mValidStreetAddress = false;
    boolean mValidPostalCode = false;



    // System generated methods
    public static SignupMaster1Fragment newInstance() {
        Bundle args = new Bundle();

        SignupMaster1Fragment fragment = new SignupMaster1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_master1, container, false);

        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_continue) {
            Intent nextIntent = new Intent(getActivity(), SignupMaster2Activity.class);

            nextIntent.putExtra(EXTRA_FAMILY_NAME, mFamilyName);
            nextIntent.putExtra(EXTRA_STREET_ADDRESS, mStreetAddress);
            nextIntent.putExtra(EXTRA_POSTAL_CODE, mPostalCode);

            startActivity(nextIntent);
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_continue);

        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        ButtonUtils.disableContinueButton(getActivity(), view);
    }

    private void setTextChangeListener(final View view) {
        final EditText familyNameInput = view.findViewById(R.id.input_family_name);
        familyNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validName(familyNameInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), familyNameInput);

                    // Reference input in class properties
                    mFamilyName = familyNameInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidFamilyName = true;

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), familyNameInput);

                    // Indicate input is invalid
                    mValidFamilyName = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText streetAddressInput = view.findViewById(R.id.input_address);
        streetAddressInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validAddress(streetAddressInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), streetAddressInput);

                    // Reference input in class properties
                    mStreetAddress = streetAddressInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidStreetAddress = true;

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), streetAddressInput);

                    // Indicate input is invalid
                    mValidStreetAddress = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText postalCodeInput = view.findViewById(R.id.input_postal_code);
        postalCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validPostalCode(postalCodeInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), postalCodeInput);

                    // Reference input in class properties
                    mPostalCode = Integer.valueOf(postalCodeInput.getText().toString().trim());

                    // Indicate input is valid
                    mValidPostalCode = true;

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), postalCodeInput);

                    // Indicate input is invalid
                    mValidPostalCode = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
