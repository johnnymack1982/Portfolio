package com.mack.john.famly_androidedition.objects.account;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.mack.john.famly_androidedition.objects.account.profile.Profile;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {



    // Class properties
    String familyName;
    String streetAddress;
    int postalCode;
    String masterEmail;
    String masterPassword;
    ArrayList<Profile> profiles;



    // Constructor
    public Account(String familyName, String streetAddress, int postalCode, String masterEmail, String masterPassword) {
        this.familyName = familyName;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.masterEmail = masterEmail;
        this.masterPassword = masterPassword;
        this.profiles = new ArrayList<>();
    }




    // Getters / Setter
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getMasterEmail() {
        return masterEmail;
    }

    public void setMasterEmail(String masterEmail) {
        this.masterEmail = masterEmail;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public ArrayList<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(ArrayList profiles) {
        this.profiles = profiles;
    }



    // Custom methods
    public void addProfile(Profile profile) {
        this.profiles.add(profile);
    }
}
