package com.mack.john.famly_androidedition.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtils {



    // Custom methods
    // Custom method to check validity of name input
    public static boolean validName(String name) {

        // If name string is empty, it is invalid
        return name != null && !name.trim().equals("");
    }

    // Custom method to check validity of address input
    public static boolean validAddress(String address) {
        // If address string is empty, it is invalid
        if(address == null || address.trim().equals("")) {
            return false;
        }

        else {
            // Address requirements
            String patternString = "^\\d{1,6}\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}" +
                    "\\040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$|^\\d{1,6}\\" +
                    "040([A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,}\\040[A-Z]{1}[a-z]{1,})$";

            // Match password input string to requirements pattern and return result
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(address.trim());
            return matcher.matches();
        }
    }

    // Custom method to check validity of postal code input
    public static boolean validPostalCode(String postalCode) {

        // If input is not 5 digits long, input is invalid
        if(postalCode == null || postalCode.trim().equals("") || postalCode.trim().length() != 5) {
            return false;
        }

        // Otherwise, input is valid
        else {
            return true;
        }
    }

    // Custom method to check email address input
    public static boolean validEmail(String email) {

        // If input string is empty, it is invalid
        if (email == null || email.trim().equals("")) {
            return false;
        }

        // Return value of built-in e-mail pattern matcher
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
        }
    }

    // Custom method to check validity of password input string
    public static boolean validPassword(String password) {

        // If password string is empty, it is invalid
        if(password == null || password.trim().equals("")) {
            return false;
        }

        else {
            // Password requirements
            String patternString = "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$";

            // Match password input string to requirements pattern and return result
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(password.trim());
            return matcher.matches();
        }
    }

    // Custom method to check validity of password confirmation input string
    public static boolean passwordsMatch(String password, String passwordConfirm) {
        // If either input strings are empty, the input is invalid
        if(password == null || password.equals("") || passwordConfirm == null || passwordConfirm.equals("")) {
            return false;
        }

        // If password confirmation string does not match password string, input is invalid
        else return passwordConfirm.equals(password);
    }

    // Custom method to check pin input
    public static boolean validPin(String pin) {

        // If input is blank, it is invalid
        if(pin == null || pin.trim().equals("")) {
            return false;
        }

        // If input is 4 digits long it is valid
        else {
            if(pin.length() == 4) {
                return true;
            }

            // Otherwise, input is invalid
            else {
                return false;
            }
        }
    }

    // Custom method to check if pin/password input matches
    public static boolean pinsMatch(String pin, String pinConfirm) {
        // If either input strings are empty, the input is invalid
        if(pin == null || pin.equals("") || pinConfirm == null || pinConfirm.equals("")) {
            return false;
        }

        // If password confirmation string does not match password string, input is invalid
        else return pinConfirm.equals(pin);
    }
}
