package com.mack.john.famly_androidedition.fragments.login;

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
import com.mack.john.famly_androidedition.NavigationActivity;

public class ProfileLoginFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ProfileLoginFragment";



    // System generated methods
    public static ProfileLoginFragment newInstance() {
        Bundle args = new Bundle();

        ProfileLoginFragment fragment = new ProfileLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_login, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_continue) {
            Intent continueIntent = new Intent(getActivity(), NavigationActivity.class);
            startActivity(continueIntent);
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button continueButton = view.findViewById(R.id.button_continue);

        cancelButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
    }
}
