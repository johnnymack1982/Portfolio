package com.mack.john.crypjoy_androidedition.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.CreateAccountActivity1;
import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;

public class LoginFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LoginFragment";

    private boolean mValidEmail = false;
    private boolean mValidPassword = false;



    // System generated methods
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        setClickListener(view);
        setOnKeyListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_login) {
            if(mValidEmail && mValidPassword) {
                Intent loginIntent = new Intent(getActivity(), DailyDetailsActivity.class);
                startActivity(loginIntent);
                getActivity().finish();
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        else if(view.getId() == R.id.button_createAccount) {
            Intent createAccountIntent = new Intent(getActivity(), CreateAccountActivity1.class);
            startActivity(createAccountIntent);
            getActivity().finish();
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        Button createAccountButton = view.findViewById(R.id.button_createAccount);
        createAccountButton.setOnClickListener(this);
    }

    private void setOnKeyListener(final View view) {
        final EditText emailInput = view.findViewById(R.id.input_email);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                if(InputUtils.validEmail(emailInput.getText().toString())) {
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    mValidEmail = true;
                }

                else {
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    mValidEmail = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}

        });

        final TextView passwordRules = view.findViewById(R.id.text_password_rules);
        passwordRules.setVisibility(View.GONE);

        final EditText passwordInput = view.findViewById(R.id.input_password);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.validPassword(passwordInput.getText().toString())) {
                    passwordInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    passwordRules.setVisibility(View.GONE);

                    mValidPassword = true;
                }

                else {
                    passwordInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));
                    passwordRules.setVisibility(View.VISIBLE);

                    mValidPassword = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
