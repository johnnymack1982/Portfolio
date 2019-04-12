package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;
import java.util.Date;

public class Joy implements Serializable {



    // Class properties
    private int giveGoal;
    private int giveProgress;
    private int getProgress;
    private int payItForwardGoal;
    private int payItForwardProgress;
    private final Date dateCreated;



    // Constructor
    public Joy(Date dateCreated) {
        this.giveGoal = 3;
        this.giveProgress = 0;

        this.getProgress = 0;

        this.payItForwardGoal = 0;
        this.payItForwardProgress = 0;

        this.dateCreated = dateCreated;
    }



    // Getters / Setters
    public int getGiveGoal() {
        return giveGoal;
    }

    public int getGiveProgress() {
        return giveProgress;
    }

    public int getGetProgress() {
        return getProgress;
    }

    public Date getDateCreated() {
        return dateCreated;
    }


    // Custom methods
    // Custom method to be called when the user logs that they have completed a random act of kindness
    public void joyGiven() {
        // Increase Give Joy progress by one
        giveProgress ++;

        // Increase Pay It Forward progress by one, only if the new value will not exceed the current
        // Pay It Forward goal
        if(payItForwardProgress + 1 <= payItForwardGoal) {
            payItForwardProgress ++;
        }
    }

    // Custom method to be called when the user logs that they have received a random act of kindness
    public void joyReceived() {
        // Increase Get Joy progress by one
        getProgress ++;

        // Increase Pay It Forward goal by one
        payItForwardGoal ++;

        // Calculate new Give Joy goal
        giveGoal = 3 + payItForwardGoal;
    }

    // Custom method to be called when the UI needs to display the users current Give Joy progress
    public String displayGiven() {
        String goalString = String.valueOf(giveGoal);
        String progressString = String.valueOf(giveProgress);

        return progressString + "/" + goalString;
    }

    // Custom method to be called when the UI needs to display the user's current Get Joy progress
    public String displayReceived() {
        return String.valueOf(getProgress);
    }

    // Custom method to be called when the UI needs to display the user's current Pay It Forward progress
    public String displayPayItForward() {
        String goalString = String.valueOf(payItForwardGoal);
        String progressString = String.valueOf(payItForwardProgress);

        return progressString + "/" + goalString;
    }
}
