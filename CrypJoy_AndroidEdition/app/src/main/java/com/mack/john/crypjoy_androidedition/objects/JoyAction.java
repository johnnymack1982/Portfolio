package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JoyAction implements Serializable {



    // Class properties
    private final Date dateCreated;
    private final double latitude;
    private final double longitude;



    // Constructor
    JoyAction(Date dateCreated, double latitude, double longitude) {
        this.dateCreated = dateCreated;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    // Getters / Setters
    public Date getDateCreated() {
        return dateCreated;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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
