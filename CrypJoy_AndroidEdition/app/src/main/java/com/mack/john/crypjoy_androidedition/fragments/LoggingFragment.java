package com.mack.john.crypjoy_androidedition.fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;
import com.mack.john.crypjoy_androidedition.utilities.LocationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class LoggingFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LoggingFragment";

    private View mView;
    private JoyUtils mJoyUtils;

    private Joy mJoy;
    private Joy mLifetimeJoy;

    private ArrayList<Give> mWeeklyGiven;
    private ArrayList<Get> mWeeklyReceived;



    // Constructor
    public LoggingFragment() {}



    // System generated methods
    public static LoggingFragment newInstance() {
        Bundle args = new Bundle();

        LoggingFragment fragment = new LoggingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment layout
        View view = inflater.inflate(R.layout.fragment_logging, container, false);

        // Set the class View property to match this view
        mView = view;

        // Create new JoyUtils object and pass layout view to allow it to reference UI and make
        // any necessary changes
        mJoyUtils = new JoyUtils(getActivity());

        // Reference Joy utilities class
        mJoy = mJoyUtils.getDailyJoy();

        // Load relevant data
        mLifetimeJoy = mJoyUtils.getLifetimeJoy();
        mWeeklyGiven = mJoyUtils.getWeeklyGiven();
        mWeeklyReceived = mJoyUtils.getWeeklyReceived();

        // Call custom method to set click listener for "Give Joy" and "Get Joy" buttons
        setClickListener();

        // Call custom method to check current progress
        // This will cause the UI to react accordingly, altering the progress message and
        // enabling or disabling the buttons depending on the user's current progress
        checkProgress();

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked "Give Joy," call custom utility method to react accordingly
        if (view.getId() == R.id.button_give) {
            giveJoy();
        }

        // If user clicked "Get Joy," call custom utility method to react accordingly
        else if (view.getId() == R.id.button_get) {
            getJoy();
        }
    }



    // Custom methods
    // Custom method to set click listeners for Give and Get buttons
    private void setClickListener() {
        // Reference Give and Get buttons and set their click listeners
        Button giveButton = mView.findViewById(R.id.button_give);
        giveButton.setOnClickListener(this);

        Button getButton = mView.findViewById(R.id.button_get);
        getButton.setOnClickListener(this);
    }

    // Custom method to log joy given
    private void giveJoy() {
        // Create new dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set positive and negative buttons
        builder.setPositiveButton(Objects.requireNonNull(getActivity()).getString(R.string.yes), giveJoyListener);
        builder.setNegativeButton(getActivity().getString(R.string.nope), giveJoyListener);

        // Set title and message
        builder.setTitle(getActivity().getString(R.string.give_joy_dialog));
        builder.setMessage(getActivity().getString(R.string.give_joy_dialog_body));

        // Build and show dialog
        AlertDialog giveJoyDialog = builder.create();
        giveJoyDialog.show();

        // NOTE: Please see the "Dialog Listeners" section at the end of this class to follow the
        // rest of this data trail. These listeners will trigger appropriate actions based on user
        // selection
    }

    // Custom method to log joy received
    private void getJoy() {
        // Create new dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set positive and negative buttons
        builder.setPositiveButton(Objects.requireNonNull(getActivity()).getString(R.string.yes), getJoyListener);
        builder.setNegativeButton(getActivity().getString(R.string.nope), getJoyListener);

        // Set title and message
        builder.setTitle(getActivity().getString(R.string.get_joy_dialog));
        builder.setMessage(getActivity().getString(R.string.get_joy_dialog_body));

        // Build and show dialog
        AlertDialog getJoyDialog = builder.create();
        getJoyDialog.show();

        // NOTE: Please see the "Dialog Listeners" section at the end of this class to follow the
        // rest of this data trail. These listeners will trigger appropriate actions based on user
        // selection
    }

    // Custom method to check user's current progress and react accordingly
    private void checkProgress() {
        // Reference Give and Get buttons from Logging Screen
        Button giveButton = mView.findViewById(R.id.button_give);
        Button getButton = mView.findViewById(R.id.button_get);


        // If the user has reached their current Give goal, disable the Give button
        if(mJoy.getGiveProgress() == mJoy.getGiveGoal()) {
            giveButton.setEnabled(false);
            giveButton.setBackgroundTintList(Objects.requireNonNull(getActivity()).getResources().getColorStateList(R.color.colorTextSecondary));
        }

        // Otherwise, enable the Give button
        else {
            giveButton.setEnabled(true);
            giveButton.setBackgroundTintList(Objects.requireNonNull(getActivity()).getResources().getColorStateList(R.color.colorAccent));
        }


        // If the user has reached the maximum for Get actions, disable the Get button
        if(mJoy.getGetProgress() == 6) {
            getButton.setEnabled(false);
            getButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorTextSecondary));
        }

        // Otherwise, enable the Get button
        else {
            getButton.setEnabled(true);
            getButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorAccent));
        }


        // Reference progress message text view
        TextView headerMessage = mView.findViewById(R.id.header);
        TextView progressMessage = mView.findViewById(R.id.text_progress_message);

        // Define entry animation for view header and start
        Animation headerTextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_text);
        headerTextAnimation.reset();
        headerMessage.startAnimation(headerTextAnimation);

        // Define entry animation for progress message
        Animation progressTextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_score);
        progressTextAnimation.setStartOffset(500);
        progressTextAnimation.reset();


        // If neither Give or Get goals are met, ask the user what action they'd like to perform
        if(giveButton.isEnabled() && getButton.isEnabled()) {
            progressMessage.setText(getActivity().getString(R.string.are_you_doing_something_nice_for_somebody_or_did_they_do_something_nice_for_you));
        }

        // If Give goal is reached but Get limit is not reached, let the user know they can
        // still log Get actions but cannot log more Give actions until their goal increases
        else if(!giveButton.isEnabled() && getButton.isEnabled()) {
            progressMessage.setText(getActivity().getString(R.string.give_disabled_get_enabled));
        }

        // If the Get limit is reached but the Give goal is not, let the user know they might
        // want to think about catching up!
        else if(giveButton.isEnabled() && !getButton.isEnabled()) {
            progressMessage.setText(getActivity().getString(R.string.give_enabled_get_disabled));
        }

        // If both limits have been reached for the day, congratulate the user
        else if(!giveButton.isEnabled() && !getButton.isEnabled()) {
            progressMessage.setText(getActivity().getString(R.string.give_disabled_get_disabled));
        }

        // Start progress message animation
        progressMessage.startAnimation(progressTextAnimation);

        // Reference heart icon and make sure it starts invisible
        ImageView heartIcon = mView.findViewById(R.id.image_heart);
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



    // Dialog Listeners
    // Click listener for dialog that appears when user clicks the "Give Joy" button
    private final DialogInterface.OnClickListener giveJoyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Only perform an action if the user confirms they wish to proceed
            if(which == -1) {
                // Call custom method to calculate effects of a new Give action being performed
                // This is called on both the daily and lifetime Joy objects
                mJoy.joyGiven();
                mLifetimeJoy.joyGiven();

                // Reference current date
                Date date = new Date();

                LocationUtils locationUtils = new LocationUtils(Objects.requireNonNull(getActivity()));

                // Create new Give object and add it to the current list of Joy Given actions
                Give give = new Give(date, locationUtils.getLatitude(), locationUtils.getLongitude(), getActivity());
                mWeeklyGiven.add(give);

                // Call custom method to save current progress
                mJoyUtils.saveProgress();

                // Let the user know their action has been successfully logged by the app
                Toast.makeText(getActivity(), getActivity().getString(R.string.joy_given_confirm), Toast.LENGTH_SHORT).show();

                getActivity().finish();
            }
        }
    };

    // Click listener for dialog that appears when the user clicks the "Get Joy" button
    private final DialogInterface.OnClickListener getJoyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Only perform an action of the user confirms they wish to proceed
            if(which == -1) {
                // Call custom method to calculate effects of a new Get action being performed
                // This is called on both the daily and lifetime Joy objects
                mJoy.joyReceived();
                mLifetimeJoy.joyReceived();

                // Reference current date
                Date date = new Date();

                LocationUtils locationUtils = new LocationUtils(Objects.requireNonNull(getActivity()));

                // Create new Receive object and add it to the current list of Joy Received actions
                Get get = new Get(date, locationUtils.getLatitude(), locationUtils.getLongitude(), getActivity());
                mWeeklyReceived.add(get);

                // Call custom method to save current progress
                mJoyUtils.saveProgress();

                // Let the user know their action has been successfully logged by the app
                Toast.makeText(getActivity(), getActivity().getString(R.string.joy_received_confirm), Toast.LENGTH_SHORT).show();

                getActivity().finish();
            }
        }
    };
}
