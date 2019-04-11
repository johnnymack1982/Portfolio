package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JoyAction implements Serializable {



    // Class properties
    Date dateCreated;



    // Constructor
    public JoyAction(Date dateCreated) {
        this.dateCreated = dateCreated;
    }



    // Getters / Setters
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }



    // Custom methods
    public String displayDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(this.dateCreated);
    }

    public String displayTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        return timeFormat.format(this.dateCreated);
    }
}
