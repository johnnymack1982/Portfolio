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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.family_profile.FamilyProfileActivity;
import com.mack.john.famly_androidedition.fragments.family_profile.FamilyProfileFragment;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "ProfileFragment";

    private String mCameraFilePath;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;

     ImageButton mDeleteProfileButton;
     ImageButton mFamilyButton;

     TextView mNameDisplay;
     TextView mDateTimeDisplay;
     TextView mLastLocationDisplay;

     boolean mEditingSelf;
     boolean mIsParent;

     Account mAccount;
     Profile mProfile;

    ImageView mPhotoView;

    Bitmap mProfilePhoto = null;



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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            if(getActivity().getIntent() != null && getActivity().getIntent().getAction().equals(FamilyProfileFragment.ACTION_EDIT_PROFILE)) {
                mEditingSelf = false;
                mIsParent = true;
                mProfile = (Profile) getActivity().getIntent().getSerializableExtra(FamilyProfileFragment.EXTRA_PROFILE);

                String selectedName = mProfile.getFirstName();
                String loadedName = AccountUtils.loadProfile(getActivity()).getFirstName();

                if(selectedName.equals(loadedName)) {
                    mEditingSelf = true;
                }
            }

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

            mEditingSelf = true;
            mProfile = AccountUtils.loadProfile(getActivity());

            if(mProfile instanceof Parent) {
                mIsParent = true;
            }

            else {
                mIsParent = false;
            }
        }

        mAccount = AccountUtils.loadAccount(getActivity());
        AccountUtils.loadProfilePhoto(getActivity(), view, mProfile);

        mPhotoView = view.findViewById(R.id.profile_photo);

        setClickListener(view);
        setTextDisplay(view);
        populateProfile(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_delete_profile) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.delete_profile))
                    .setMessage(getString(R.string.confirm_delete_1) + " " + mProfile.getFirstName() + getString(R.string.confirm_delete_2))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountUtils.deleteProfile(getActivity(), mEditingSelf, mProfile, mAccount);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog confirmDialog = alertDialogBuilder.create();
            confirmDialog.show();
        }

        else if(view.getId() == R.id.button_camera) {
            addPhotoFromCamera();
        }

        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        else if(view.getId() == R.id.button_family) {
            Intent familyIntent = new Intent(getActivity(), FamilyProfileActivity.class);
            startActivity(familyIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
    private void setClickListener(View view) {
        mDeleteProfileButton = view.findViewById(R.id.button_delete_profile);
        mFamilyButton = view.findViewById(R.id.button_family);
        ImageButton cameraButton = view.findViewById(R.id.button_camera);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);


        mDeleteProfileButton.setOnClickListener(this);
        mFamilyButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
    }

    private void setTextDisplay(View view) {
        mNameDisplay = view.findViewById(R.id.display_name);
        mDateTimeDisplay = view.findViewById(R.id.display_timestamp);
        mLastLocationDisplay = view.findViewById(R.id.display_last_location);
    }

    private void populateProfile(View view) {
        TextView profileName = view.findViewById(R.id.display_name);
        TextView timeStamp = view.findViewById(R.id.display_timestamp);
        TextView lastKnwonLocation = view.findViewById(R.id.display_last_location);
        ImageButton deleteProfileButton = view.findViewById(R.id.button_delete_profile);
        ImageButton familyButton = view.findViewById(R.id.button_family);

        if(!mIsParent) {
            deleteProfileButton.setVisibility(View.GONE);
            familyButton.setVisibility(View.GONE);
        }

        String name = mProfile.getFirstName() + " " + mAccount.getFamilyName();
        profileName.setText(name);
    }

    private void addPhotoFromGallery() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        photoIntent.setType("image/*");

        String[] mimeTypes = {"image/jpeg", "image/png"};
        photoIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        startActivityForResult(photoIntent, 1);
    }

    private void addPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
        }

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
