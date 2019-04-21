package com.mack.john.crypjoy_androidedition.utilities;

import android.animation.ObjectAnimator;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.BounceInterpolator;

public class AddButtonUtils {


    // Custom methods
    // Custom method to hide Add button with animation
    public void hide(FloatingActionButton addButton) {
        // Define animation parameters for X axis and start
        ObjectAnimator addButtonAnimator1 = ObjectAnimator.ofFloat(addButton, "scaleX", 0.0f);
        addButtonAnimator1.setStartDelay(2000);
        addButtonAnimator1.setDuration(500);
        addButtonAnimator1.start();

        // Define animation parameters for Y axis and start
        ObjectAnimator addButtonAnimator2 = ObjectAnimator.ofFloat(addButton, "scaleY", 0.0f);
        addButtonAnimator2.setStartDelay(2000);
        addButtonAnimator2.setDuration(500);
        addButtonAnimator2.start();
    }

    // Custom method to show Add button with animation
    public void show(FloatingActionButton addButton) {
        // Make sure button starts as hidden
        addButton.setScaleX(0.0f);
        addButton.setScaleY(0.0f);

        // Define parameters for X axis and start
        ObjectAnimator addButtonAnimator1 = ObjectAnimator.ofFloat(addButton, "scaleX", 1.0f);
        addButtonAnimator1.setStartDelay(2000);
        addButtonAnimator1.setInterpolator(new BounceInterpolator());
        addButtonAnimator1.setDuration(500);
        addButtonAnimator1.start();

        // Define parameters for Y axis and start
        ObjectAnimator addButtonAnimator2 = ObjectAnimator.ofFloat(addButton, "scaleY", 1.0f);
        addButtonAnimator2.setStartDelay(2000);
        addButtonAnimator2.setInterpolator(new BounceInterpolator());
        addButtonAnimator2.setDuration(500);
        addButtonAnimator2.start();
    }
}
