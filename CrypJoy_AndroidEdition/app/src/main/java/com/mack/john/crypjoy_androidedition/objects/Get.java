package com.mack.john.crypjoy_androidedition.objects;

import android.content.Context;

import com.mack.john.crypjoy_androidedition.R;

import java.util.Date;

public class Get extends JoyAction {
    
    
    
    // Class properties
    String actionName;
    
    
    
    // Constructor
    public Get(Date dateCreated, Context context) {
        super(dateCreated);
        this.actionName = context.getString(R.string.joy_received_small);
    }



    // Getters / Setters
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
