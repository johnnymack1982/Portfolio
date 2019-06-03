package com.mack.john.famly_androidedition.fragments.signup.profile.child;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.profile.Child;
import com.mack.john.famly_androidedition.signup.profile.AddProfileActivity;
import com.mack.john.famly_androidedition.utils.AccountUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddChild2Fragment extends Fragment implements View.OnClickListener {



    // Class properties
    public static final String TAG = "AddChild2Fragment";

    private String mCameraFilePath;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;

    ImageView mPhotoView;

    String mFirstName;
    Date mDob;
    int mGenderId;
    int mPin;

    Bitmap mProfilePhoto = null;



    // System generated methods
    public static AddChild2Fragment newInstance() {
        Bundle args = new Bundle();

        AddChild2Fragment fragment = new AddChild2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_child2, container, false);

        mPhotoView = view.findViewById(R.id.profile_photo);

        Intent sendingIntent = getActivity().getIntent();

        mFirstName = sendingIntent.getStringExtra(AddChild1Fragment.EXTRA_FIRST_NAME);
        mDob = (Date) sendingIntent.getSerializableExtra(AddChild1Fragment.EXTRA_DOB);
        mGenderId = sendingIntent.getIntExtra(AddChild1Fragment.EXTRA_GENDER_ID, 0);
        mPin = sendingIntent.getIntExtra(AddChild1Fragment.EXTRA_PIN, 0);

        setClickListener(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_camera) {
            addPhotoFromCamera();
        }

        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        else if(view.getId() == R.id.button_cancel) {
            Intent cancelIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(cancelIntent);
        }

        else if(view.getId() == R.id.button_continue) {
            Child child = new Child(mFirstName, mDob, mGenderId, mPin);

            AccountUtils.addProfile(getContext(), child, mProfilePhoto);


            Intent createIntent = new Intent(getActivity(), AddProfileActivity.class);
            startActivity(createIntent);
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

                    break;

                case CAMERA_REQUEST_CODE:
                    mPhotoView.setImageURI(Uri.parse(mCameraFilePath));

                    mProfilePhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    mPhotoView.setImageBitmap(mProfilePhoto);

                    break;
            }
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        ImageButton cameraButton = view.findViewById(R.id.button_camera);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);
        Button cancelButton = view.findViewById(R.id.button_cancel);
        Button createButton = view.findViewById(R.id.button_continue);

        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
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
