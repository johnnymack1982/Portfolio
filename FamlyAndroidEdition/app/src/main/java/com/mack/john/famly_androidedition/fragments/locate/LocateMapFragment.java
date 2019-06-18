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

        mMap = googleMap;

        LocationUtils locationUtils = new LocationUtils(getActivity());
        locationUtils.getProfileLocation(mProfile, mMap);
    }



    // Custom methods
    private void zoomInCamera() {
        // If the map is null, do not continue
        if(mMap == null) {
            return;
        }

        // Reference current latitude and longitude
        LatLng currentLocation = new LatLng(mCurrentLat, mCurrentLong);

        // Zoom camera in to current location
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
        mMap.animateCamera(cameraMovement);
    }

    private void addMapMarker() {
        if(mMap == null) {
            return;
        }

        @SuppressLint("ResourceType")
        String green = getResources().getString(R.color.colorGrassPrimary);

        MarkerOptions options = new MarkerOptions();
        options.title(AccountUtils.loadProfile(getActivity()).getFirstName() + " " + AccountUtils.loadAccount(getActivity()).getFamilyName());

        LocationUtils locationUtils = new LocationUtils(getActivity());
        options.snippet(locationUtils.convertAddress(getActivity(), mCurrentLat, mCurrentLong));

        LatLng currentLocation = new LatLng(mCurrentLat, mCurrentLong);
        options.position(currentLocation);
        options.icon(setMarkerColor(green));

        mMap.addMarker(options);
    }

    private BitmapDescriptor setMarkerColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
