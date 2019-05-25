package com.mack.john.famly_androidedition.fragments.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.signup.SignupMaster3Activity;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class SignupMaster2Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "SignupMaster2Fragment";

    public static final String EXTRA_EMAIL = "extra_email";
    public static final String EXTRA_PASSWORD = "extra_password";

    String mFamilyName;
    String mStreetAddress;
    int mPostalCode;

    String mEmail;
    String mPassword;

    boolean mValidEmail;
    boolean mValidPassword;
    boolean mPasswordsMatch;



    // System generated methods
    public static SignupMaster2Fragment newInstance() {
        Bundle args = new Bundle();

        SignupMaster2Fragment fragment = new SignupMaster2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_master2, container, false);

        setClickListener(view);
        setTextChangeListener(view);

        Intent sendingIntent = getActivity().getIntent();

        mFamilyName = sendingIntent.getStringExtra(SignupMaster1Fragment.EXTRA_FAMILY_NAME);
        mStreetAddress = sendingIntent.getStringExtra(SignupMaster1Fragment.EXTRA_STREET_ADDRESS);
        mPostalCode = sendingIntent.getIntExtra(SignupMaster1Fragment.EXTRA_POSTAL_CODE, 0);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            Intent loginIntent = new Intent(getActivity(), MasterLoginActivity.class);
            startActivity(loginIntent);
        }

        else if(view.getId() == R.id.button_continue) {
            Intent nextIntent = new Intent(getActivity(), SignupMaster3Activity.class);

            nextIntent.putExtra(SignupMaster1Fragment.EXTRA_FAMILY_NAME, mFamilyName);
            nextIntent.putExtra(SignupMaster1Fragment.EXTRA_STREET_ADDRESS, mStreetAddress);
            nextIntent.putExtra(SignupMaster1Fragment.EXTRA_POSTAL_CODE, mPostalCode);

            nextIntent.putExtra(EXTRA_EMAIL, mEmail);
            nextIntent.putExtra(EXTRA_PASSWORD, mPassword);

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
        final EditText masterEmailInput = view.findViewById(R.id.input_master_email);
        masterEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If input is a valid email address...
                if (InputUtils.validEmail(masterEmailInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), masterEmailInput);

                    // Reference input in class properties
                    mEmail = masterEmailInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidEmail = true;

                    if(mValidEmail && mValidPassword && mPasswordsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), masterEmailInput);

                    // Indicate input is invalid
                    mValidEmail = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set password rules to be invisible by default
        final TextView passwordRules = view.findViewById(R.id.display_password_rules);
        passwordRules.setVisibility(View.GONE);

        final EditText passwordInput = view.findViewById(R.id.input_master_password);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If password input is valid...
                if(InputUtils.validPassword(passwordInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), passwordInput);

                    // Hide password rules
                    passwordRules.setVisibility(View.GONE);

                    // Indicate input is valid
                    mValidPassword = true;

                    if(mValidEmail && mValidPassword && mPasswordsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), passwordInput);

                    // Show password rules
                    passwordRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mValidPassword = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set password confirmation rules to be invisible by default
        final TextView passwordConfirmRules = view.findViewById(R.id.display_password_match_rules);
        passwordConfirmRules.setVisibility(View.GONE);

        final EditText passwordConfirmInput = view.findViewById(R.id.input_confirm_master_password);
        passwordConfirmInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input matches password...
                if(InputUtils.passwordsMatch(passwordInput.getText().toString(), passwordConfirmInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), passwordConfirmInput);

                    // Hide password confirmation rules
                    passwordConfirmRules.setVisibility(View.GONE);

                    // Reference password confirmation input in class properties
                    mPassword = passwordInput.getText().toString().trim();

                    // Indicate input is valid
                    mPasswordsMatch = true;

                    if(mValidEmail && mValidPassword && mPasswordsMatch) {
                        ButtonUtils.enableContinueButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableContinueButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), passwordConfirmInput);

                    // Show password confirmation rules
                    passwordConfirmRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mPasswordsMatch = false;

                    ButtonUtils.disableContinueButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
