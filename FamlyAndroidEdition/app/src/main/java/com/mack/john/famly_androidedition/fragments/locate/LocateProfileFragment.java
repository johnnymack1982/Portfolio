package com.mack.john.famly_androidedition.fragments.locate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.fragments.CheckinFragment;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;

public class LocateProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "LocateProfileFragment";

    Profile mProfile;

    View mView;



    // Class properties
    public static LocateProfileFragment newInstance() {
        Bundle args = new Bundle();

        LocateProfileFragment fragment = new LocateProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view lauout
        View view = inflater.inflate(R.layout.fragment_locate_profile, container, false);

        // Reference view
        mView = view;

        // Inflate map fragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_map_fragment, LocateMapFragment.newInstance(), LocateMapFragment.TAG)
                .commit();

        // Get selected profile from sending intent
        mProfile = (Profile) getActivity().getIntent().getSerializableExtra(LocateSelectionFragment.EXTRA_PROFILE);

        // Update location display for selected profile
        LocationUtils locationUtils = new LocationUtils(getActivity());
        locationUtils.updateLocationDisplay(getActivity(), view, mProfile);

        // Call custom methods to populate display and set click listener
        populateDisplay(view);
        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked locate button, refresh location data for selected profile
        if(view.getId() == R.id.button_locate) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_map_fragment, LocateMapFragment.newInstance(), LocateMapFragment.TAG)
                    .commit();

            populateDisplay(mView);
        }

        // If user clicked cancel button, return to previous activity
        else if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }
    }




    // Custom methods
    // Custom method to populate display
    private void populateDisplay(View view) {
        // Load profile photo
        AccountUtils.loadProfilePhoto(getActivity(), view, mProfile.getProfileId());

        // Display profile name
        TextView profileNameDisplay = view.findViewById(R.id.display_profile_name);
        profileNameDisplay.setText(mProfile.getFirstName() + " " + AccountUtils.loadAccount(getActivity()).getFamilyName());
    }

    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference buttons in layout
        Button locateButton = view.findViewById(R.id.button_locate);
        Button cancelButton = view.findViewById(R.id.button_cancel);

        // Set click listener for buttons
        locateButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
}
