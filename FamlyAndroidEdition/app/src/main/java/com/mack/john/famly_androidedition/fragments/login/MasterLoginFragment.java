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
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.login.ForgotPasswordActivity;
import com.mack.john.famly_androidedition.login.ProfileLoginActivity;
import com.mack.john.famly_androidedition.signup.SignupMaster1Activity;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class MasterLoginFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "MasterLoginFragment";

    private static String mEmail;
    private static String mPassword;

    private static boolean mValidEmail;
    private static boolean mValidPassword;

    private static View mView;



    // System generated methods
    public static MasterLoginFragment newInstance() {
        Bundle args = new Bundle();

        MasterLoginFragment fragment = new MasterLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_login_master, container, false);

        // Reference view
        mView = view;

        // Disable login button by default
        ButtonUtils.disableLoginButton(getActivity(), view);

        // Call custom methods to set click and text change listeners
        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked login button, attempt to log in
        if(view.getId() == R.id.button_login) {
            AccountUtils.login(getActivity(), mEmail, mPassword, mView);
        }

        // If user clicked forgot password button, launch forgot password activity
        else if(view.getId() == R.id.button_forgot_password) {
            Intent forgotPasswordIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
        }

        // If user clicked signup button, launch signup activity
        else if(view.getId() == R.id.button_sign_up) {
            Intent signupIntent = new Intent(getActivity(), SignupMaster1Activity.class);
            startActivity(signupIntent);
        }
    }



    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference buttons in layout
        Button loginButton = view.findViewById(R.id.button_login);
        Button forgotPasswordButton = view.findViewById(R.id.button_forgot_password);
        Button signupButton = view.findViewById(R.id.button_sign_up);

        // Reference progress indicator and hide by default
        ProgressBar loadingProgress = view.findViewById(R.id.progress_loading);
        loadingProgress.setVisibility(View.GONE);

        // Set click listener
        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    // Custom method to set text change listener
    private void setTextChangeListener( final View view) {
        // Set master email input listener
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

                    // If all fields have valid input, enable login button
                    if(mValidEmail && mValidPassword) {
                        ButtonUtils.enableLoginButton(getActivity(), view);
                    }

                    // Otherwise, disable login button
                    else {
                        ButtonUtils.disableLoginButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), masterEmailInput);

                    // Indicate input is invalid
                    mValidEmail = false;

                    // Disable login button
                    ButtonUtils.disableLoginButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set password rules to be invisible by default
        final TextView passwordRules = view.findViewById(R.id.display_password_rules);
        passwordRules.setVisibility(View.GONE);

        // Set password input listener
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

                    // Reference password input
                    mPassword = passwordInput.getText().toString().trim();

                    // If all fields have valid input, enable login button
                    if(mValidEmail && mValidPassword) {
                        ButtonUtils.enableLoginButton(getActivity(), view);
                    }

                    // Otherwise, disable login button
                    else {
                        ButtonUtils.disableLoginButton(getActivity(), view);
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

                    // Disable login button
                    ButtonUtils.disableLoginButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
