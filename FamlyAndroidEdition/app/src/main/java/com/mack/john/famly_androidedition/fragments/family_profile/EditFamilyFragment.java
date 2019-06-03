package com.mack.john.famly_androidedition.fragments.family_profile;

import android.content.Intent;
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
import com.mack.john.famly_androidedition.family_profile.EditPasswordActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.ButtonUtils;
import com.mack.john.famly_androidedition.utils.EditTextUtils;
import com.mack.john.famly_androidedition.utils.InputUtils;

public class EditFamilyFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "EditFamilyFragment";

    Account mAccount;

    ImageView mFamilyPhoto;

    String mFamilyName;
    String mStreetAddress;
    int mPostalCode;
    String mNewEmail;
    String mOldEmail;
    String mPassword;

    boolean mValidFamilyName = true;
    boolean mValidStreetAddress = true;
    boolean mValidPostalCode = true;
    boolean mValidEmail = true;
    boolean mValidPassword = false;



    // System generated methods
    public static EditFamilyFragment newInstance() {
        Bundle args = new Bundle();

        EditFamilyFragment fragment = new EditFamilyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_family, container, false);

        mFamilyPhoto = view.findViewById(R.id.profile_photo);

        ButtonUtils.disableSaveButton(getActivity(), view);

        AccountUtils.loadAccountPhoto(getActivity(), view);

        mAccount = AccountUtils.loadAccount(getActivity());

        setClickListener(view);
        setTextListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_save) {
            if(mPassword.equals(mAccount.getMasterPassword())) {
                Bitmap photo = ((BitmapDrawable) mFamilyPhoto.getDrawable()).getBitmap();

                AccountUtils.updateAccount(getActivity(), mFamilyName, mStreetAddress, mPostalCode, mNewEmail, mOldEmail, mPassword, photo);

                getActivity().finish();
            }

            else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            }
        }

        else if(view.getId() == R.id.button_change_password) {
            Intent editPasswordIntent = new Intent(getActivity(), EditPasswordActivity.class);
            startActivity(editPasswordIntent);
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button saveButton = view.findViewById(R.id.button_save);
        Button changePasswordButton = view.findViewById(R.id.button_change_password);

        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
    }

    private void setTextListener(final View view) {
        mFamilyName = mAccount.getFamilyName();
        mStreetAddress = mAccount.getStreetAddress();
        mPostalCode = mAccount.getPostalCode();
        mNewEmail = mAccount.getMasterEmail();
        mOldEmail = mAccount.getMasterEmail();

        final EditText familyNameInput = view.findViewById(R.id.input_family_name);
        familyNameInput.setText(mAccount.getFamilyName());
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

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), familyNameInput);

                    // Indicate input is invalid
                    mValidFamilyName = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText streetAddressInput = view.findViewById(R.id.input_address);
        streetAddressInput.setText(mAccount.getStreetAddress());
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

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), streetAddressInput);

                    // Indicate input is invalid
                    mValidStreetAddress = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText postalCodeInput = view.findViewById(R.id.input_postal_code);
        postalCodeInput.setText(String.valueOf(mAccount.getPostalCode()));
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

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), postalCodeInput);

                    // Indicate input is invalid
                    mValidPostalCode = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final EditText masterEmailInput = view.findViewById(R.id.input_master_email);
        masterEmailInput.setText(mAccount.getMasterEmail());
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
                    mNewEmail = masterEmailInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidEmail = true;

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
                    }
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    EditTextUtils.turnRed(getActivity(), masterEmailInput);

                    // Indicate input is invalid
                    mValidEmail = false;

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

                    mPassword = passwordInput.getText().toString();

                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    else {
                        ButtonUtils.disableSaveButton(getActivity(), view);
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

                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
