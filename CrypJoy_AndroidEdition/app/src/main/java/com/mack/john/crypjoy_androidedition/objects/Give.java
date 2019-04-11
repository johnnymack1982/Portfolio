package com.mack.john.crypjoy_androidedition.objects;

import android.content.Context;

import com.mack.john.crypjoy_androidedition.R;

import java.util.Date;

public class Give extends JoyAction {
    
    
    
    // Class properties
    String actionName;
    
    
    
    // Constructor
    public Give(Date dateCreated, Context context) {
        super(dateCreated);
        this.actionName = context.getString(R.string.joy_given_small);
    }



    // Getters / Setters
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
