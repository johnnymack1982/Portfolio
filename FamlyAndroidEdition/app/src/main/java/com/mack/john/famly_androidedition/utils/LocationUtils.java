package com.mack.john.famly_androidedition.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.post.Post;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationUtils implements LocationListener {



    // Class properties
    private static final String TAG = "LocationUtils";

    Context mContext;

    private final LocationManager mLocationManager;

    private double mLatitude;
    private double mLongitude;
    
    private double mProfileLatitude;
    private double mProfileLongitude;

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

        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final Date date = new Date();

        Map<String, Object> checkinMap = new HashMap<>();
        checkinMap.put("latitude", mLatitude);
        checkinMap.put("longitude", mLongitude);
        checkinMap.put("checkinTimestamp", date);

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + AccountUtils.loadProfile(mContext).getProfileId()).collection("location")
                .document("location").set(checkinMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Log.i(TAG, "onComplete: Location Updated Successfully");
                }
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    // Custom methods
    private void getCurrentLocation() {
        // If required permissions have been granted, continue
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Start requesting location updates
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10.0f, this);
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
    
    public void getProfileLocation(final Profile profile, final GoogleMap map) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("location")
                .document("location").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(map == null) {
                        return;
                    }

                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");

                        // Reference current latitude and longitude
                        LatLng currentLocation = new LatLng(latitude, longitude);

                        // Zoom camera in to current location
                        CameraUpdate cameraMovement = CameraUpdateFactory.newLatLngZoom(currentLocation, 15);
                        map.animateCamera(cameraMovement);

                        @SuppressLint("ResourceType")
                        String green = mContext.getResources().getString(R.color.colorGrassPrimary);

                        MarkerOptions options = new MarkerOptions();
                        options.title(AccountUtils.loadProfile(mContext).getFirstName() + " " + AccountUtils.loadAccount(mContext).getFamilyName());

                        options.snippet(convertAddress(mContext, latitude, longitude));

                        options.position(currentLocation);
                        options.icon(setMarkerColor(green));

                        map.addMarker(options);
                    }

                    else {
                        Toast.makeText(mContext, mContext.getString(R.string.location_info_for) + " " + profile.getFirstName() + " "
                                + mContext.getString(R.string.is_unavailable), Toast.LENGTH_SHORT).show();
                    }
                }
                
                else {
                    Toast.makeText(mContext, mContext.getString(R.string.location_info_for) + " " + profile.getFirstName() + " "
                            + mContext.getString(R.string.is_unavailable), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Custom method to return current location latitude
    public double getLatitude() {
        return mLatitude;
    }

    // Custom method to return current location longitude
    public double getLongitude() {
        return mLongitude;
    }

    public void checkIn(final Context context, final double latitude, final double longitude, final String message, final Bitmap image) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final Date date = new Date();

        Map<String, Object> checkinMap = new HashMap<>();
        checkinMap.put("latitude", latitude);
        checkinMap.put("longitude", longitude);
        checkinMap.put("checkinTimestamp", date);

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + AccountUtils.loadProfile(context).getProfileId()).collection("location")
                .document("location").set(checkinMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String address = convertAddress(context, latitude, longitude);

                Profile profile = AccountUtils.loadProfile(context);

                boolean hasImage;

                if(image != null) {
                    hasImage = true;
                }

                else {
                    hasImage = false;
                }

                if(message == null || message.trim().equals("")) {
                    String postMesage = AccountUtils.loadProfile(context).getFirstName() + " " + context.getString(R.string.checked_in_at) + " " + address;

                    Post post = new Post(postMesage, date, profile.getProfileId(), profile.getFirstName(), hasImage);
                    PostUtils.createPost(context, post, profile, image);
                }
                
                else {
                    String postMessage = AccountUtils.loadProfile(context).getFirstName() + " " + context.getString(R.string.checked_in_at) + " " + address
                            + " " + context.getString(R.string.and_says) + " " + message + "'";

                    Post post = new Post(postMessage, date, profile.getProfileId(), profile.getFirstName(), hasImage);
                    PostUtils.createPost(context, post, profile, image);
                }
            }
        });
    }

    public void updateLocationDisplay(final Context context, View view, final Profile profile) {
        final TextView timeStampDisplay = view.findViewById(R.id.display_timestamp);
        final TextView lastKnownLocationDisplay = view.findViewById(R.id.display_last_location);

        timeStampDisplay.setText("");
        lastKnownLocationDisplay.setText("");

        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("location")
                .document("location").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {
                        Date checkinTimestamp = document.getDate("checkinTimestamp");
                        double latitude = document.getDouble("latitude");
                        double longitude = document.getDouble("longitude");
                        String address = convertAddress(context, latitude, longitude);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy @ hh:mm a", Locale.getDefault());
                        String timestamp = dateFormat.format(checkinTimestamp);

                        timeStampDisplay.setText(timestamp);
                        lastKnownLocationDisplay.setText(address);
                    }

                    else {
                        Toast.makeText(mContext, mContext.getString(R.string.location_info_for) + " " + profile.getFirstName() + " "
                                + mContext.getString(R.string.is_unavailable), Toast.LENGTH_SHORT).show();
                        
                        timeStampDisplay.setText("");
                        lastKnownLocationDisplay.setText(context.getString(R.string.unknown));
                    }
                }

                else {
                    Toast.makeText(mContext, mContext.getString(R.string.location_info_for) + " " + profile.getFirstName() + " "
                            + mContext.getString(R.string.is_unavailable), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String convertAddress(Context context, double latitude, double longitude) {
        String addressString = "Unknown Address";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = addressList.get(0);

            String streetAddress = address.getAddressLine(0);

            addressString = streetAddress;
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return addressString;
    }

    private BitmapDescriptor setMarkerColor(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
