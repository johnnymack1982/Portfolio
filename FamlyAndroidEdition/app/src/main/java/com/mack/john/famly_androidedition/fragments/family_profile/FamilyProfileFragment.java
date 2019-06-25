package com.mack.john.famly_androidedition.fragments.family_profile;

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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ProfilesAdapter;
import com.mack.john.famly_androidedition.family_profile.EditFamilyActivity;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;
import com.mack.john.famly_androidedition.utils.AccountUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FamilyProfileFragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "FamilyProfileFragment";

    private String mCameraFilePath;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;

    public static final String ACTION_EDIT_PROFILE = "action_edit_profile";

    public static final String EXTRA_PROFILE = "extra_profile";

    ImageView mPhotoView;

    Account mAccount;

    Bitmap mFamilyPhoto = null;



    // System generated methods
    public static FamilyProfileFragment newInstance() {
        Bundle args = new Bundle();

        FamilyProfileFragment fragment = new FamilyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view lauout
        View view = inflater.inflate(R.layout.fragment_family_profile, container, false);

        // Reference logged in account
        mAccount = AccountUtils.loadAccount(getActivity());

        // Reference family photo display
        mPhotoView = view.findViewById(R.id.profile_photo);

        // Call custom method to set click listener
        setClickListener(view);

        // Call custom method to populate profile display
        populateProfile(view);

        // Call custom method to populate profile grid
        populateGrid(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked delete family button...
        if(view.getId() == R.id.button_delete_family) {
            // Build confirmation alert
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.delete_account))
                    .setMessage(getString(R.string.delete_account_confirm))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Delete account and return to login activity
                            AccountUtils.deleteAccount(getActivity());

                            Intent logoutIntent = new Intent(getActivity(), MasterLoginActivity.class);
                            getActivity().startActivity(logoutIntent);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            // Show confirmation alert
            AlertDialog confirmDialog = alertDialogBuilder.create();
            confirmDialog.show();
        }

        // If user clicked edit family button, launch edit family activity
        else if(view.getId() == R.id.button_edit_family){
            Intent editIntent = new Intent(getActivity(), EditFamilyActivity.class);
            startActivity(editIntent);
        }

        // if user clicked camera button, launch camera
        else if(view.getId() == R.id.button_photo) {
            addPhotoFromCamera();
        }

        // If user clicked gallery button, launch gallery picker
        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        // If user clicked add profile button, launch add profile activity
        else if(view.getId() == R.id.button_add) {
            Intent addIntent = new Intent(getActivity(), AddProfileActivity.class);
            getActivity().startActivity(addIntent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Set family photo to photo take from camera or gallery
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mPhotoView.setImageURI(selectedImage);

                    mFamilyPhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    AccountUtils.saveAccount(getActivity(), mAccount, mFamilyPhoto);
                    AccountUtils.uploadFamilyPhoto(mFamilyPhoto);

                    break;

                case CAMERA_REQUEST_CODE:
                    mPhotoView.setImageURI(Uri.parse(mCameraFilePath));

                    mFamilyPhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    AccountUtils.saveAccount(getActivity(), mAccount, mFamilyPhoto);
                    AccountUtils.uploadFamilyPhoto(mFamilyPhoto);

                    mPhotoView.setImageBitmap(mFamilyPhoto);

                    break;
            }
        }
    }

    // Custom methods
    // Custom method to set click listeer
    private void setClickListener(View view) {
        // Reference all buttons in layout
        ImageButton deleteFamilyButton = view.findViewById(R.id.button_delete_family);
        ImageButton editFamilyButton = view.findViewById(R.id.button_edit_family);
        ImageButton cameraButton = view.findViewById(R.id.button_photo);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);
        FloatingActionButton addButton = view.findViewById(R.id.button_add);

        // Set click listener for buttons
        deleteFamilyButton.setOnClickListener(this);
        editFamilyButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
    }

    // Custom method to populate profile display
    private void populateProfile(View view) {
        // Reference display elemets
        ImageView familyPhoto = view.findViewById(R.id.profile_photo);
        TextView familyNameDisplay = view.findViewById(R.id.display_family_name);
        TextView addressDisplay = view.findViewById(R.id.display_address);

        // Get family name
        String familyName = mAccount.getFamilyName() + " " + getString(R.string.family);

        // Populate family details
        AccountUtils.loadAccountPhoto(getActivity(), familyPhoto);
        familyNameDisplay.setText(familyName);
        addressDisplay.setText(mAccount.getFullAddress());
    }

    // Custom method to add photo from gallery picker
    private void addPhotoFromGallery() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        photoIntent.setType("image/*");

        String[] mimeTypes = {"image/jpeg", "image/png"};
        photoIntent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        startActivityForResult(photoIntent, 1);
    }

    // Custom method to add photo from camera
    private void addPhotoFromCamera() {
        // Check for required permissions..if they don't exist, request them
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST);
        }

        // If required permissions exist...
        else {
            try {
                // Start image picker intent and wait for result
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

    // Custom method to populate profile grid
    private void populateGrid(View view) {
        // Reference all profiles on account
        final Profile[] profiles = mAccount.getProfiles().toArray(new Profile[mAccount.getProfiles().size()]);

        // Referece gridview
        GridView profileGrid = view.findViewById(R.id.grid_family);

        // Set grid adapter
        ProfilesAdapter profilesAdapter = new ProfilesAdapter(getActivity(), profiles, mAccount);
        profileGrid.setAdapter(profilesAdapter);

        // Set grid click listener
        profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Reference selected profile
                Profile profile = profiles[position];

                // Launch profile intent and send selected profile
                Intent editIntent = new Intent(getActivity(), NavigationActivity.class);
                editIntent.setAction(ACTION_EDIT_PROFILE);
                editIntent.putExtra(EXTRA_PROFILE, profile);

                getActivity().startActivity(editIntent);
            }
        });
    }
}
