package com.mack.john.famly_androidedition.fragments.locate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.R;

public class LocateSelectionFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LocateSelectionFragment";



    // System generated methods
    public static LocateSelectionFragment newInstance() {
        Bundle args = new Bundle();

        LocateSelectionFragment fragment = new LocateSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locate_selection, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(this);
    }
}
