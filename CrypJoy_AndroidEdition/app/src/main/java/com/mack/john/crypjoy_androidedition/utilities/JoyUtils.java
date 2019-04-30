package com.mack.john.crypjoy_androidedition.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JoyUtils {


    private static final String JOY_DATA = "joy.joy";
    private static final String LIFETIME_JOY_DATA = "lifetimejoy.joy";
    private static final String WEEKLY_GIVEN_DATA = "weeklygiven.joy";
    private static final String WEEKLY_RECEIVED_DATA = "weeklyreceived.joy";

    private final Context mContext;

    private Joy mJoy;
    private Joy mLifetimeJoy;
    private ArrayList<Give> mWeeklyGiven;
    private ArrayList<Get> mWeeklyReceived;



    // Constructor
    public JoyUtils(Context context) {
        // Reference context from sending activity
        this.mContext = context;
    }




    // Custom methods
    // ** DATA STORAGE METHODS **
    // Custom method to save current progress
    public void saveProgress(Joy dailyJoy, Joy lifetimeJoy, ArrayList<Give> weeklyGiven, ArrayList<Get> weeklyReceived) {
        if(dailyJoy != null && lifetimeJoy != null && weeklyGiven != null && weeklyReceived != null) {
            mJoy = dailyJoy;
            mLifetimeJoy = lifetimeJoy;
            mWeeklyGiven = weeklyGiven;
            mWeeklyReceived = weeklyReceived;
        }

        // Reference private storage and target file name for daily progress data
        File targetDir = mContext.getFilesDir();
        File joyFile = new File(targetDir + JOY_DATA);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(joyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write daily progress data to file
            objectOutputStream.writeObject(mJoy);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();

            // Save to cloud storage
            FirebaseUtils.saveDailyProgress(mJoy);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Reference file name for lifetime progress data
        File lifetimeJoyFile = new File(targetDir + LIFETIME_JOY_DATA);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(lifetimeJoyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write lifetime progress data to file
            objectOutputStream.writeObject(mLifetimeJoy);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();

            // Save to cloud storage
            FirebaseUtils.saveLifetimeProgress(mLifetimeJoy);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Reference file name for list of Joy Given for the week
        File weeklyGivenFile = new File(targetDir + WEEKLY_GIVEN_DATA);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(weeklyGivenFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write list of Joy Given for the week to file
            objectOutputStream.writeObject(mWeeklyGiven);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();

            // Save to cloud storage
            FirebaseUtils.saveWeeklyGiveActions(mWeeklyGiven);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Reference file name for list of Joy Received for the week
        File weeklyReceivedFile = new File(targetDir + WEEKLY_RECEIVED_DATA);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(weeklyReceivedFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write list of Joy Received for the week to file
            objectOutputStream.writeObject(mWeeklyReceived);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();

            // Save to cloud storage
            FirebaseUtils.saveWeeklyReceivedActions(mWeeklyReceived);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom method to load daily joy data
    private void loadDailyJoy() {
        // Reference private storage and file name for daily progress data
        File targetDir = mContext.getFilesDir();
        File joyFile = new File(targetDir + JOY_DATA);

        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(joyFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read daily progress from file
            mJoy = (Joy) objectInputStream.readObject();

            // Close input streams
            objectInputStream.close();
            fileInputStream.close();

            // Call custom method to check validity of loaded daily joy data
            if(mJoy == null) {
                Date date = new Date();
                mJoy = new Joy(date);
            }

            // Call custom method to check validity of loaded daily joy data
            checkDailyJoyValidity();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            // If no data was loaded, create a new daily Joy object to begin tracking progress
            Date date = new Date();
            mJoy = new Joy(date);
        }
    }

    // Custom method to check validity of loaded daily joy data
    private void checkDailyJoyValidity() {
        // Create calendar item to reference current date elements
        Calendar currentCalendar = Calendar.getInstance();

        // Extract current day, month, and year
        int currentDay = currentCalendar.get(Calendar.DATE);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        // Create calendar item to reference date elements for date/time stamp of object being checked
        Calendar createdCalendar = Calendar.getInstance();

        createdCalendar.setTime(mJoy.getDateCreated());

        // Extract day, month, and year from date/time stamp of object being checked
        int createdDay = createdCalendar.get(Calendar.DATE);
        int createdMonth = createdCalendar.get(Calendar.MONTH);
        int createdYear = createdCalendar.get(Calendar.YEAR);

        // If data for daily progress is not from today, it is invalid and should be discarded
        // Create a new daily Joy object to begin tracking progress for the day
        if (currentDay != createdDay || currentMonth != createdMonth || currentYear != createdYear) {
            Date date = new Date();
            mJoy = new Joy(date);
        }
    }

    // Custom method to load lifetime joy data
    private void loadLifetimeJoy() {
        // Reference private storage and file name for lifetime progress data
        File targetDir = mContext.getFilesDir();
        File lifetimeJoyFile = new File(targetDir + LIFETIME_JOY_DATA);

        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(lifetimeJoyFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read lifetime progress from file
            mLifetimeJoy = (Joy) objectInputStream.readObject();

            if(mLifetimeJoy == null) {
                Date date = new Date();
                mLifetimeJoy = new Joy(date);
            }

            // Close input streams
            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            // If no data was loaded, create a new lifetime Joy object to begin tracking progress
            Date date = new Date();
            mLifetimeJoy = new Joy(date);
        }
    }

    // Custom method to load individual Give actions for the week
    private void loadWeeklyGiven() {
        // Reference private storage and file name for Daily Given progress data
        File targetDir = mContext.getFilesDir();
        File weeklyGivenFile = new File(targetDir + WEEKLY_GIVEN_DATA);

        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(weeklyGivenFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read list of Joy Given for the week from file
            //noinspection unchecked
            mWeeklyGiven = (ArrayList<Give>) objectInputStream.readObject();

            if(mWeeklyGiven == null) {
                mWeeklyGiven = new ArrayList<>();
            }

            // Close input streams
            objectInputStream.close();
            fileInputStream.close();

            // Call custom method to check validity of loaded weekly Give data
            checkWeeklyGivenValidity();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            // If no data was loaded, create a new list of Joy Given for the week to begin tracking progress
            mWeeklyGiven = new ArrayList<>();
        }
    }

    // Custom method to check validity of loaded weekly Give data
    private void checkWeeklyGivenValidity() {
        // Create calendar item to reference current date elements
        Calendar currentCalendar = Calendar.getInstance();

        // Extract current day, month, and year
        int currentDay = currentCalendar.get(Calendar.DATE);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        // Create calendar item to reference date elements for date/time stamp of object being checked
        Calendar createdCalendar = Calendar.getInstance();

        // Loop through list of Joy Given objects that have been loaded
        for(Give given : mWeeklyGiven) {
            // Pull date/time stamp for current Give object
            createdCalendar.setTime(given.getDateCreated());

            // Extract day, month, and year from date/time stamp of current object
            int createdDay = createdCalendar.get(Calendar.DATE);
            int createdMonth = createdCalendar.get(Calendar.MONTH);
            int createdYear = createdCalendar.get(Calendar.YEAR);

            // If the current Give object is more than 7 days old, it should be discarded
            // Remove it from the list. When the list is saved again, the object will no longer exist
            if(createdDay < currentDay - 7 || createdMonth < currentMonth || createdYear < currentYear) {
                mWeeklyGiven.remove(given);
            }
        }
    }

    // Custom method to load individual Get actions for the week
    private void loadWeeklyReceived() {
        // Reference private storage and file name for Weekly Given progress data
        File targetDir = mContext.getFilesDir();
        File weeklyReceivedFile = new File(targetDir + WEEKLY_RECEIVED_DATA);

        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(weeklyReceivedFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read list of Joy Received for the week from file
            //noinspection unchecked
            mWeeklyReceived = (ArrayList<Get>) objectInputStream.readObject();

            if(mWeeklyReceived == null) {
                mWeeklyReceived = new ArrayList<>();
            }

            // Close input streams
            objectInputStream.close();
            fileInputStream.close();

            // Call custom method to check validity of loaded weekly Get data
            checkWeeklyReceivedValidity();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            // If no data was loaded, create a new list of Joy Received for the week to begin tracking progress
            mWeeklyReceived = new ArrayList<>();
        }
    }

    private void checkWeeklyReceivedValidity() {
        // Create calendar item to reference current date elements
        Calendar currentCalendar = Calendar.getInstance();

        // Extract current day, month, and year
        int currentDay = currentCalendar.get(Calendar.DATE);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        // Create calendar item to reference date elements for date/time stamp of object being checked
        Calendar createdCalendar = Calendar.getInstance();

        // Loop through list of Joy Received objects that have been loaded
        for (Get received : mWeeklyReceived) {
            // Pull date/time stamp for current Receive object
            createdCalendar.setTime(received.getDateCreated());

            // Extract day, month, and year from date/time stamp of current object
            int createdDay = createdCalendar.get(Calendar.DATE);
            int createdMonth = createdCalendar.get(Calendar.MONTH);
            int createdYear = createdCalendar.get(Calendar.YEAR);

            // If the current Receive object is more than 7 days old, it should be discarded
            // Remove it from the list. When the list is saved again, the object will no longer exist
            if(createdDay < currentDay - 7 || createdMonth < currentMonth || createdYear < currentYear) {
                mWeeklyReceived.remove(received);
            }
        }
    }

    // Custom method to clear all cached data
    public void clearCache() {
        // Delete files
        mContext.deleteFile(JOY_DATA);
        mContext.deleteFile(LIFETIME_JOY_DATA);
        mContext.deleteFile(WEEKLY_GIVEN_DATA);
        mContext.deleteFile(WEEKLY_RECEIVED_DATA);
        mContext.deleteFile(FirebaseUtils.FILE_USER);

        // Restart application
        Intent restartIntent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(restartIntent);
        ((Activity) mContext).finish();
    }


    // **DETAIL METHODS**
    // Custom method to load and return daily joy
    public Joy getDailyJoy() {
        loadDailyJoy();

        return mJoy;
    }

    // Custom method to load and return lifetime joy
    public Joy getLifetimeJoy() {
        loadLifetimeJoy();

        return mLifetimeJoy;
    }

    // Custom method to load and return weekly Give data
    public ArrayList<Give> getWeeklyGiven() {
        loadWeeklyGiven();

        return mWeeklyGiven;
    }

    // Custom method to load and return weekly Get data
    public ArrayList<Get> getWeeklyReceived() {
        loadWeeklyReceived();

        return mWeeklyReceived;
    }
}
