package com.mack.john.crypjoy_androidedition.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtils {



    // Class properties



    // Custom methods
    public static boolean validEmail(String email) {
        if (email == null || email.trim().equals("")) {
            return false;
        }

        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean validPassword(String password) {
        if(password == null || password.trim().equals("")) {
            return false;
        }

        else {
            String patternString = "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$";

            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(password.trim());

            return matcher.matches();
        }
    }

    public static boolean validName(String name) {
        if(name == null || name.trim().equals("")) {
            return false;
        }

        else {
            return true;
        }
    }

    public static boolean passwordsMatch(String password, String passwordConfirm) {
        if(password == null || password.equals("") || passwordConfirm == null || passwordConfirm.equals("")) {
            return false;
        }

        else if(!passwordConfirm.equals(password)) {
            return false;
        }

        else {
            return true;
        }
    }
}
