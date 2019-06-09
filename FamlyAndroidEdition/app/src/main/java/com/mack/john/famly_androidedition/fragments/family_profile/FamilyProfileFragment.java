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
        View view = inflater.inflate(R.layout.fragment_family_profile, container, false);

        mAccount = AccountUtils.loadAccount(getActivity());

        mPhotoView = view.findViewById(R.id.profile_photo);

        setClickListener(view);
        populateProfile(view);
        populateGrid(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_delete_family) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle(getString(R.string.delete_account))
                    .setMessage(getString(R.string.delete_account_confirm))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

            AlertDialog confirmDialog = alertDialogBuilder.create();
            confirmDialog.show();
        }

        else if(view.getId() == R.id.button_edit_family){
            Intent editIntent = new Intent(getActivity(), EditFamilyActivity.class);
            startActivity(editIntent);
        }

        else if(view.getId() == R.id.button_photo) {
            addPhotoFromCamera();
        }

        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
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
    private void setClickListener(View view) {
        ImageButton deleteFamilyButton = view.findViewById(R.id.button_delete_family);
        ImageButton editFamilyButton = view.findViewById(R.id.button_edit_family);
        ImageButton cameraButton = view.findViewById(R.id.button_photo);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);

        deleteFamilyButton.setOnClickListener(this);
        editFamilyButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
    }

    private void populateProfile(View view) {
        ImageView familyPhoto = view.findViewById(R.id.profile_photo);
        TextView familyNameDisplay = view.findViewById(R.id.display_family_name);
        TextView addressDisplay = view.findViewById(R.id.display_address);

        String familyName = mAccount.getFamilyName() + " " + getString(R.string.family);

        AccountUtils.loadAccountPhoto(getActivity(), familyPhoto);
        familyNameDisplay.setText(familyName);
        addressDisplay.setText(mAccount.getFullAddress());
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

    private void populateGrid(View view) {
        final Profile[] profiles = mAccount.getProfiles().toArray(new Profile[mAccount.getProfiles().size()]);

        GridView profileGrid = view.findViewById(R.id.grid_family);

        ProfilesAdapter profilesAdapter = new ProfilesAdapter(getActivity(), profiles, mAccount);
        profileGrid.setAdapter(profilesAdapter);
        profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Profile profile = profiles[position];

                Intent editIntent = new Intent(getActivity(), NavigationActivity.class);
                editIntent.setAction(ACTION_EDIT_PROFILE);
                editIntent.putExtra(EXTRA_PROFILE, profile);

                getActivity().startActivity(editIntent);
            }
        });
    }
}
