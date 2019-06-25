package com.mack.john.famly_androidedition.fragments.login;

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
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ForgotPasswordFragment";

    String mEmail;

    boolean mValidEmail;



    // System generated methods
    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Disable send button by default
        ButtonUtils.disableSendButton(getActivity(), view);

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

        // if user clicked send password, send password reset email
        else if(view.getId() == R.id.button_send) {
            AccountUtils.resetPassword(getActivity(), mEmail);
        }
    }




    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button sendButton = view.findViewById(R.id.button_send);

        cancelButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }

    // Custom method to set text change listener
    private void setTextChangeListener(final View view) {
        // Set email input listener
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

                    // If input is valid, enable save button
                    if(mValidEmail) {
                        ButtonUtils.enableSendButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
                    else {
                        ButtonUtils.disableSendButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), masterEmailInput);

                    // Indicate input is invalid
                    mValidEmail = false;

                    // Disable save button
                    ButtonUtils.disableSendButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
