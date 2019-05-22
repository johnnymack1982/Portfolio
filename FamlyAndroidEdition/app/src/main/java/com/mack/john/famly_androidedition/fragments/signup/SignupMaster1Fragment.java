package com.mack.john.famly_androidedition.fragments.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.signup.SignupMaster2Activity;

public class SignupMaster1Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "SignupMaster1Fragment";



    // System generated methods
    public static SignupMaster1Fragment newInstance() {
        Bundle args = new Bundle();

        SignupMaster1Fragment fragment = new SignupMaster1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_master1, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_create) {
            Intent nextIntent = new Intent(getActivity(), SignupMaster2Activity.class);
            startActivity(nextIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_create);

        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }
}
