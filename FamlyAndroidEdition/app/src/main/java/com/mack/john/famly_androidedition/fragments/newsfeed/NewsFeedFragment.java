package com.mack.john.famly_androidedition.fragments.newsfeed;

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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.mack.john.famly_androidedition.BuildConfig;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Child;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.post.Post;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.LocationUtils;
import com.mack.john.famly_androidedition.utils.PermissionRequestUtils;
import com.mack.john.famly_androidedition.utils.PostUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsFeedFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class properties
    public static final String TAG = "NewsFeedFragment";

    private String mCameraFilePath;

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private static final int CAMERA_PERMISSION_REQUEST = 3;

    ImageButton mCameraButton;
    Button mCancelButton;
    Button mUpdateButton;

    EditText mPostInput;
    ImageView mPhotoView;

    Bitmap mPostPhoto = null;

    ConstraintLayout mImagePicker;

    ArrayList<Post> mPosts;

    NewsfeedAdapter mNewsfeedAdapter;
    ListView mNewsfeed;



    // System generated methods
    public static NewsFeedFragment newInstance() {
        Bundle args = new Bundle();

        NewsFeedFragment fragment = new NewsFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        PostUtils.listenForNews(getActivity());

        mPosts = PostUtils.loadNewsfeed(getActivity());

        mNewsfeed = view.findViewById(R.id.list_newsfeed);

        mImagePicker = view.findViewById(R.id.picker_image);
        mPhotoView = view.findViewById(R.id.post_image);

        AccountUtils.listenForUpdates(getActivity());

        if(AccountUtils.loadProfile(getActivity()) instanceof Parent) {
            PermissionRequestUtils.receiveRequests(getActivity(), AccountUtils.loadProfile(getActivity()));
        }

        else if(AccountUtils.loadProfile(getActivity()) instanceof Child) {
            PermissionRequestUtils.receiveResponses(getActivity(), AccountUtils.loadProfile(getActivity()));
        }

        setClickListener(view);
        setFocusListener(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPosts = PostUtils.loadNewsfeed(getActivity());
        Account account = AccountUtils.loadAccount(getActivity());

        mNewsfeedAdapter = new NewsfeedAdapter(getActivity(), mPosts, account);
        mNewsfeed.setAdapter(mNewsfeedAdapter);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mUpdateButton.setVisibility(View.VISIBLE);
        }

        else {
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_photo) {
            if(mImagePicker.getVisibility() == View.GONE) {
                mImagePicker.setVisibility(View.VISIBLE);
            }

            else {
                mImagePicker.setVisibility(View.GONE);
            }
        }

        else if(view.getId() == R.id.button_camera) {
            addPhotoFromCamera();
        }

        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        else if(view.getId() == R.id.button_cancel) {
            mPostInput.clearFocus();
            mPostInput.setText("");

            mPhotoView.setVisibility(View.GONE);
            mImagePicker.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            mPostPhoto = null;
        }

        else if(view.getId() == R.id.button_update) {
            mPostInput.clearFocus();

            mPhotoView.setVisibility(View.GONE);
            mImagePicker.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            Profile poster = AccountUtils.loadProfile(getActivity());

            if ((mPostInput.getText().toString() != null && mPostInput.getText().toString().trim()!= "") || mPostPhoto != null) {
                boolean hasImage = false;

                if(mPostPhoto != null) {
                    hasImage = true;
                }

                else {
                    hasImage = false;
                }

                Post newPost = new Post(mPostInput.getText().toString().trim(), new Date(), poster.getProfileId(), poster.getFirstName(), hasImage);

                PostUtils.createPost(getActivity(), newPost, poster, mPostPhoto);
            }

            mPostInput.setText("");
            mPostPhoto = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            mPhotoView.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
            mUpdateButton.setVisibility(View.VISIBLE);

            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    mPhotoView.setImageURI(selectedImage);

                    mPostPhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    break;

                case CAMERA_REQUEST_CODE:
                    mPhotoView.setImageURI(Uri.parse(mCameraFilePath));

                    mPostPhoto = ((BitmapDrawable)mPhotoView.getDrawable()).getBitmap();

                    mPhotoView.setImageBitmap(mPostPhoto);

                    break;
            }
        }
    }




    // Custom methods
    private void setClickListener(View view) {
        mCameraButton = view.findViewById(R.id.button_photo);
        mCancelButton = view.findViewById(R.id.button_cancel);
        mUpdateButton = view.findViewById(R.id.button_update);
        ImageButton cameraButton = view.findViewById(R.id.button_camera);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);

        mCameraButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mPostInput = view.findViewById(R.id.input_post);

        mPostInput.setOnFocusChangeListener(this);
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
