package com.mack.john.famly_androidedition.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;

public class ProfilesAdapter extends BaseAdapter {



    // Class properties
    private final Context context;
    private final Profile[] profiles;
    private final Account account;



    // Constructor
    public ProfilesAdapter(Context mContext, Profile[] profiles, Account account) {
        this.context = mContext;
        this.profiles = profiles;
        this.account = account;
    }



    // System generated methods

    @Override
    public int getCount() {
        return profiles.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Profile getItem(int position) {
        return profiles[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Profile profile = getItem(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cell_list_profiles, null);
        }

        TextView profileNameButton = convertView.findViewById(R.id.profile_name);

        AccountUtils.loadProfilePhoto(context, convertView, profile);

        String name = profile.getFirstName() + " " + account.getFamilyName();
        profileNameButton.setText(name);

        return convertView;
    }
}
