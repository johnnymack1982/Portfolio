package com.mack.john.famly_androidedition.fragments.newsfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;

public class FullScreenPhotoFragment extends Fragment {



    // Class properties
    public static final String TAG = "FullScreenPhotoFragment";

    Bitmap mPhoto;



    // System generated methods
    public static FullScreenPhotoFragment newInstance() {
        Bundle args = new Bundle();

        FullScreenPhotoFragment fragment = new FullScreenPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_photo, container, false);

        Intent sendingIntent = getActivity().getIntent();
        mPhoto = BitmapFactory.decodeByteArray(sendingIntent.getByteArrayExtra(NewsfeedAdapter.EXTRA_PHOTO), 0,
                sendingIntent.getByteArrayExtra(NewsfeedAdapter.EXTRA_PHOTO).length);

        ImageView postPhoto = view.findViewById(R.id.post_photo);
        postPhoto.setImageBitmap(mPhoto);

        return view;
    }
}
