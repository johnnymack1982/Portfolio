package com.mack.john.famly_androidedition.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ProfilesAdapter;
import com.mack.john.famly_androidedition.fragments.locate.CheckinMapFragment;
import com.mack.john.famly_androidedition.locate.LocateSelectionActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckinFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "CheckinFragment";

    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;
    private static final int LOCATION_PERMISSION_REQUEST = 4;

    private String mCameraFilePath;

    EditText mCheckinInput;

    ImageButton mCameraButton;
    Button mCheckinButton;
    Button mFindButton;

    ImageView mCheckinImageView;

    Bitmap mCheckinImage = null;

    private double mCurrentLat;
    private double mCurrentLong;

    View mImagePicker;



    // System generated methods
    public static CheckinFragment newInstance() {
        Bundle args = new Bundle();

        CheckinFragment fragment = new CheckinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);

        // If required permissions don't exist, request them
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
        }

        // Inflate map fragmet
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_map_fragment, CheckinMapFragment.newInstance(), CheckinMapFragment.TAG)
                .commit();

        // Call custom method to set click listener
        setClickListener(view);

        // Reference UI elemets
        mCheckinInput = view.findViewById(R.id.input_checkin);
        mCheckinImageView = view.findViewById(R.id.checkin_image);
        mImagePicker = view.findViewById(R.id.picker_image);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked checkin button, call custom method to create checkin
        if(view.getId() == R.id.button_checkin) {
            checkIn();
        }

        // If user clicked find button, launch find activity
        else if(view.getId() == R.id.button_find) {
            Intent findIntent = new Intent(getActivity(), LocateSelectionActivity.class);
            startActivity(findIntent);
        }

        // If user clicked camera button...
        else if(view.getId() == R.id.button_photo) {

            // If no photo exists, call cusom method to add photo from camera
            if(mCheckinImageView.getDrawable() == null) {
                addPhotoFromCamera();
            }

            // Otherwise hide image view
            else {
                mCheckinImageView.setImageDrawable(null);
                mCheckinImageView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Populate UI with selected image
        if(resultCode == Activity.RESULT_OK) {
            mCheckinImageView.setVisibility(View.VISIBLE);

            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    mCheckinImageView.setImageURI(Uri.parse(mCameraFilePath));

                    mCheckinImage = ((BitmapDrawable)mCheckinImageView.getDrawable()).getBitmap();

                    mCheckinImageView.setImageBitmap(mCheckinImage);

                    break;
            }
        }
    }

    // Custom methods
    // Custom method to set click listener
    private void  setClickListener(View view) {
        mCameraButton = view.findViewById(R.id.button_photo);
        mCheckinButton = view.findViewById(R.id.button_checkin);
        mFindButton = view.findViewById(R.id.button_find);

        mCameraButton.setOnClickListener(this);
        mCheckinButton.setOnClickListener(this);
        mFindButton.setOnClickListener(this);
    }

    // Custom method to create checkin
    private void checkIn() {
        // Instantiate location utilities class
        LocationUtils locationUtils = new LocationUtils(getActivity());

        // Hide image view
        mCheckinImageView.setVisibility(View.GONE);

        // Get current location data
        mCurrentLat = locationUtils.getLatitude();
        mCurrentLong = locationUtils.getLongitude();

        // Track checkin message
        String checkinMessage = "";

        // Reference checkin message innput
        if(mCheckinInput.getText().toString() != null && mCheckinInput.getText().toString() != "") {
            checkinMessage = mCheckinInput.getText().toString();
        }

        // Attempt to create checkin
        locationUtils.checkIn(getActivity(), mCurrentLat, mCurrentLong, mCheckinInput.getText().toString(), mCheckinImage);
    }

    // Custom method to add photo from camera
    private void addPhotoFromCamera() {
        // If required permissions don't exist, request them
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
        }

        // If required permissions exist, launch camera and wait for result
        else {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile()));

                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Custom method to create image file
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for using again
        mCameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }
}
