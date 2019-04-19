package com.mack.john.crypjoy_androidedition.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class LocationUtils implements LocationListener {


    private final Context mContext;

    private final LocationManager mLocationManager;

    private double mLatitude;
    private double mLongitude;

    private boolean mRequestingUpdates = false;



    // Constructor
    public LocationUtils(Context context) {
        this.mContext = context;

        // Instantiate location manager and call custom method to get current location
        this.mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();
    }



    // System generated methods
    @Override
    public void onLocationChanged(Location location) {
        // Get current latitude and longitude
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

        // If currently requesting updates, stop
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
        // If required permissions have been granted, continue
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // Start requesting location updates
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10.0f, this);
            mRequestingUpdates = true;

            // Pull current location
            Location currentPosition = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // If current location is available, extract latitude and longitude
            if(currentPosition != null) {
                mLatitude = currentPosition.getLatitude();
                mLongitude = currentPosition.getLongitude();
            }
        }
    }

    // Custom method to return current location latitude
    public double getLatitude() {
        return mLatitude;
    }

    // Custom method to return current location longitude
    public double getLongitude() {
        return mLongitude;
    }
}
