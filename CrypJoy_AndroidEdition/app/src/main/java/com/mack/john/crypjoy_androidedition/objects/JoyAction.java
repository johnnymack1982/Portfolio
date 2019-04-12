package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JoyAction implements Serializable {



    // Class properties
    private final Date dateCreated;



    // Constructor
    JoyAction(Date dateCreated) {
        this.dateCreated = dateCreated;
    }



    // Getters / Setters
    public Date getDateCreated() {
        return dateCreated;
    }


    // Custom methods
    // Custom method to format and display creation date for current object
    // This method will be implemented at a later stage in the development cycle
    public String displayDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(this.dateCreated);
    }

    // Custom method to format and display creation time for current object
    // This method will be implemented at a later stage in the development cycle
    public String displayTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(this.dateCreated);
    }
}
