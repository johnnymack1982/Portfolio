package com.mack.john.famly_androidedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.permission_request.Request;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ParentPermissionRequestAdapter extends BaseAdapter {



    // Class properties
    ArrayList<Request> requests;
    Context context;
    Account account;



    // Constructor
    public ParentPermissionRequestAdapter(ArrayList<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
        this.account = AccountUtils.loadAccount(context);
    }



    // System generated methods

    @Override
    public int getCount() {
        // Return size of request list
        return requests.size();
    }

    @Override
    public long getItemId(int position) {
        // Return position or current item
        return position;
    }

    @Override
    public Object getItem(int position) {
        // Return current item
        return requests.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Reference current request
        final Request request = requests.get(position);

        // Inflate cell
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cell_parent_permission, null);
        }

        // Load currently logged in profile
        AccountUtils.loadProfilePhoto(context, convertView, request.getRequesterId());

        // Display profile name
        TextView profileNameDisplay = convertView.findViewById(R.id.display_profile_name);
        String name = request.getRequesterName() + " " + account.getFamilyName();
        profileNameDisplay.setText(name);

        // Display timestamp
        TextView timestampDisplay = convertView.findViewById(R.id.display_timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy @ hh:mm a", Locale.getDefault());
        String timestamp = dateFormat.format(request.getTimeStamp());
        timestampDisplay.setText(timestamp);

        // Display request message
        TextView requestMessageDisplay = convertView.findViewById(R.id.display_last_location);
        requestMessageDisplay.setText(request.getRequestMessage());

        // Reference grant and deny buttons
        final ImageButton grantButton = convertView.findViewById(R.id.button_grant);
        final ImageButton denyButton = convertView.findViewById(R.id.button_deny);

        // Count number of parent profiles on account
        int parentCount = 0;
        for(Profile profile : AccountUtils.loadAccount(context).getProfiles()) {
            if(profile instanceof Parent) {
                parentCount += 1;
            }
        }

        // If request status is denied, indicate and hide approve button
        if(request.getRequestStatus() == -1) {
            grantButton.setVisibility(View.INVISIBLE);
            grantButton.setEnabled(false);

            denyButton.setImageAlpha(1000);
            denyButton.setEnabled(false);
        }

        // If request status is pending, display both buttons
        else if(request.getRequestStatus() == 0) {
            grantButton.setImageAlpha(25);
            denyButton.setImageAlpha(25);
        }

        // If request is approved by current parent, indicate approved and hide deny button
        else if(request.getFirstApprover().equals(AccountUtils.loadProfile(context).getProfileId()) ||
                request.getRequestStatus() == parentCount) {
            grantButton.setImageAlpha(1000);
            grantButton.setEnabled(false);

            denyButton.setVisibility(View.INVISIBLE);
            denyButton.setEnabled(false);
        }

        // Otherwise, show both buttons
        else {
            grantButton.setImageAlpha(25);
            denyButton.setImageAlpha(25);

            grantButton.setVisibility(View.VISIBLE);
            denyButton.setVisibility(View.VISIBLE);
        }

        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Approve current request
                PermissionRequestUtils.approveRequest(context, request, grantButton, denyButton);
            }
        });


        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Deny current request
                PermissionRequestUtils.denyRequest(context, request, grantButton, denyButton);
            }
        });

        // Return cell
        return convertView;
    }
}
