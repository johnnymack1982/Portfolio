package com.mack.john.famly_androidedition.fragments.signup.profile.parent;

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
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;

public class AddParent2Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "AddParent2Fragment";



    // System generated methods
    public static AddParent2Fragment newInstance() {
        Bundle args = new Bundle();

        AddParent2Fragment fragment = new AddParent2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_parent2, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_add_photo) {

        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(cancelIntent);
        }

        else if(view.getId() == R.id.button_create) {
            Intent continueIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(continueIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button addPhotoButton = view.findViewById(R.id.button_add_photo);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button createButton = view.findViewById(R.id.button_create);

        addPhotoButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
    }
}
