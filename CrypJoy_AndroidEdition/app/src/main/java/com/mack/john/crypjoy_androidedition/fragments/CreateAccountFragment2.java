package com.mack.john.crypjoy_androidedition.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.MainActivity;
import com.mack.john.crypjoy_androidedition.R;

public class CreateAccountFragment2 extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "CreateAccountFragment2";



    // System generaged methods
    public static CreateAccountFragment2 newInstance() {

        Bundle args = new Bundle();

        CreateAccountFragment2 fragment = new CreateAccountFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account2, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_finish) {
            Intent finishIntent = new Intent(getActivity(), DailyDetailsActivity.class);
            startActivity(finishIntent);
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(cancelIntent);
            getActivity().finish();
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);

        Button finishButton = view.findViewById(R.id.button_finish);
        finishButton.setOnClickListener(this);

    }
}
