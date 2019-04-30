package com.mack.john.crypjoy_androidedition.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtils {



    // Custom methods
    // Custom method to check validity of e-mail input string
    public static boolean validEmail(String email) {

        // If input string is empty, it is invalid
        if (email == null || email.trim().equals("")) {
            return false;
        }

        // Return value of built-in e-mail pattern matcher
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

    // Custom method to check validity of name input string
    public static boolean validName(String name) {

        // If name string is empty, it is invalid
        return name != null && !name.trim().equals("");
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
}
