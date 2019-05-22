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
import com.mack.john.famly_androidedition.login.ForgotPasswordActivity;
import com.mack.john.famly_androidedition.login.ProfileLoginActivity;
import com.mack.john.famly_androidedition.signup.SignupMaster1Activity;

public class MasterLoginFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "MasterLoginFragment";



    // System generated methods
    public static MasterLoginFragment newInstance() {
        Bundle args = new Bundle();

        MasterLoginFragment fragment = new MasterLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_master, container, false);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_login) {
            Intent profileLoginIntent = new Intent(getActivity(), ProfileLoginActivity.class);
            startActivity(profileLoginIntent);
        }

        else if(view.getId() == R.id.button_forgot_password) {
            Intent forgotPasswordIntent = new Intent(getActivity(), ForgotPasswordActivity.class);
            startActivity(forgotPasswordIntent);
        }

        else if(view.getId() == R.id.button_sign_up) {
            Intent signupIntent = new Intent(getActivity(), SignupMaster1Activity.class);
            startActivity(signupIntent);
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        Button loginButton = view.findViewById(R.id.button_login);
        Button forgotPasswordButton = view.findViewById(R.id.button_forgot_password);
        Button signupButton = view.findViewById(R.id.button_sign_up);

        loginButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }
}
