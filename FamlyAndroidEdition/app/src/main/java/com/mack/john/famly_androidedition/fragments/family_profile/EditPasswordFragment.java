package com.mack.john.famly_androidedition.fragments.family_profile;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class EditPasswordFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "EditPasswordFragment";

    Account mAccount;

    ImageView mFamilyPhoto;

    String mOldPassword;
    String mNewPassword;

    boolean mValidOldPassword = false;
    boolean mValidNewPassword = false;
    boolean mNewPasswordsMatch = false;



    // System generated methods
    public static EditPasswordFragment newInstance() {
        Bundle args = new Bundle();

        EditPasswordFragment fragment = new EditPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        mAccount = AccountUtils.loadAccount(getActivity());

        mFamilyPhoto = view.findViewById(R.id.profile_photo);
        AccountUtils.loadAccountPhoto(getActivity(), view);

        setOnClickListener(view);
        setTextChangeListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_save) {
            if(mOldPassword.equals(mAccount.getMasterPassword())) {
                Bitmap photo = ((BitmapDrawable) mFamilyPhoto.getDrawable()).getBitmap();

                AccountUtils.updatePassword(getActivity(), mOldPassword, mNewPassword, mAccount, photo);
            }

            else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            }
        }
    }



    // Custom methods
    private void setOnClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button saveButton = view.findViewById(R.id.button_save);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void setTextChangeListener(final View view) {
        final TextView passwordRules = view.findViewById(R.id.display_password_rules);
        passwordRules.setVisibility(View.GONE);

        final EditText oldPasswordInput = view.findViewById(R.id.input_old_master_password);
        oldPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If password input is valid...
                if(InputUtils.validPassword(oldPasswordInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), oldPasswordInput);

                    // Hide password rules
                    passwordRules.setVisibility(View.GONE);

                    // Indicate input is valid
                    mValidOldPassword = true;

                    mOldPassword = oldPasswordInput.getText().toString();

                    if(mValidOldPassword && mValidNewPassword && mNewPasswordsMatch) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), oldPasswordInput);

                    // Show password rules
                    passwordRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mValidOldPassword = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText newPasswordInput = view.findViewById(R.id.input_new_master_password);
        newPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If password input is valid...
                if(InputUtils.validPassword(newPasswordInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), newPasswordInput);

                    // Hide password rules
                    passwordRules.setVisibility(View.GONE);

                    // Indicate input is valid
                    mValidNewPassword = true;

                    if(mValidOldPassword && mValidNewPassword && mNewPasswordsMatch) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), newPasswordInput);

                    // Show password rules
                    passwordRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mValidNewPassword = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText confirmNewPasswordInput = view.findViewById(R.id.input_confirm_new_master_password);
        confirmNewPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If password input is valid...
                if(InputUtils.validPassword(confirmNewPasswordInput.getText().toString())) {

                    // Turn input field green
                    EditTextUtils.turnGreen(getActivity(), confirmNewPasswordInput);

                    // Hide password rules
                    passwordRules.setVisibility(View.GONE);

                    // Indicate input is valid
                    mNewPasswordsMatch = true;

                    mNewPassword = newPasswordInput.getText().toString().trim();

                    if(mValidOldPassword && mValidNewPassword && mNewPasswordsMatch) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), confirmNewPasswordInput);

                    // Show password rules
                    passwordRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mNewPasswordsMatch = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
