package com.mack.john.crypjoy_androidedition.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.User;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Objects;

public class CreateAccountFragment2 extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "CreateAccountFragment2";

    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mPassword;

    private boolean mValidEmail = false;
    private boolean mValidPassword = false;
    private boolean mValidPasswordConfirm = false;

    private View mView;

    private FirebaseAuth mAuth;



    // System generaged methods
    public static CreateAccountFragment2 newInstance() {
        Bundle args = new Bundle();

        CreateAccountFragment2 fragment = new CreateAccountFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_create_account2, container, false);

        // Reference layout in class properties
        mView = view;

        // Instantiate FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();

        // Call custom method to animate header
        animateHeader();

        // Call custom methods to set click, text change, and keyboard listeners
        setClickListener(view);
        setTextChangedListener(view);
        setKeyboardListener(view);

        // Call custom method to get current user's name
        getUserName(view);

        return view;
    }

    @Override
    public void onClick(View view) {

        // If user clicked the Finish button...
        if(view.getId() == R.id.button_finish) {

            // If email, password, and password confirmation input is valid...
            if(mValidEmail && mValidPassword && mValidPasswordConfirm) {

                // Reference UI elements to be hidden
                Button finishButton = mView.findViewById(R.id.button_finish);
                Button cancelButton = mView.findViewById(R.id.button_cancel);
                TextView passwordRules = mView.findViewById(R.id.text_passwordInstructions);

                // Reference progress bar
                ProgressBar createProgress = mView.findViewById(R.id.progress_create_user);

                // Hide necessary UI elements
                finishButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                passwordRules.setVisibility(View.GONE);

                // Show progress bar
                createProgress.setVisibility(View.VISIBLE);

                // Call custom method to create new user account
                createAccount();
            }

            // If input is invalid, let the user know
            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        // If user clicked the Cancel button, launch the Login activity
        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(cancelIntent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        // Reference UI elements to be manipulated
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        ImageView background = mView.findViewById(R.id.background);
        TextView  passwordInstructions = mView.findViewById(R.id.text_passwordInstructions);
        EditText emailInput = mView.findViewById(R.id.input_email);
        EditText passwordInput = mView.findViewById(R.id.input_password);
        EditText passwordConfirmInput = mView.findViewById(R.id.input_passwordConfirm);
        Guideline horizontalGuideline = mView.findViewById(R.id.guideline_horizontal);

        // If the email input field is in focus...
        if((view.getId() == R.id.input_email && hasFocus)) {

            // Hide all unnecessary UI elements, including the password confirmation input field
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
            passwordInstructions.setVisibility(View.GONE);
            passwordConfirmInput.setVisibility(View.GONE);

            // Make sure email and password input fields are visible
            emailInput.setVisibility(View.VISIBLE);
            passwordInput.setVisibility(View.VISIBLE);
        }

        // If password or password confirmation input fields are in focus...
        else if((view.getId() == R.id.input_password && hasFocus) || (view.getId() == R.id.input_passwordConfirm && hasFocus)) {
            // Hide all unnecessary UI elements, including the email input field
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
            passwordInstructions.setVisibility(View.GONE);
            emailInput.setVisibility(View.GONE);

            // Shift password input field up by 20% of visible screen
            horizontalGuideline.setGuidelinePercent(0.30f);

            // Make sure password and password confirmation input fields are visible
            passwordInput.setVisibility(View.VISIBLE);
            passwordConfirmInput.setVisibility(View.VISIBLE);
        }

        // If no input fields are in focus, reset layout to default values
        else {
            header1.setVisibility(View.VISIBLE);
            header2.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
            background.setVisibility(View.VISIBLE);
            passwordInstructions.setVisibility(View.VISIBLE);

            horizontalGuideline.setGuidelinePercent(0.50f);

            emailInput.setVisibility(View.VISIBLE);
            passwordInput.setVisibility(View.VISIBLE);
            passwordConfirmInput.setVisibility(View.VISIBLE);
        }
    }



    // Custom methods
    // Custom method to animate header
    private void animateHeader() {

        // Animate left side of header
        TextView header1 = mView.findViewById(R.id.header1);
        Animation header1TextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        header1TextAnimation.reset();
        header1.startAnimation(header1TextAnimation);

        // Animate right side of header
        TextView header2 = mView.findViewById(R.id.header2);
        Animation header2TextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        header2TextAnimation.reset();
        header2.startAnimation(header2TextAnimation);

        // Reference heart icon and make sure it starts invisible
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        heartIcon.setScaleX(0.0f);
        heartIcon.setScaleY(0.0f);

        // Define entry animation parameters for heart icon X axis and start animation
        ObjectAnimator heartIconAnimator1 = ObjectAnimator.ofFloat(heartIcon, "scaleX", 1.0f);
        heartIconAnimator1.setStartDelay(1000);
        heartIconAnimator1.setInterpolator(new BounceInterpolator());
        heartIconAnimator1.setDuration(500);
        heartIconAnimator1.start();

        // Define entry animation parameters for heart icon Y axis and start animation
        ObjectAnimator heartIconAnimator2 = ObjectAnimator.ofFloat(heartIcon, "scaleY", 1.0f);
        heartIconAnimator2.setStartDelay(1000);
        heartIconAnimator2.setInterpolator(new BounceInterpolator());
        heartIconAnimator2.setDuration(500);
        heartIconAnimator2.start();
    }

    // Custom method to set click listener
    private void setClickListener(View view) {
        // Set Cancel button listener
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        // Set Finish button listener
        Button finishButton = view.findViewById(R.id.button_finish);
        finishButton.setOnClickListener(this);

    }

    // Custom method to set text change listener
    private void setTextChangedListener(View view) {

        // Set email input field listener
        final EditText emailInput = view.findViewById(R.id.input_email);
        emailInput.setOnFocusChangeListener(this);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is a valid email address...
                if(InputUtils.validEmail(emailInput.getText().toString())) {

                    // Turn input field green
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Reference input in class properties
                    mEmail = emailInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidEmail = true;
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    // Indicate input is invalid
                    mValidEmail = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set password rules to be invisible by default
        final TextView passwordRules = view.findViewById(R.id.text_password_rules);
        passwordRules.setVisibility(View.GONE);

        // Set password input listener
        final EditText passwordInput = view.findViewById(R.id.input_password);
        passwordInput.setOnFocusChangeListener(this);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If password input is valid...
                if(InputUtils.validPassword(passwordInput.getText().toString())) {

                    // Turn input field green
                    passwordInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Hide password rules
                    passwordRules.setVisibility(View.GONE);

                    // Indicate input is valid
                    mValidPassword = true;
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    passwordInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    // Show password rules
                    passwordRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mValidPassword = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set password confirmation rules to be invisible by default
        final TextView passwordConfirmRules = view.findViewById(R.id.text_password_match_rules);
        passwordConfirmRules.setVisibility(View.GONE);

        // Set password confirmation input listener
        final EditText passwordConfirmInput = view.findViewById(R.id.input_passwordConfirm);
        passwordConfirmInput.setOnFocusChangeListener(this);
        passwordConfirmInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input matches password...
                if(InputUtils.passwordsMatch(passwordInput.getText().toString(), passwordConfirmInput.getText().toString())) {

                    // Turn input field green
                    passwordConfirmInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Hide password confirmation rules
                    passwordConfirmRules.setVisibility(View.GONE);

                    // Reference password confirmation input in class properties
                    mPassword = passwordInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidPasswordConfirm = true;
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    passwordConfirmInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    // Show password confirmation rules
                    passwordConfirmRules.setVisibility(View.VISIBLE);

                    // Indicate input is invalid
                    mValidPasswordConfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Custom method to set keyboard listener
    private void setKeyboardListener(View v) {
        final View view = v;

        // Set keyboard listener
        KeyboardVisibilityEvent.setEventListener(Objects.requireNonNull(getActivity()), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

                // If keyboard is not open...
                if(!isOpen) {
                    // Reference all input fields
                    EditText emailInput = view.findViewById(R.id.input_email);
                    EditText passwordInput = view.findViewById(R.id.input_password);
                    EditText passwordConfirmInput = view.findViewById(R.id.input_passwordConfirm);

                    // Clear focus on all input fields
                    emailInput.clearFocus();
                    passwordInput.clearFocus();
                    passwordConfirmInput.clearFocus();
                }
            }
        });
    }

    // Custom method to get current user's name
    private void getUserName(View view) {

        // Get extras fron sending activity
        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();

        // Get first and last name from sending activity
        mFirstName = Objects.requireNonNull(extras).getString(CreateAccountFragment1.EXTRA_FIRST_NAME);
        mLastName = Objects.requireNonNull(extras).getString(CreateAccountFragment1.EXTRA_LAST_NAME);

        String welcome1 = getString(R.string.welcome1);
        String welcome2 = getString(R.string.welcome2);

        // Build welcome string with user's first name and display on UI
        TextView welcomeText = view.findViewById(R.id.text_welcome);
        welcomeText.setText(welcome1 + " " + mFirstName + welcome2);
    }

    // Custom method to create new user account
    private void createAccount() {

        // Call Firebase method to create new account using user input
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    // Get newly generated user
                    FirebaseUser user = mAuth.getCurrentUser();

                    // If a user exists...
                    if(user != null) {

                        // Begin to generate user information and launch Daily Progress Details activity
                        User newUser = new User(mFirstName, mLastName, mEmail, user.getUid());
                        FirebaseUtils.createUser(newUser, getActivity());
                    }
                }

                // If creation was unsuccessful, let the user know
                else {
                    Toast.makeText(getActivity(), getString(R.string.account_create_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
