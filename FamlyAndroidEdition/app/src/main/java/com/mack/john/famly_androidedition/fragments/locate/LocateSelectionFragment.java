package com.mack.john.famly_androidedition.fragments.locate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ProfilesAdapter;
import com.mack.john.famly_androidedition.locate.LocateProfileActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;

public class LocateSelectionFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LocateSelectionFragment";

    public static final String EXTRA_PROFILE = "extra_profile";



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
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_locate_selection, container, false);

        // Call custom methods to set click listener and populate profile grid
        setClickListener(view);
        populateGrid(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // if user clicked cancel button, return to previous activity
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }
    }



    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        Button cancelButton = view.findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(this);
    }

    // Custom method to populate profile grid
    private void populateGrid(View view) {
        // Reference all profiles on account
        Account account = AccountUtils.loadAccount(getActivity());
        final Profile[] profiles = account.getProfiles().toArray(new Profile[account.getProfiles().size()]);

        // Reference gridview
        GridView profileGrid = view.findViewById(R.id.grid_family);

        // Set gridview adapter
        ProfilesAdapter profilesAdapter = new ProfilesAdapter(getActivity(), profiles, account);
        profileGrid.setAdapter(profilesAdapter);

        // Set gridview click listener
        profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Reference selected profile
                Profile profile = profiles[position];

                // Launch profile location activity and send selected profile
                Intent editIntent = new Intent(getActivity(), LocateProfileActivity.class);
                editIntent.putExtra(EXTRA_PROFILE, profile);

                getActivity().startActivity(editIntent);
            }
        });
    }
}
