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
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.LoginActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Objects;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "ForgotPasswordFragment";

    private View mView;

    private String mEmail = "";

    private boolean mValidEmail = false;



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

        // Reference layout in class properties
        mView = view;

        // Call custom method to animate header
        animateHeader();

        // Call custom methods to set click, text change, and keyboard listeners
        setClickListener(view);
        setTextChangeListener(view);
        setKeyboardListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {

        // If the user clicked the Send button...
        if(view.getId() == R.id.button_send) {

            // If email input is valid...
            if(mValidEmail) {

                // Let them know a password reset email will be sent
                Toast.makeText(getContext(), getString(R.string.reset_email_sent), Toast.LENGTH_LONG).show();

                // Call Firebase method to send password reset email
                FirebaseUtils.forgotPassword(mEmail);

                // Launch Login activity
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                Objects.requireNonNull(getActivity()).finish();
            }

            // If input is invalid, let the user know
            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        // If the user clicked the Cancel button, launch the Login activity
        else if(view.getId() == R.id.button_cancel) {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        // Reference unnecessary UI elements
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        TextView welcomeText = mView.findViewById(R.id.text_welcome);
        ImageView background = mView.findViewById(R.id.background);

        // If an input field is in focus, hide all unnecessary UI elements
        if((view.getId() == R.id.input_email && hasFocus) || (view.getId() == R.id.input_password && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            welcomeText.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
        }

        // If no input field is in focus, show all UI elements
        else {
            header1.setVisibility(View.VISIBLE);
            header2.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
            welcomeText.setVisibility(View.VISIBLE);
            background.setVisibility(View.VISIBLE);
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
        // Set Send button listener
        Button sendButton = view.findViewById(R.id.button_send);
        sendButton.setOnClickListener(this);

        // Set Cancel button listener
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
    }

    // Custom method to set text changed listener
    private void setTextChangeListener(View view) {

        // Set Email input listener
        final EditText emailInput = view.findViewById(R.id.input_email);
        emailInput.setOnFocusChangeListener(this);
        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

                // If input is a valid email address...
                if(InputUtils.validEmail(emailInput.getText().toString())) {

                    // Turn input field green
                    emailInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Reference input in class properties
                    mEmail = emailInput.getText().toString();

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
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {}

        });
    }

    // Custom method to set keyboard listener
    private void setKeyboardListener(View v) {
        final View view = v;

        // Set keyboard listener
        KeyboardVisibilityEvent.setEventListener(Objects.requireNonNull(getActivity()), new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

                // If keyboard is not open, clear focus on all input fields
                if (!isOpen) {
                    EditText emailInput = view.findViewById(R.id.input_email);

                    emailInput.clearFocus();
                }
            }
        });
    }
}
