package com.mack.john.crypjoy_androidedition.objects;

import java.io.Serializable;

public class User implements Serializable {



    // Class properties
    String firstName;
    String lastName;
    String email;
    String uID;



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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
