package com.mack.john.famly_androidedition.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;
import com.mack.john.famly_androidedition.family_profile.FamilyProfileActivity;
import com.mack.john.famly_androidedition.fragments.family_profile.FamilyProfileFragment;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.post.Post;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;
import com.mack.john.famly_androidedition.utils.PostUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ProfileFragment";

    private String mCameraFilePath;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;

    View mView;

     ImageButton mDeleteProfileButton;
     ImageButton mFamilyButton;

     TextView mNameDisplay;
     TextView mDateTimeDisplay;
     TextView mLastLocationDisplay;

     boolean mEditingSelf;
     boolean mIsParent;

     Account mAccount;
     Profile mProfile;

     ArrayList<Post> mPosts;

    ImageView mPhotoView;

    Bitmap mProfilePhoto = null;

    NewsfeedAdapter mNewsfeedAdapter;

    ListView mTimeline;

    SwipeRefreshLayout mRefresher;



    // System generated methods
    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Reference view
        mView = view;

        try {
            // If selected is not the logged in profile but user is a parent...
            if(getActivity().getIntent() != null && getActivity().getIntent().getAction().equals(FamilyProfileFragment.ACTION_EDIT_PROFILE)) {
                mEditingSelf = false;
                mIsParent = true;

                // Get profile from sending intent
                mProfile = (Profile) getActivity().getIntent().getSerializableExtra(FamilyProfileFragment.EXTRA_PROFILE);

                // Get selected profile name and compare to logged in profile name
                String selectedName = mProfile.getFirstName();
                String loadedName = AccountUtils.loadProfile(getActivity()).getFirstName();

                // If names match, indicate that the parent is viewing their own profile
                if(selectedName.equals(loadedName)) {
                    mEditingSelf = true;
                }
            }

            // Otherwise, indicate user is not viewing their own profile
            else {
                mEditingSelf = true;
                mProfile = AccountUtils.loadProfile(getActivity());

                if(mProfile instanceof Parent) {
                    mIsParent = true;
                }

                else {
                    mIsParent = false;
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();

            // Default to viewing own profile
            mEditingSelf = true;
            mProfile = AccountUtils.loadProfile(getActivity());

            // If logged in profile is a parent, indicate that
            if(mProfile instanceof Parent) {
                mIsParent = true;
            }

            // Otherwise, indicate that logged in profile is not a parent
            else {
                mIsParent = false;
            }
        }

        // Load account
        mAccount = AccountUtils.loadAccount(getActivity());

        // Load profile photo
        AccountUtils.loadProfilePhoto(getActivity(), view, mProfile.getProfileId());

        // Reference photo view, timeline, and pull-down refresher
        mPhotoView = view.findViewById(R.id.profile_photo);
        mTimeline = view.findViewById(R.id.list_newsfeed);
        mRefresher = view.findViewById(R.id.refresher);

        // Call custom methods to set click listener, set text display, and populate profile display
        setClickListener(view);
        setTextDisplay(view);
        populateProfile(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Listen for updates to profile timeline
        PostUtils.listenForTimeline(getActivity(), mProfile, mAccount, mTimeline);

        // Update location display
        LocationUtils locationUtils = new LocationUtils(getActivity());
        locationUtils.updateLocationDisplay(getActivity(), mView, mProfile);
    }

    @Override
    public void onClick(View view) {
        // If user clicked delete profile button...
        if(view.getId() == R.id.button_delete_profile) {
            // Build confirmation alert
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.delete_profile))
                    .setMessage(getString(R.string.confirm_delete_1) + " " + mProfile.getFirstName() + getString(R.string.confirm_delete_2))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete profile
                            AccountUtils.deleteProfile(getActivity(), mEditingSelf, mProfile, mAccount);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Show confirm alert
            AlertDialog confirmDialog = alertDialogBuilder.create();
            confirmDialog.show();
        }

        // If user clicked camera button, call custom method to add photo from camera
        else if(view.getId() == R.id.button_photo) {
            addPhotoFromCamera();
        }

        // If user clicked gallery button, call custom method to add photo from gallery
        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        // If user clicked family button, launch family profile activity
        else if(view.getId() == R.id.button_family) {
            Intent familyIntent = new Intent(getActivity(), FamilyProfileActivity.class);
            startActivity(familyIntent);
        }

        // If user clicked loggout button...
        else if (view.getId() == R.id.button_logout) {
            // Build confirm alert
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Log Out?");
            builder.setMessage("Are you sure you want to log out of this profile?");
            builder.setCancelable(true);

            // If user clicked signout button, sign out
            builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AccountUtils.signout(getActivity());
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Show cofirm alert
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Populate UI with selected image
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mPhotoView.setImageURI(selectedImage);

                    mProfilePhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    AccountUtils.saveAccount(getActivity(), mAccount, mProfilePhoto);
                    AccountUtils.uploadProfilePhoto(mProfile, mProfilePhoto);

                    break;

                case CAMERA_REQUEST_CODE:
                    mPhotoView.setImageURI(Uri.parse(mCameraFilePath));

                    mProfilePhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    AccountUtils.saveAccount(getActivity(), mAccount, mProfilePhoto);
                    AccountUtils.uploadProfilePhoto(mProfile, mProfilePhoto);

                    mPhotoView.setImageBitmap(mProfilePhoto);

                    break;
            }
        }
    }



    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference all buttons in layout
        mDeleteProfileButton = view.findViewById(R.id.button_delete_profile);
        mFamilyButton = view.findViewById(R.id.button_family);
        ImageButton cameraButton = view.findViewById(R.id.button_photo);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);
        Button logoutButton = view.findViewById(R.id.button_logout);

        // If displayed profile is the logged in profile, show the lgout button
        if(mProfile.getProfileId().equals(AccountUtils.loadProfile(getActivity()).getProfileId())) {
            logoutButton.setVisibility(View.VISIBLE);
        }

        // Otherwise, hide the logout button
        else {
            logoutButton.setVisibility(View.INVISIBLE);
        }


        // Set button click listeers
        mDeleteProfileButton.setOnClickListener(this);
        mFamilyButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        // Set pull-down refresh listener
        mRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PostUtils.listenForTimeline(getActivity(), mProfile, mAccount, mTimeline);

                mRefresher.setRefreshing(false);
            }
        });
    }

    // Custom method to display text elements
    private void setTextDisplay(View view) {
        mNameDisplay = view.findViewById(R.id.display_name);
        mDateTimeDisplay = view.findViewById(R.id.display_timestamp);
        mLastLocationDisplay = view.findViewById(R.id.display_last_location);
    }

    // Custom method to populate profile display
    private void populateProfile(View view) {
        // Reference UI elements
        TextView profileName = view.findViewById(R.id.display_name);
        ImageButton deleteProfileButton = view.findViewById(R.id.button_delete_profile);
        ImageButton familyButton = view.findViewById(R.id.button_family);

        // If logged in profile is not a parent, hide family and delete profile buttons
        if(!mIsParent) {
            deleteProfileButton.setVisibility(View.GONE);
            familyButton.setVisibility(View.GONE);
        }

        // Display profile name
        String name = mProfile.getFirstName() + " " + mAccount.getFamilyName();
        profileName.setText(name);

        // Display profile's last known location
        LocationUtils locationUtils = new LocationUtils(getActivity());
        locationUtils.updateLocationDisplay(getActivity(), view, mProfile);
    }

    // Custom method to add photo from gallery
    private void addPhotoFromGallery() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        photoIntent.setType("image/*");

        String[] mimeTypes = {"image/jpeg", "image/png"};
        photoIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        startActivityForResult(photoIntent, 1);
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
