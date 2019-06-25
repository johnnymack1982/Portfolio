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
        // Inflate fragment layout
        View view = inflater.inflate(R.layout.fragment_edit_family, container, false);

        // Reference family photo display
        mFamilyPhoto = view.findViewById(R.id.profile_photo);

        // Disable save button by default
        ButtonUtils.disableSaveButton(getActivity(), view);

        // Load family photo
        AccountUtils.loadAccountPhoto(getActivity(), view);

        // Reference currently logged in accout
        mAccount = AccountUtils.loadAccount(getActivity());

        // Call custom methods to set text and click listeners
        setClickListener(view);
        setTextListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If cancel button is clicked, return to previous activity
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        // If save button is clicked...
        else if(view.getId() == R.id.button_save) {

            // If user entered valid account password...
            if(mPassword.equals(mAccount.getMasterPassword())) {

                // Reference current family photo
                Bitmap photo = ((BitmapDrawable) mFamilyPhoto.getDrawable()).getBitmap();

                // Update account
                AccountUtils.updateAccount(getActivity(), mFamilyName, mStreetAddress, mPostalCode, mNewEmail, mOldEmail, mPassword, photo);

                // Return to previous activity
                getActivity().finish();
            }

            // If user entered invalid password, let them know
            else {
                Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
            }
        }

        // If user clicked change password button, launch change password activity
        else if(view.getId() == R.id.button_change_password) {
            Intent editPasswordIntent = new Intent(getActivity(), EditPasswordActivity.class);
            startActivity(editPasswordIntent);
        }
    }




    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference all buttons in layout
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button saveButton = view.findViewById(R.id.button_save);
        Button changePasswordButton = view.findViewById(R.id.button_change_password);

        // Set click listener on buttons
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        changePasswordButton.setOnClickListener(this);
    }

    // Custom method to set text change listener
    private void setTextListener(final View view) {
        // Reference family details
        mFamilyName = mAccount.getFamilyName();
        mStreetAddress = mAccount.getStreetAddress();
        mPostalCode = mAccount.getPostalCode();
        mNewEmail = mAccount.getMasterEmail();
        mOldEmail = mAccount.getMasterEmail();

        // Set family name input listener
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

                    // If all fields have valid input, enable save button
                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
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

                    // Disable save button
                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set street address input listener
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

                    // If all fields have valid input, enable save button
                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
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

                    // Disable save button
                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set postal code input listener
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

                    // If all fields have valid input, enable save button
                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
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

                    // Disable save button
                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set master email input listener
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

                    // If all fields have valid input, enable save button
                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
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

                    // Disable save button
                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Disable password rules display by default
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
                    mPassword = passwordInput.getText().toString();

                    // If all fields have valid input, enable save buttonn
                    if(mValidFamilyName && mValidStreetAddress && mValidPostalCode && mValidEmail && mValidPassword) {
                        ButtonUtils.enableSaveButton(getActivity(), view);
                    }

                    // Otherwise, disable save button
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

                    // Disable save button
                    ButtonUtils.disableSaveButton(getActivity(), view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
