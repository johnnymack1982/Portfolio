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
import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.User;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class CreateAccountFragment2 extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "CreateAccountFragment2";

    String mFirstName;
    String mLastName;
    String mEmail;
    String mPassword;

    private boolean mValidEmail = false;
    private boolean mValidPassword = false;
    private boolean mValidPasswordConfirm = false;

    View mView;

    FirebaseAuth mAuth;



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
        View view = inflater.inflate(R.layout.fragment_create_account2, container, false);

        mView = view;
        mAuth = FirebaseAuth.getInstance();

        animateHeader();
        setClickListener(view);
        setTextChangedListener(view);
        setKeyboardListener(view);
        getUserName(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_finish) {
            if(mValidEmail && mValidPassword && mValidPasswordConfirm) {
                Button finishButton = mView.findViewById(R.id.button_finish);
                Button cancelButton = mView.findViewById(R.id.button_cancel);
                TextView passwordRules = mView.findViewById(R.id.text_passwordInstructions);
                ProgressBar createProgress = mView.findViewById(R.id.progress_create_user);

                finishButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                passwordRules.setVisibility(View.GONE);
                createProgress.setVisibility(View.VISIBLE);

                createAccount();
            }

            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(cancelIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        ImageView background = mView.findViewById(R.id.background);
        TextView  passwordInstructions = mView.findViewById(R.id.text_passwordInstructions);
        EditText emailInput = mView.findViewById(R.id.input_email);
        EditText passwordInput = mView.findViewById(R.id.input_password);
        EditText passwordConfirmInput = mView.findViewById(R.id.input_passwordConfirm);
        Guideline horizontalGuideline = mView.findViewById(R.id.guideline_horizontal);

        if((view.getId() == R.id.input_email && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
            passwordInstructions.setVisibility(View.GONE);

            emailInput.setVisibility(View.VISIBLE);
            passwordInput.setVisibility(View.VISIBLE);
            passwordConfirmInput.setVisibility(View.GONE);
        }

        else if((view.getId() == R.id.input_password && hasFocus) || (view.getId() == R.id.input_passwordConfirm && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
            passwordInstructions.setVisibility(View.GONE);

            horizontalGuideline.setGuidelinePercent(0.30f);

            emailInput.setVisibility(View.GONE);
            passwordInput.setVisibility(View.VISIBLE);
            passwordConfirmInput.setVisibility(View.VISIBLE);
        }

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
    // Custom methods
    private void animateHeader() {
        TextView header1 = mView.findViewById(R.id.header1);
        Animation header1TextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        header1TextAnimation.reset();
        header1.startAnimation(header1TextAnimation);

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

    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        Button finishButton = view.findViewById(R.id.button_finish);
        finishButton.setOnClickListener(this);

    }

    private void setTextChangedListener(View view) {
        final EditText emailInput = view.findViewById(R.id.input_email);
        emailInput.setOnFocusChangeListener(this);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.validEmail(emailInput.getText().toString())) {
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    mEmail = emailInput.getText().toString().trim();

                    mValidEmail = true;
                }

                else {
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));
                    mValidEmail = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final TextView passwordRules = view.findViewById(R.id.text_password_rules);
        passwordRules.setVisibility(View.GONE);

        final EditText passwordInput = view.findViewById(R.id.input_password);
        passwordInput.setOnFocusChangeListener(this);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
            public void afterTextChanged(Editable s) {}
        });

        final TextView passwordConfirmRules = view.findViewById(R.id.text_password_match_rules);
        passwordConfirmRules.setVisibility(View.GONE);

        final EditText passwordConfirmInput = view.findViewById(R.id.input_passwordConfirm);
        passwordConfirmInput.setOnFocusChangeListener(this);
        passwordConfirmInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.passwordsMatch(passwordInput.getText().toString(), passwordConfirmInput.getText().toString())) {
                    passwordConfirmInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    passwordConfirmRules.setVisibility(View.GONE);
                    mPassword = passwordInput.getText().toString().trim();

                    mValidPasswordConfirm = true;
                }

                else {
                    passwordConfirmInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));
                    passwordConfirmRules.setVisibility(View.VISIBLE);

                    mValidPasswordConfirm = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setKeyboardListener(View v) {
        final View view = v;

        KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(!isOpen) {
                    EditText emailInput = view.findViewById(R.id.input_email);
                    EditText passwordInput = view.findViewById(R.id.input_password);
                    EditText passwordConfirmInput = view.findViewById(R.id.input_passwordConfirm);

                    emailInput.clearFocus();
                    passwordInput.clearFocus();
                    passwordConfirmInput.clearFocus();
                }
            }
        });
    }

    private void getUserName(View view) {
        Bundle extras = getActivity().getIntent().getExtras();

        mFirstName = extras.getString(CreateAccountFragment1.EXTRA_FIRST_NAME);
        mLastName = extras.getString(CreateAccountFragment1.EXTRA_LAST_NAME);

        String welcome1 = "Hey, ";
        String welcome2 = "! Now, let's finish up. Easy right?";

        TextView welcomeText = view.findViewById(R.id.text_welcome);
        welcomeText.setText(welcome1 + mFirstName + welcome2);
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();

                    if(user != null) {
                        User newUser = new User(mFirstName, mLastName, mEmail, user.getUid());
                        FirebaseUtils.createUser(newUser, getActivity());
                    }
                }

                else {
                    Toast.makeText(getActivity(), getString(R.string.account_create_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
