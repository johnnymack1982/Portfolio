package com.mack.john.famly_androidedition.fragments.family_profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.mack.john.famly_androidedition.R;

public class FamilyProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "FamilyProfileFragment";



    // System generated methods
    public static FamilyProfileFragment newInstance() {
        Bundle args = new Bundle();

        FamilyProfileFragment fragment = new FamilyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family_profile, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_delete_family) {

        }

        else if(view.getId() == R.id.button_edit_family){

        }

        else if(view.getId() == R.id.button_edit_family_photo) {

        }
    }



    // Custom methods
    private void setClickListener(View view) {
        ImageButton deleteFamilyButton = view.findViewById(R.id.button_delete_family);
        ImageButton editFamilyButton = view.findViewById(R.id.button_edit_family);
        ImageButton editFamilyPhotoButton = view.findViewById(R.id.button_edit_family_photo);

        deleteFamilyButton.setOnClickListener(this);
        editFamilyButton.setOnClickListener(this);
        editFamilyPhotoButton.setOnClickListener(this);
    }
}
