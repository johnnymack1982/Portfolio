package com.mack.john.famly_androidedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.permission_request.Request;
import com.mack.john.famly_androidedition.utils.AccountUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChildPermissionRequestAdapter extends BaseAdapter {



    // Class properties
    ArrayList<Request> requests;
    Context context;
    Account account;



    // Constructor
    public ChildPermissionRequestAdapter(ArrayList<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
        this.account = AccountUtils.loadAccount(context);
    }



    // System generated methods
    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return requests.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Request request = requests.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cell_child_permission, null);
        }

        AccountUtils.loadProfilePhoto(context, convertView, request.getRequesterId());

        TextView profileNameDisplay = convertView.findViewById(R.id.display_profile_name);
        String name = request.getRequesterName() + " " + account.getFamilyName();
        profileNameDisplay.setText(name);

        TextView timestampDisplay = convertView.findViewById(R.id.display_timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy @ hh:mm a", Locale.getDefault());
        String timestamp = dateFormat.format(request.getTimeStamp());
        timestampDisplay.setText(timestamp);

        TextView requestMessageDisplay = convertView.findViewById(R.id.display_last_location);
        requestMessageDisplay.setText(request.getRequestMessage());

        ImageView postStatusDisplay = convertView.findViewById(R.id.display_permission_status);

        int requestStatus = request.getRequestStatus();

        ArrayList<Parent> parents = new ArrayList<>();

        for(Profile profile : AccountUtils.loadAccount(context).getProfiles()) {
            if(profile instanceof Parent) {
                parents.add((Parent) profile);
            }
        }

        if(requestStatus == 0) {
            postStatusDisplay.setImageDrawable(context.getDrawable(R.drawable.permission_pending_icon));
        }

        else if(requestStatus == parents.size()) {
            postStatusDisplay.setImageDrawable(context.getDrawable(R.drawable.permission_granted_icon));
        }

        else if(requestStatus == -1) {
            postStatusDisplay.setImageDrawable(context.getDrawable(R.drawable.permission_denied_icon));
        }

        return convertView;
    }
}
