package com.mack.john.famly_androidedition.fragments.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mack.john.famly_androidedition.R;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ForgotPasswordFragment";



    // System generated methods
    public static ForgotPasswordFragment newInstance() {
        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_send) {

        }
    }




    // Custom methods
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button sendButton = view.findViewById(R.id.button_send);

        cancelButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);
    }
}
