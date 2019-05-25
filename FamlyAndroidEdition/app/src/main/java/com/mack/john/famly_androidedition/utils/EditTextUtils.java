package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.EditText;

import com.mack.john.famly_androidedition.R;

public class EditTextUtils {



    // Custom methods
    public static void turnGreen(Context context, EditText editText) {
        editText.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorInputValid)));
    }

    public static void turnRed(Context context, EditText editText) {
        editText.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorInputInvalid)));
    }
}
