package com.mack.john.crypjoy_androidedition.objects;

import android.content.Context;

import com.mack.john.crypjoy_androidedition.R;

import java.util.Date;

public class Give extends JoyAction {
    
    
    
    // Class properties
    private final String actionName;
    
    
    
    // Constructor
    public Give(Date dateCreated, double latitude, double longitude, Context context) {
        super(dateCreated, latitude, longitude);
        this.actionName = context.getString(R.string.joy_given_small);
    }




    // Getters / Setters
    public String getActionName() {
        return actionName;
    }

}
