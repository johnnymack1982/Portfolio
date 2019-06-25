package com.mack.john.famly_androidedition.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ParentPermissionRequestAdapter;
import com.mack.john.famly_androidedition.objects.permission_request.Request;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ParentPermissionFragment extends Fragment {



    // Class properties
    View mView;

    SwipeRefreshLayout mRefresher;



    // System generated methods
    public static ParentPermissionFragment newInstance() {
        Bundle args = new Bundle();

        ParentPermissionFragment fragment = new ParentPermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        final View view = inflater.inflate(R.layout.fragment_parent_permission, container, false);

        // Reference view
        mView = view;

        // Reference pull-down refresher
        mRefresher = view.findViewById(R.id.refresher);

        // Set pull-down refresher list
        mRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populate(view);

                mRefresher.setRefreshing(false);
            }
        });

        // Call custom method to populate UI
        populate(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populate(mView);
    }




    // Custom methods
    // Custom method to populate UI
    private void populate(View view) {
        // Reference and sort permission requests
        ArrayList<Request> requests = PermissionRequestUtils.loadRequests(getActivity());
        Collections.sort(requests);
        Collections.reverse(requests);

        // Set permission request adapter
        ParentPermissionRequestAdapter parentPermissionRequestAdapter = new ParentPermissionRequestAdapter(requests, getActivity());
        ListView requestList = view.findViewById(R.id.list_requests);
        requestList.setAdapter(parentPermissionRequestAdapter);
    }
}
