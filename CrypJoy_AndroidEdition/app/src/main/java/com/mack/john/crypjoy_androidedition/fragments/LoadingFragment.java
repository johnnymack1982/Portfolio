package com.mack.john.crypjoy_androidedition.fragments;

import android.animation.ObjectAnimator;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.utilities.FirebaseUtils;

public class LoadingFragment extends Fragment {



    // Class properties
    public static final String TAG = "LoadingFragment";



    // System generated methods
    public static LoadingFragment newInstance() {
        Bundle args = new Bundle();

        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        // Call custom methods to animate header and check existing user
        animateHeader(view);
        checkExistingUser();

        return view;
    }



    // Custom methods
    // Custom method to animate header
    private void animateHeader(View view) {

        // Animate left side of header
        TextView header1 = view.findViewById(R.id.header1);
        Animation header1TextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
        header1TextAnimation.reset();
        header1.startAnimation(header1TextAnimation);

        // Animate right side of header
        TextView header2 = view.findViewById(R.id.header2);
        Animation header2TextAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        header2TextAnimation.reset();
        header2.startAnimation(header2TextAnimation);

        // Reference heart icon and make sure it starts invisible
        ImageView heartIcon = view.findViewById(R.id.heart_icon);
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

        ProgressBar loadingProgress = view.findViewById(R.id.progress_loading);
        Animation progressAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_score);
        progressAnimation.setStartOffset(1500);
        loadingProgress.startAnimation(progressAnimation);
    }

    // Custom method to check existing user
    private void checkExistingUser() {

        // Check for existing usero
        FirebaseUtils.checkForAuth(getActivity());
    }
}
