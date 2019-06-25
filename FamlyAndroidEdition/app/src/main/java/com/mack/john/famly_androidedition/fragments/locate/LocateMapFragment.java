package com.mack.john.famly_androidedition.fragments.locate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;

public class LocateMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {



    // Class properties
    public static final String TAG = "LocateMapFragment";

    private GoogleMap mMap;

    private double mCurrentLat;
    private double mCurrentLong;

    Profile mProfile;



    // System generated methods
    public static LocateMapFragment newInstance() {
        Bundle args = new Bundle();

        LocateMapFragment fragment = new LocateMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Reference selected profile
        mProfile = (Profile) getActivity().getIntent().getSerializableExtra(LocateSelectionFragment.EXTRA_PROFILE);

        getMapAsync(this);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Load custom map style
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map);
        googleMap.setMapStyle(mapStyleOptions);

        // Reference map
        mMap = googleMap;

        // Reference last known location for selected profile
        LocationUtils locationUtils = new LocationUtils(getActivity());
        locationUtils.getProfileLocation(mProfile, mMap);
    }
}
