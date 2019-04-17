package com.mack.john.crypjoy_androidedition.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class LocationUtils implements LocationListener {



    // Class properties
    private static final String TAG = "LocationUtils";

    Context mContext;

    LocationManager mLocationManager;

    private double mLatitude;
    private double mLongitude;

    private boolean mRequestingUpdates = false;



    // Constructor
    public LocationUtils(Context context) {
        this.mContext = context;

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        getCurrentLocation();
    }



    // System generated methods


    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        if(mRequestingUpdates) {
            mRequestingUpdates = false;
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}




    // Custom methods
    private void getCurrentLocation() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10.0f, this);

            mRequestingUpdates = true;

            Location currentPosition = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(currentPosition != null) {
                mLatitude = currentPosition.getLatitude();
                mLongitude = currentPosition.getLongitude();
            }
        }
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
