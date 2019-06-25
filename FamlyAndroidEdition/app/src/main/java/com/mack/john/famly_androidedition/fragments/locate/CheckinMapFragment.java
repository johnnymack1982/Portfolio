package com.mack.john.famly_androidedition.fragments.locate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;

public class CheckinMapFragment extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {



    // Class properties
    public static final String TAG = "CheckinMapFragment";

    private GoogleMap mMap;

    private double mCurrentLat;
    private double mCurrentLong;



    // System generated methods
    public static CheckinMapFragment newInstance() {
        Bundle args = new Bundle();

        CheckinMapFragment fragment = new CheckinMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Load custom map style
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map);
        googleMap.setMapStyle(mapStyleOptions);

        // Reference current location
        LocationUtils locationUtils = new LocationUtils(getActivity());
        mCurrentLat = locationUtils.getLatitude();
        mCurrentLong = locationUtils.getLongitude();

        // Reference map
        mMap = googleMap;

        // Call custom methods to set map location and add marker
        zoomInCamera();
        addMapMarker();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
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
        // If map is null, do not continue
        if(mMap == null) {
            return;
        }

        // Set custom marker color
        @SuppressLint("ResourceType")
        String green = getResources().getString(R.color.colorGrassPrimary);

        // Set profile name as marker title
        MarkerOptions options = new MarkerOptions();
        options.title(AccountUtils.loadProfile(getActivity()).getFirstName() + " " + AccountUtils.loadAccount(getActivity()).getFamilyName());

        // Set current location as marker snippet
        LocationUtils locationUtils = new LocationUtils(getActivity());
        options.snippet(locationUtils.convertAddress(getActivity(), mCurrentLat, mCurrentLong));

        // Set location for marker
        LatLng currentLocation = new LatLng(mCurrentLat, mCurrentLong);
        options.position(currentLocation);
        options.icon(setMarkerColor(green));

        // Add marker to map
        mMap.addMarker(options);
    }

    private BitmapDescriptor setMarkerColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
