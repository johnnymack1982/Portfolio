package com.mack.john.famly_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ChildPermissionRequestAdapter;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.permission_request.Request;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.Collections;

public class ChildPermissionFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class Properties
    public static final String TAG = "ChildPermissionFragment";

    EditText mRequestInput;

    Button mCancelButton;
    Button mRequestButton;

    Account mAccount;
    Profile mProfile;

    View mView;



    // System generated methods
    public static ChildPermissionFragment newInstance() {
        Bundle args = new Bundle();

        ChildPermissionFragment fragment = new ChildPermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_permission, container, false);

        mAccount = AccountUtils.loadAccount(getActivity());
        mProfile = AccountUtils.loadProfile(getActivity());

        mView = view;

        setClickListener(view);
        setFocusListener(view);
        populate(view);

        return view;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mRequestButton.setVisibility(View.VISIBLE);
        }

        else {
            mCancelButton.setVisibility(View.GONE);
            mRequestButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            mRequestInput.clearFocus();
        }

        else if(view.getId() == R.id.button_request) {
            if(mRequestInput.getText().toString() != null && mRequestInput.getText().toString() != "") {
                PermissionRequestUtils.sendRequest(getActivity(), mRequestInput.getText().toString(), mProfile);
                mRequestInput.setText("");
                mRequestInput.clearFocus();
            }
        }
    }



    // Custom methods
    private void setClickListener(View view) {
        mCancelButton = view.findViewById(R.id.button_cancel);
        mRequestButton = view.findViewById(R.id.button_request);

        mCancelButton.setOnClickListener(this);
        mRequestButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mRequestInput = view.findViewById(R.id.input_request);

        mRequestInput.setOnFocusChangeListener(this);
    }

    private void populate(View view) {
        ArrayList<Request> requests = PermissionRequestUtils.loadRequests(getActivity());
        Collections.sort(requests);
        Collections.reverse(requests);

        ChildPermissionRequestAdapter childPermissionRequestAdapter = new ChildPermissionRequestAdapter(requests, getActivity());

        ListView requestList = view.findViewById(R.id.list_requests);
        requestList.setAdapter(childPermissionRequestAdapter);
    }
}
