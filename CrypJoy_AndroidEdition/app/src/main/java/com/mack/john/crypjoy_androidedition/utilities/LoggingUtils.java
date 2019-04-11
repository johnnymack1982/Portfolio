package com.mack.john.crypjoy_androidedition.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.R;
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

public class LoggingUtils {



    // Class properties
    public static final String TAG = "LoggingUtils";

    private static final String JOY_DATA = "joy.joy";
    private static final String WEEKLY_GIVEN_DATA = "weeklygiven.joy";
    private static final String WEEKLY_RECEIVED_DATA = "weeklyreceived.joy";

    private Context mContext;
    private View mView;

    private Joy mJoy;
    private ArrayList<Give> mWeeklyGiven;
    private ArrayList<Get> mWeeklyReceived;



    // Constructor
    public LoggingUtils(Context context, View view) {
        this.mContext = context;
        this.mView = view;

        loadProgress();

        this.mWeeklyGiven = new ArrayList<>();
        this.mWeeklyReceived = new ArrayList<>();
    }




    // Custom methods
    public void giveJoy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setPositiveButton(mContext.getString(R.string.yes), giveJoyListener);
        builder.setNegativeButton(mContext.getString(R.string.nope), giveJoyListener);

        builder.setTitle(mContext.getString(R.string.give_joy_dialog));
        builder.setMessage(mContext.getString(R.string.give_joy_dialog_body));

        AlertDialog giveJoyDialog = builder.create();
        giveJoyDialog.show();
    }

    public void getJoy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setPositiveButton(mContext.getString(R.string.yes), getJoyListenter);
        builder.setNegativeButton(mContext.getString(R.string.nope), getJoyListenter);

        builder.setTitle(mContext.getString(R.string.get_joy_dialog));
        builder.setMessage(mContext.getString(R.string.get_joy_dialog_body));

        AlertDialog getJoyDialog = builder.create();
        getJoyDialog.show();
    }

    public void checkProgress() {
        Button giveButton = mView.findViewById(R.id.button_give);
        Button getButton = mView.findViewById(R.id.button_get);

        if(mJoy.getGiveProgress() == mJoy.getGiveGoal()) {
            giveButton.setEnabled(false);
            giveButton.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorTextSecondary));
        }

        else {
            giveButton.setEnabled(true);
            giveButton.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorAccent));
        }

        if(mJoy.getGetProgress() == 6) {
            getButton.setEnabled(false);
            getButton.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorTextSecondary));
        }

        else {
            getButton.setEnabled(true);
            getButton.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorAccent));
        }

        TextView progressMessage = mView.findViewById(R.id.text_progress_message);

        if(giveButton.isEnabled() && getButton.isEnabled()) {
            progressMessage.setText(mContext.getString(R.string.are_you_doing_something_nice_for_somebody_or_did_they_do_something_nice_for_you));
        }

        else if(!giveButton.isEnabled() && getButton.isEnabled()) {
            progressMessage.setText(mContext.getString(R.string.give_disabled_get_enabled));
        }
        
        else if(giveButton.isEnabled() && !getButton.isEnabled()) {
            progressMessage.setText(mContext.getString(R.string.give_enabled_get_disabled));
        }
        
        else if(!giveButton.isEnabled() && !getButton.isEnabled()) {
            progressMessage.setText(mContext.getString(R.string.give_disabled_get_disabled));
        }
    }

    public void saveProgress() {
        File targetDir = mContext.getFilesDir();
        File joyFile = new File(targetDir + JOY_DATA);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(joyFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(mJoy);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        File weeklyGivenFile = new File(targetDir + WEEKLY_GIVEN_DATA);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(weeklyGivenFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(mWeeklyGiven);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        File weeklyReceivedFile = new File(targetDir + WEEKLY_RECEIVED_DATA);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(weeklyReceivedFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(mWeeklyReceived);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProgress() {
        File targetDir = mContext.getFilesDir();
        File joyFile = new File(targetDir + JOY_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(joyFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            mJoy = (Joy) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            Date date = new Date();
            mJoy = new Joy(date);
        }

        File weeklyGivenFile = new File(targetDir + WEEKLY_GIVEN_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(weeklyGivenFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            mWeeklyGiven = (ArrayList<Give>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            mWeeklyGiven = new ArrayList<>();
        }

        File weeklyReceivedFile = new File(targetDir + WEEKLY_RECEIVED_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(weeklyReceivedFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            mWeeklyReceived = (ArrayList<Get>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // This is for testing purposes only
        // When you don't need to reset daily progress, these lines should be commented out
        Date date = new Date();
        this.mJoy = new Joy(date);
        mWeeklyGiven = new ArrayList<>();
        mWeeklyReceived = new ArrayList<>();

        checkDataValidity();
    }

    private void checkDataValidity() {
        Calendar currentCalendar = Calendar.getInstance();

        int currentDay = currentCalendar.get(Calendar.DATE);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTime(mJoy.getDateCreated());

        int createdDay = createdCalendar.get(Calendar.DATE);
        int createdMonth = createdCalendar.get(Calendar.MONTH);
        int createdYear = createdCalendar.get(Calendar.YEAR);

        if(currentDay != createdDay || currentMonth != createdMonth || currentYear != createdYear) {
            Date date = new Date();
            mJoy = new Joy(date);
        }

        for(Give given : mWeeklyGiven) {
            createdCalendar.setTime(given.getDateCreated());

            createdDay = createdCalendar.get(Calendar.DATE);
            createdMonth = createdCalendar.get(Calendar.MONTH);
            createdYear = createdCalendar.get(Calendar.YEAR);

            if(createdDay < currentDay - 7 || createdMonth < currentMonth || createdYear < currentYear) {
                mWeeklyGiven.remove(given);
            }
        }

        for (Get received : mWeeklyReceived) {
            createdCalendar.setTime(received.getDateCreated());

            createdDay = createdCalendar.get(Calendar.DATE);
            createdMonth = createdCalendar.get(Calendar.MONTH);
            createdYear = createdCalendar.get(Calendar.YEAR);

            if(createdDay < currentDay - 7 || createdMonth < currentMonth || createdYear < currentYear) {
                mWeeklyGiven.remove(received);
            }
        }
    }



    // Dialog Listeners
    DialogInterface.OnClickListener giveJoyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                mJoy.joyGiven();

                Date date = new Date();

                Give give = new Give(date, mContext);
                mWeeklyGiven.add(give);

                checkProgress();
                saveProgress();

                Toast.makeText(mContext, mContext.getString(R.string.joy_given_confirm), Toast.LENGTH_SHORT).show();
            }
        }
    };

    DialogInterface.OnClickListener getJoyListenter = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                mJoy.joyReceived();

                Date date = new Date();

                Get get = new Get(date, mContext);
                mWeeklyReceived.add(get);

                checkProgress();
                saveProgress();

                Toast.makeText(mContext, mContext.getString(R.string.joy_received_confirm), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
