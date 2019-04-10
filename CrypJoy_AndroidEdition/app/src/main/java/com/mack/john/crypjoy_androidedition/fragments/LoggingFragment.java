package com.mack.john.crypjoy_androidedition.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Joy;

public class LoggingFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LoggingFragment";

    View mView;
    Joy mJoy;



    // Constructor
    public LoggingFragment() {}



    // System generated methods
    public static LoggingFragment newInstance() {
        Bundle args = new Bundle();

        LoggingFragment fragment = new LoggingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logging, container, false);

        mJoy = new Joy();
        mView = view;

        setClickListener();
        checkProgress();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_give) {
            giveJoy();
        }

        else if (view.getId() == R.id.button_get) {
            getJoy();
        }
    }



    // Custom methods
    private void setClickListener() {
        Button giveButton = mView.findViewById(R.id.button_give);
        giveButton.setOnClickListener(this);

        Button getButton = mView.findViewById(R.id.button_get);
        getButton.setOnClickListener(this);
    }

    private void giveJoy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(getString(R.string.yes), giveJoyListener);
        builder.setNegativeButton(getString(R.string.nope), giveJoyListener);

        builder.setTitle(getString(R.string.give_joy_dialog));
        builder.setMessage(getString(R.string.give_joy_dialog_body));

        AlertDialog giveJoyDialog = builder.create();
        giveJoyDialog.show();
    }

    private void getJoy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(getString(R.string.yes), getJoyListenter);
        builder.setNegativeButton(getString(R.string.nope), getJoyListenter);

        builder.setTitle(getString(R.string.get_joy_dialog));
        builder.setMessage(getString(R.string.get_joy_dialog_body));

        AlertDialog getJoyDialog = builder.create();
        getJoyDialog.show();
    }

    private void checkProgress() {
        Button giveButton = mView.findViewById(R.id.button_give);
        Button getButton = mView.findViewById(R.id.button_get);

        if(mJoy.getGiveProgress() == mJoy.getGiveGoal()) {
            giveButton.setEnabled(false);
            giveButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorTextSecondary));
        }

        else {
            giveButton.setEnabled(true);
            giveButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorAccent));
        }

        if(mJoy.getGetProgress() == 6) {
            getButton.setEnabled(false);
            getButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorTextSecondary));
        }

        else {
            getButton.setEnabled(true);
            getButton.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorAccent));
        }
    }



    // Dialog Listeners
    DialogInterface.OnClickListener giveJoyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                mJoy.joyGiven();

                checkProgress();

                Toast.makeText(getActivity(), getString(R.string.joy_given_confirm), Toast.LENGTH_SHORT).show();
            }
        }
    };

    DialogInterface.OnClickListener getJoyListenter = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which == -1) {
                mJoy.joyReceived();

                checkProgress();

                Toast.makeText(getActivity(), getString(R.string.joy_received_confirm), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
