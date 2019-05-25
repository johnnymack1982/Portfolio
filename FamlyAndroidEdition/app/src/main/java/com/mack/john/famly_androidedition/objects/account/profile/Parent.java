package com.mack.john.famly_androidedition.objects.account.profile;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Parent extends Profile {



    // Class properties
    int roleID; // 0 == Mother, 1 == Father, 2 == Grandmother, 3 == Grandfather, 4 == Aunt, 5 == Uncle



    // Constructors
    public Parent(String firstName, Date dateOfBirth, int genderID, int profilePIN, int roleID) {
        super(firstName, dateOfBirth, genderID, profilePIN);
        this.roleID = roleID;
    }



    // Getters / Setters
    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
}
