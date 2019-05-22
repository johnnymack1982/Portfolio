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
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.signup.profile.parent.AddParent1Activity;

public class SignupMaster3Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "SignupMaster3Fragment";



    // System generated methods
    public static SignupMaster3Fragment newInstance() {
        Bundle args = new Bundle();

        SignupMaster3Fragment fragment = new SignupMaster3Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_master3, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add_photo) {

        }

        else if(view.getId() == R.id.button_cancel) {
            Intent loginIntent = new Intent(getActivity(), MasterLoginActivity.class);
            startActivity(loginIntent);
        }

        else if(view.getId() == R.id.button_create) {
            Intent addParentIntent = new Intent(getActivity(), AddParent1Activity.class);
            startActivity(addParentIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button addPhotoButton = view.findViewById(R.id.button_add_photo);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_create);

        addPhotoButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }

}
