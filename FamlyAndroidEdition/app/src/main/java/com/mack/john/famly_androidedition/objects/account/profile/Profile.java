package com.mack.john.famly_androidedition.objects.account.profile;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class Profile implements Serializable {



    // Class properties
    String firstName;
    Date dateOfBirth;
    int genderID; // 0 == Male, 1 == Female
    int profilePIN;



    // Constructor
    public Profile(String firstName, Date dateOfBirth, int genderID, int profilePIN) {
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
        this.genderID = genderID;
        this.profilePIN = profilePIN;
    }

    public Profile() {}




    // Getters / Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getGenderID() {
        return genderID;
    }

    public void setGenderID(int genderID) {
        this.genderID = genderID;
    }

    public int getProfilePIN() {
        return profilePIN;
    }

    public void setProfilePIN(int profilePIN) {
        this.profilePIN = profilePIN;
    }



    // Custom methods
    public String getProfileId() {
        return firstName + profilePIN;
    }
}
