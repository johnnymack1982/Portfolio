package com.mack.john.crypjoy_androidedition.objects;

import android.content.Context;

import com.mack.john.crypjoy_androidedition.R;

import java.util.Date;

public class Get extends JoyAction {
    
    
    
    // Class properties
    private final String actionName;
    
    
    
    // Constructor
    public Get(Date dateCreated, Context context) {
        super(dateCreated);
        this.actionName = context.getString(R.string.joy_received_small);
    }



    // Getters / Setters
    public String getActionName() {
        return actionName;
    }

}
