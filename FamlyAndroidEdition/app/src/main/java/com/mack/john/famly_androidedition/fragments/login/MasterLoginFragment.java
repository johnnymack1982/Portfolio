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
        View view = inflater.inflate(R.layout.fragment_login_master, container, false);

        ButtonUtils.disableLoginButton(getActivity(), view);

        setClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_login) {
            AccountUtils.login(getActivity(), mEmail, mPassword);
        }

        else if(view.getId() == R.id.button_forgot_password) {
            Intent forgotPasswordIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
        }

        else if(view.getId() == R.id.button_sign_up) {
            Intent signupIntent = new Intent(getActivity(), SignupMaster1Activity.class);
            startActivity(signupIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button loginButton = view.findViewById(R.id.button_login);
        Button forgotPasswordButton = view.findViewById(R.id.button_forgot_password);
        Button signupButton = view.findViewById(R.id.button_sign_up);

        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    private void setTextChangeListener( final View view) {
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

                    if(mValidEmail && mValidPassword) {
                        ButtonUtils.enableLoginButton(getActivity(), view);
                    }

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

                    ButtonUtils.disableLoginButton(getActivity(), view);
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

                    mPassword = passwordInput.getText().toString().trim();

                    if(mValidEmail && mValidPassword) {
                        ButtonUtils.enableLoginButton(getActivity(), view);
                    }

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

                    ButtonUtils.disableLoginButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
