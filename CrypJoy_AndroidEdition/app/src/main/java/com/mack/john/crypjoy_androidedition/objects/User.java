package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;

public class User implements Serializable {



    // Class properties
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String uID;



    // Constructor
    public User(String firstName, String lastName, String email, String uID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uID = uID;
    }

    public User() {
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.uID = "";
    }



    // Getters / Setters
    public String getFirstName() {
        return firstName;
    }

    public String getuID() {
        return uID;
    }

}
