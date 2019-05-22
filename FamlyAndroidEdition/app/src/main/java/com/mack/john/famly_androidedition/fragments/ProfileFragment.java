package com.mack.john.famly_androidedition.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.family_profile.FamilyProfileActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ProfileFragment";

     ImageButton mDeleteProfileButton;
     ImageButton mEditPhotoButton;
     ImageButton mFamilyButton;

     TextView mNameDisplay;
     TextView mDateTimeDisplay;
     TextView mLastLocationDisplay;



    // System generated methods
    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setClickListener(view);
        setTextDisplay(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_delete_profile) {

        }

        else if(view.getId() == R.id.button_edit_photo) {

        }

        else if(view.getId() == R.id.button_family) {
            Intent familyIntent = new Intent(getActivity(), FamilyProfileActivity.class);
            startActivity(familyIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        mDeleteProfileButton = view.findViewById(R.id.button_delete_profile);
        mEditPhotoButton = view.findViewById(R.id.button_edit_photo);
        mFamilyButton = view.findViewById(R.id.button_family);

        mDeleteProfileButton.setOnClickListener(this);
        mEditPhotoButton.setOnClickListener(this);
        mFamilyButton.setOnClickListener(this);
    }

    private void setTextDisplay(View view) {
        mNameDisplay = view.findViewById(R.id.display_name);
        mDateTimeDisplay = view.findViewById(R.id.display_timestamp);
        mLastLocationDisplay = view.findViewById(R.id.display_last_location);
    }
}
