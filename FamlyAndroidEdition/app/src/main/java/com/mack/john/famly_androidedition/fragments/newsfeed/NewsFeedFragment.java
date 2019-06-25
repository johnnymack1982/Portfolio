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
import android.support.v4.widget.SwipeRefreshLayout;
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

    SwipeRefreshLayout mRefresher;



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
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        // Listen for updates to newsfeed
        PostUtils.listenForNews(getActivity());

        // Load current newsfeed
        mPosts = PostUtils.loadNewsfeed(getActivity());

        // Reference newsfeed display and pull-down refresher
        mNewsfeed = view.findViewById(R.id.list_newsfeed);
        mRefresher = view.findViewById(R.id.refresher);

        // Reference image picker and photo view
        mImagePicker = view.findViewById(R.id.picker_image);
        mPhotoView = view.findViewById(R.id.post_image);

        // Listen for account updates
        AccountUtils.listenForUpdates(getActivity());

        // If logged in profile is a parent, listen for incoming permission requests
        if(AccountUtils.loadProfile(getActivity()) instanceof Parent) {
            PermissionRequestUtils.receiveRequests(getActivity(), AccountUtils.loadProfile(getActivity()));
        }

        // If logged in profile is a child, listen for incoming request responnses
        else if(AccountUtils.loadProfile(getActivity()) instanceof Child) {
            PermissionRequestUtils.receiveResponses(getActivity(), AccountUtils.loadProfile(getActivity()));
        }

        // Call custom methods to set click and focus change listeners
        setClickListener(view);
        setFocusListener(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load current newsfeed and logged in account
        mPosts = PostUtils.loadNewsfeed(getActivity());
        Account account = AccountUtils.loadAccount(getActivity());

        // Set newsfeed adapter
        mNewsfeedAdapter = new NewsfeedAdapter(getActivity(), mPosts, account);
        mNewsfeed.setAdapter(mNewsfeedAdapter);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        // If input field has focus, show buttons
        if(hasFocus) {
            mCancelButton.setVisibility(View.VISIBLE);
            mUpdateButton.setVisibility(View.VISIBLE);
        }

        // Otherwise, hide buttons
        else {
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        // If user clicked photo button, toggle image picker
        if(view.getId() == R.id.button_photo) {
            if(mImagePicker.getVisibility() == View.GONE) {
                mImagePicker.setVisibility(View.VISIBLE);
            }

            else {
                mImagePicker.setVisibility(View.GONE);
            }
        }

        // If user clicked camera button, launch camera
        else if(view.getId() == R.id.button_camera) {
            addPhotoFromCamera();
        }

        // If user clicked gallery button, launch gallery picker
        else if(view.getId() == R.id.button_gallery) {
            addPhotoFromGallery();
        }

        // If user clicked cancel button, reset input UI
        else if(view.getId() == R.id.button_cancel) {
            mPostInput.clearFocus();
            mPostInput.setText("");

            mPhotoView.setVisibility(View.GONE);
            mImagePicker.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            mPostPhoto = null;
        }

        // If user clicked update button, create new post and reset UI
        else if(view.getId() == R.id.button_update) {
            mPostInput.clearFocus();

            mPhotoView.setVisibility(View.GONE);
            mImagePicker.setVisibility(View.GONE);
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            // Load logged in profile
            Profile poster = AccountUtils.loadProfile(getActivity());

            // Only continue if acceptable input exists
            if ((mPostInput.getText().toString() != null && mPostInput.getText().toString().trim()!= "") || mPostPhoto != null) {
                boolean hasImage = false;

                // Indicate whether or not post has a photo
                if(mPostPhoto != null) {
                    hasImage = true;
                }

                else {
                    hasImage = false;
                }

                // Create new post object
                Post newPost = new Post(mPostInput.getText().toString().trim(), new Date(), poster.getProfileId(), poster.getFirstName(), hasImage);

                // Add post to family newsfeed
                PostUtils.createPost(getActivity(), newPost, poster, mPostPhoto);
            }

            mPostInput.setText("");
            mPostPhoto = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If a valid image was picked, populate in UI
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
    // Custom method to set click listener
    private void setClickListener(View view) {
        // Reference all buttons in layout
        mCameraButton = view.findViewById(R.id.button_photo);
        mCancelButton = view.findViewById(R.id.button_cancel);
        mUpdateButton = view.findViewById(R.id.button_update);
        ImageButton cameraButton = view.findViewById(R.id.button_camera);
        ImageButton galleryButton = view.findViewById(R.id.button_gallery);

        // Set click listener for buttons
        mCameraButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        galleryButton.setOnClickListener(this);

        // Set pull-down refresh listener
        mRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh newsfeed and update display
                mPosts = PostUtils.loadNewsfeed(getActivity());
                Account account = AccountUtils.loadAccount(getActivity());

                mNewsfeedAdapter = new NewsfeedAdapter(getActivity(), mPosts, account);
                mNewsfeed.setAdapter(mNewsfeedAdapter);
                mRefresher.setRefreshing(false);
            }
        });
    }

    // Custom method to set input field focus listener
    private void setFocusListener(View view) {
        mPostInput = view.findViewById(R.id.input_post);

        mPostInput.setOnFocusChangeListener(this);
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
        // Check for required permissions...if they don't exist, request them
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
