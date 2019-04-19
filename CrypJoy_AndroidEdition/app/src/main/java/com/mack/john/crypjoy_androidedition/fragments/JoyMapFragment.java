package com.mack.john.crypjoy_androidedition.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;
import com.mack.john.crypjoy_androidedition.utilities.LocationUtils;

import java.util.ArrayList;

public class JoyMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {


    private GoogleMap mMap;

    private double mLatitude;
    private double mLongitude;

    private ArrayList<Give> mWeeklyJoyGiven;
    private ArrayList<Get> mWeeklyJoyReceived;



    // Constructor
    public JoyMapFragment() {}


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        // Extract current latitude and longitude
        LocationUtils locationUtils = new LocationUtils(getActivity());
        mLatitude = locationUtils.getLatitude();
        mLongitude = locationUtils.getLongitude();

        // Load map
        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Call custom method to add map markers for actions completed in the last 7 days
        addMapMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Load custom map style
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map);
        googleMap.setMapStyle(mapStyleOptions);

        // Reference Map object and set custom Info Window adapter
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);

        // Call custom method to zoom map in to current location
        zoomInCamera();

        // Call custom method to add map markers for actions completed in the last 7 days
        addMapMarkers();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View contents = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

        // Instantiate null JoyAction objects
        Get getAction = null;
        Give giveAction = null;

        // ** NOTE: This method will attempt to case the marker tag as either a Get or Give action.
        // It should never be possible for the marker to be cast to any type other than these two options.
        // If the marker cannot be case to a Get object it MUST be possible for it to cast to a Give object

        // Attempt to cast marker tag to Get object
        try {
            getAction = (Get) marker.getTag();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        // Attempt to cast marker tag as Give object
        try {
            giveAction = (Give) marker.getTag();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        // If marker was successfully cast to a Get object, display the details for the action it represents
        if(getAction != null) {
            ((TextView) contents.findViewById(R.id.text_joyAction)).setText(getAction.getActionName());
            ((TextView) contents.findViewById(R.id.text_date)).setText(getAction.displayDate());
            ((TextView) contents.findViewById(R.id.text_time)).setText(getAction.displayTime());
            ((ImageView) contents.findViewById(R.id.markerImage)).setImageDrawable(getResources().getDrawable(R.drawable.heart_icon));
        }

        // If marker was successfully case to a Give object, display the details for the action it represents
        else if(giveAction != null) {
            ((TextView) contents.findViewById(R.id.text_joyAction)).setText(giveAction.getActionName());
            ((TextView) contents.findViewById(R.id.text_date)).setText(giveAction.displayDate());
            ((TextView) contents.findViewById(R.id.text_time)).setText(giveAction.displayTime());
            ((ImageView) contents.findViewById(R.id.markerImage)).setImageDrawable(getResources().getDrawable(R.drawable.heart_icon));
        }

        return contents;
    }




    // Custom methods
    // Custom method to zoom map in to current location
    private void zoomInCamera() {
        // If the map us null, do not continue
        if(mMap == null) {
            return;
        }

        // Reference current latitude and longitude
        LatLng currentLocation = new LatLng(mLatitude, mLongitude);

        // Zoom camera in to current location
        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
        mMap.animateCamera(cameraMovement);
    }

    // Custom method to add map markers for actions completed in the last 7 days
    private void addMapMarkers() {
        // If the map is null, do not continue
        if(mMap == null) {
            return;
        }

        // Extract hex code for the app's default Orange color
        @SuppressLint("ResourceType")
        String orange = getResources().getString(R.color.colorAccent);


        // Extract hex code for the app's primary dark color
        @SuppressLint("ResourceType")
        String green = getResources().getString(R.color.colorPrimaryDark);

        // NOTE: These hex codes will be used to apply custom colors to map markers, depending on the
        // action type the marker represents

        // Call custom method to load weekly actions completed in the last 7 days
        loadWeeklyActions();

        // If Give actions exist for the last 7 days, continue
        if(mWeeklyJoyGiven != null) {

            // Loop through each action
            for (Give given : mWeeklyJoyGiven) {

                // Extract location info for current action
                LatLng position = new LatLng(given.getLatitude(), given.getLongitude());

                // Set marker color and position
                MarkerOptions options = new MarkerOptions();
                options.icon(setMarkerColor(orange));
                options.position(position);

                // Add marker to the map
                Marker marker = mMap.addMarker(options);

                // Add Give object as marker tag. This will be used later to display action details
                // in the marker's Info Window
                marker.setTag(given);
            }
        }

        // If Get actions exist for the last 7 days, continue
        if(mWeeklyJoyReceived != null) {

            // Loop through each action
            for (Get received : mWeeklyJoyReceived) {

                // Extract location info for current action
                LatLng position = new LatLng(received.getLatitude(), received.getLongitude());

                // Set marker color and position
                MarkerOptions options = new MarkerOptions();
                options.icon(setMarkerColor(green));
                options.position(position);

                // Add marker to the map
                Marker marker = mMap.addMarker(options);

                // Add Get object as marker tag. This will be used later to display action details
                // in the marker's Info Window
                marker.setTag(received);
            }
        }
    }

    // Custom method to convert color hex code to a hue that the map marker BitmapDescriptor will recognize
    private BitmapDescriptor setMarkerColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    // Custom method to load actions completed in the last 7 days
    private void loadWeeklyActions() {
        JoyUtils joyUtils = new JoyUtils(getActivity());

        mWeeklyJoyGiven = joyUtils.getWeeklyGiven();
        mWeeklyJoyReceived = joyUtils.getWeeklyReceived();
    }
}
