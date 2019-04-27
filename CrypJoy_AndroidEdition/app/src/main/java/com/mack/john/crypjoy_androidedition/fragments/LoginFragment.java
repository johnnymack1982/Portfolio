package com.mack.john.crypjoy_androidedition.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.mack.john.crypjoy_androidedition.CreateAccountActivity1;
import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.User;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class LoginFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "LoginFragment";

    View mView;

    private boolean mValidEmail = false;
    private boolean mValidPassword = false;

    String mEmail;
    String mPassword;



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

        mView = view;

        animateHeader();
        setClickListener(view);
        setTextChangeListener(view);
        setKeyboardListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_login) {
            if(mValidEmail && mValidPassword) {
                Button loginButton = mView.findViewById(R.id.button_login);
                ProgressBar loginProgress = mView.findViewById(R.id.progress_login);

                loginButton.setVisibility(View.GONE);
                loginProgress.setVisibility(View.VISIBLE);

                login();
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

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        ImageView background = mView.findViewById(R.id.background);

        if((view.getId() == R.id.input_email && hasFocus) || (view.getId() == R.id.input_password && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
        }

        else {
            header1.setVisibility(View.VISIBLE);
            header2.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
            background.setVisibility(View.VISIBLE);
        }
    }




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
        Button loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(this);

        Button createAccountButton = view.findViewById(R.id.button_createAccount);
        createAccountButton.setOnClickListener(this);
    }

    private void setTextChangeListener(final View view) {
        final EditText emailInput = view.findViewById(R.id.input_email);
        emailInput.setOnFocusChangeListener(this);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                if(InputUtils.validEmail(emailInput.getText().toString())) {
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    mEmail = emailInput.getText().toString();

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
        passwordInput.setOnFocusChangeListener(this);
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InputUtils.validPassword(passwordInput.getText().toString())) {
                    passwordInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));
                    passwordRules.setVisibility(View.GONE);

                    mPassword = passwordInput.getText().toString();

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

    private void setKeyboardListener(View v) {
        final View view = v;

        KeyboardVisibilityEvent.setEventListener(getActivity(), new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(!isOpen) {
                            EditText emailInput = view.findViewById(R.id.input_email);
                            EditText passwordInput = view.findViewById(R.id.input_password);

                            passwordInput.clearFocus();
                            emailInput.clearFocus();
                        }
                    }
                });
    }

    private void login() {
        FirebaseUtils.login(mEmail, mPassword, getActivity(), mView);
    }
}
