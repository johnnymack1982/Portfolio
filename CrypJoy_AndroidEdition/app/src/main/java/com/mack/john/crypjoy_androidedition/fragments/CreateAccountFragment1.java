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

import com.mack.john.crypjoy_androidedition.CreateAccountActivity2;
import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.InputUtils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Objects;

public class CreateAccountFragment1 extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {



    // Class properties
    public static final String TAG = "CreateAccountFragment1";

    public static final String EXTRA_FIRST_NAME = "FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "LAST_NAME";

    private String mFirstName;
    private String mLastName;

    private boolean mValidFirstName = false;
    private boolean mValidLastName = false;

    private View mView;



    // System generated methods
    public static CreateAccountFragment1 newInstance() {
        Bundle args = new Bundle();

        CreateAccountFragment1 fragment = new CreateAccountFragment1();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_create_account1, container, false);

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

        // If user clicked the Next button...
        if(view.getId() == R.id.button_next) {

            // If first name and last name input are both valid...
            if(mValidFirstName && mValidLastName) {

                // Create intent to launch Create Account 2 activity
                Intent nextIntent = new Intent(getActivity(), CreateAccountActivity2.class);

                // Pass first and last name into sending intent
                nextIntent.putExtra(EXTRA_FIRST_NAME, mFirstName);
                nextIntent.putExtra(EXTRA_LAST_NAME, mLastName);

                // Launch Create Account 2 activity
                startActivity(nextIntent);
                Objects.requireNonNull(getActivity()).finish();
            }

            // If input is invalid, let the user know
            else {
                Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
            }
        }

        // If user clicked the Cancel button launch the Login activity
        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(cancelIntent);
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        // Reference all unnecessary UI elements
        TextView header1 = mView.findViewById(R.id.header1);
        TextView header2 = mView.findViewById(R.id.header2);
        ImageView heartIcon = mView.findViewById(R.id.heart_icon);
        ImageView background = mView.findViewById(R.id.background);

        // If any input field has focus, hide all unnecessary UI elements
        if((view.getId() == R.id.input_firstName && hasFocus) || (view.getId() == R.id.input_lastName && hasFocus)) {
            header1.setVisibility(View.GONE);
            header2.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
            background.setVisibility(View.INVISIBLE);
        }

        // If no input field is in focus, show all UI elements
        else {
            header1.setVisibility(View.VISIBLE);
            header2.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
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

        // Set Cancel button listener
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        // Set Next button listener
        Button nextButton = view.findViewById(R.id.button_next);
        nextButton.setOnClickListener(this);
    }

    // Custom method to set text change listener
    private void setTextChangeListener(View view) {
        // Set First Name input field listener
        final EditText firstNameInput = view.findViewById(R.id.input_firstName);
        firstNameInput.setOnFocusChangeListener(this);
        firstNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validName(firstNameInput.getText().toString())) {

                    // Turn input field green
                    firstNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Reference input in class properties
                    mFirstName = firstNameInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidFirstName = true;
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    firstNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    // Indicate input is invalid
                    mValidFirstName = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set last name input field listener
        final EditText lastNameInput = view.findViewById(R.id.input_lastName);
        lastNameInput.setOnFocusChangeListener(this);
        lastNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // If input is valid...
                if(InputUtils.validName(lastNameInput.getText().toString())) {

                    // Turn input field green
                    lastNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputValid)));

                    // Reference input in class properties
                    mLastName = lastNameInput.getText().toString().trim();

                    // Indicate input is valid
                    mValidLastName = true;
                }

                // If input is invalid...
                else {

                    // Turn input field red
                    lastNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorInputInvalid)));

                    // Indicate input is invalid
                    mValidLastName = false;
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

                // If keyboard is not open, clear focus on all input fields
                if(!isOpen) {
                    EditText firstNameInput = view.findViewById(R.id.input_firstName);
                    EditText lastNameInput = view.findViewById(R.id.input_lastName);

                    firstNameInput.clearFocus();
                    lastNameInput.clearFocus();
                }
            }
        });
    }
}
