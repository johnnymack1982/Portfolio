package com.mack.john.famly_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ChildPermissionRequestAdapter;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.permission_request.Request;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;
import com.mack.john.famly_androidedition.utils.PostUtils;

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

    SwipeRefreshLayout mRefresher;



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
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_child_permission, container, false);

        // Load logged in account and logged in profile
        mAccount = AccountUtils.loadAccount(getActivity());
        mProfile = AccountUtils.loadProfile(getActivity());

        // Reference view
        mView = view;

        // Reference pull-down refresher
        mRefresher = view.findViewById(R.id.refresher);

        // Call custom methods to set click and focus listeners
        setClickListener(view);
        setFocusListener(view);

        // Call custom method to populate profile display
        populate(view);

        return view;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        // If input field has focus, show buttons
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mRequestButton.setVisibility(View.VISIBLE);
        }

        // Otherwise, show buttons
        else {
            mCancelButton.setVisibility(View.GONE);
            mRequestButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        // If user cliced cancel button, clear input focus
        if(view.getId() == R.id.button_cancel) {
            mRequestInput.clearFocus();
        }

        // If user clicked request button, send request and clear input focus
        else if(view.getId() == R.id.button_request) {
            if(mRequestInput.getText().toString() != null && mRequestInput.getText().toString() != "") {
                PermissionRequestUtils.sendRequest(getActivity(), mRequestInput.getText().toString(), mProfile);
                mRequestInput.setText("");
                mRequestInput.clearFocus();
            }
        }
    }



    // Custom methods
    // Custom method to set click listener
    private void setClickListener(final View view) {
        mCancelButton = view.findViewById(R.id.button_cancel);
        mRequestButton = view.findViewById(R.id.button_request);

        mCancelButton.setOnClickListener(this);
        mRequestButton.setOnClickListener(this);

        mRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate(view);

                mRefresher.setRefreshing(false);
            }
        });
    }

    // Custom method to set input focus listener
    private void setFocusListener(View view) {
        mRequestInput = view.findViewById(R.id.input_request);

        mRequestInput.setOnFocusChangeListener(this);
    }

    // Custom method to populate profile display
    private void populate(View view) {
        // Reference and sort sent requests
        ArrayList<Request> requests = PermissionRequestUtils.loadRequests(getActivity());
        Collections.sort(requests);
        Collections.reverse(requests);

        // Set permission request adapter
        final ChildPermissionRequestAdapter childPermissionRequestAdapter = new ChildPermissionRequestAdapter(requests, getActivity());
        final ListView requestList = view.findViewById(R.id.list_requests);
        requestList.setAdapter(childPermissionRequestAdapter);
    }
}
