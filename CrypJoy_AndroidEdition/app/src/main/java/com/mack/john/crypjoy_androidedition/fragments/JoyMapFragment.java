package com.mack.john.crypjoy_androidedition.fragments;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.objects.JoyAction;
import com.mack.john.crypjoy_androidedition.utilities.JoyUtils;
import com.mack.john.crypjoy_androidedition.utilities.LocationUtils;

import java.util.ArrayList;
import java.util.Date;

public class JoyMapFragment extends MapFragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {



    // Class properties
    public static final String TAG = "JoyMapFragment";

    private GoogleMap mMap;

    private double mLatitude;
    private double mLongitude;

    ArrayList<Give> mWeeklyJoyGiven;
    ArrayList<Get> mWeeklyJoyReceived;



    // Constructor
    public JoyMapFragment() {}



    // System generated methods
    public static JoyMapFragment newInstance() {
        Bundle args = new Bundle();

        JoyMapFragment fragment = new JoyMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        LocationUtils locationUtils = new LocationUtils(getActivity());

        mLatitude = locationUtils.getLatitude();
        mLongitude = locationUtils.getLongitude();

        getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        addMapMarkers();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);

        zoomInCamera();
        addMapMarkers();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View contents = LayoutInflater.from(getActivity()).inflate(R.layout.info_window, null);

        Get getAction = null;
        Give giveAction = null;

        try {
            getAction = (Get) marker.getTag();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            giveAction = (Give) marker.getTag();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        if(getAction != null) {
            ((TextView) contents.findViewById(R.id.text_joyAction)).setText(getAction.getActionName());
            ((TextView) contents.findViewById(R.id.text_date)).setText(getAction.displayDate());
            ((TextView) contents.findViewById(R.id.text_time)).setText(getAction.displayTime());
            ((ImageView) contents.findViewById(R.id.markerImage)).setImageDrawable(getResources().getDrawable(R.drawable.heart_icon));
        }

        else if(giveAction != null) {
            ((TextView) contents.findViewById(R.id.text_joyAction)).setText(giveAction.getActionName());
            ((TextView) contents.findViewById(R.id.text_date)).setText(giveAction.displayDate());
            ((TextView) contents.findViewById(R.id.text_time)).setText(giveAction.displayTime());
            ((ImageView) contents.findViewById(R.id.markerImage)).setImageDrawable(getResources().getDrawable(R.drawable.heart_icon));
        }

        return contents;
    }




    // Custom methods
    private void zoomInCamera() {
        if(mMap == null) {
            return;
        }

        LatLng currentLocation = new LatLng(mLatitude, mLongitude);

        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 16);
        mMap.animateCamera(cameraMovement);
    }

    private void addMapMarkers() {
        if(mMap == null) {
            return;
        }

        loadWeeklyActions();

        if(mWeeklyJoyGiven != null) {
            for (Give given : mWeeklyJoyGiven) {
                LatLng position = new LatLng(given.getLatitude(), given.getLongitude());

                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                options.position(position);

                Marker marker = mMap.addMarker(options);

                marker.setTag(given);
            }
        }

        if(mWeeklyJoyReceived != null) {
            for (Get received : mWeeklyJoyReceived) {
                LatLng position = new LatLng(received.getLatitude(), received.getLongitude());

                MarkerOptions options = new MarkerOptions();
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                options.position(position);

                Marker marker = mMap.addMarker(options);

                marker.setTag(received);
            }
        }
    }

    private void loadWeeklyActions() {
        JoyUtils joyUtils = new JoyUtils(getActivity());

        mWeeklyJoyGiven = joyUtils.getWeeklyGiven();
        mWeeklyJoyReceived = joyUtils.getWeeklyReceived();
    }
}
