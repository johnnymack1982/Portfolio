package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.Button;

import com.mack.john.famly_androidedition.R;

public class ButtonUtils {



    // Custom methods
    // Custom method to enable continue button
    public static void enableContinueButton(Context context, View view) {
        Button continueButton = view.findViewById(R.id.button_continue);
        continueButton.setEnabled(true);
        continueButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrassPrimary)));
    }

    // Custom method to disable continue button
    public static void disableContinueButton(Context context, View view) {
        Button continueButton = view.findViewById(R.id.button_continue);
        continueButton.setEnabled(false);
        continueButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextLight)));
    }

    // Custom method to enable parent button
    public static void enableParentButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_add_parent);
        addParentButton.setEnabled(true);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrassPrimary)));
    }

    // Custom method to disable parent button
    public static void disableParentButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_add_parent);
        addParentButton.setEnabled(false);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextLight)));
    }

    // Custom method to enable login button
    public static void enableLoginButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_login);
        addParentButton.setEnabled(true);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrassPrimary)));
    }

    // Custom method to disable login button
    public static void disableLoginButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_login);
        addParentButton.setEnabled(false);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextLight)));
    }

    // Custom method to enable send button
    public static void enableSendButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_send);
        addParentButton.setEnabled(true);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrassPrimary)));
    }

    // Custom method to disable send button
    public static void disableSendButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_send);
        addParentButton.setEnabled(false);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextLight)));
    }

    // Custom method to enable save button
    public static void enableSaveButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_save);
        addParentButton.setEnabled(true);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorGrassPrimary)));
    }

    // Custom method to disable save button
    public static void disableSaveButton(Context context, View view) {
        Button addParentButton = view.findViewById(R.id.button_save);
        addParentButton.setEnabled(false);
        addParentButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorTextLight)));
    }
}
