package com.mack.john.famly_androidedition.fragments.signup.profile.child;

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

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;
import com.mack.john.famly_androidedition.signup.profile.child.AddChild2Activity;

public class AddChild1Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "AddChild1Fragment";



    // System generated methods
    public static AddChild1Fragment newInstance() {
        Bundle args = new Bundle();

        AddChild1Fragment fragment = new AddChild1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_child1, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_date_picker) {

        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(cancelIntent);
        }

        else if(view.getId() == R.id.button_create) {
            Intent continueIntent = new Intent(getActivity(), AddChild2Activity.class);
            startActivity(continueIntent);
        }
    }



    // System generated methods
    private void setClickListener(View view) {
        ImageButton calendarButton = view.findViewById(R.id.button_date_picker);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_create);

        calendarButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }
}
