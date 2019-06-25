package com.mack.john.famly_androidedition.fragments.newsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.post.Post;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PostUtils;

import java.util.ArrayList;
import java.util.Date;

public class EditPostFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {



    // Class properties
    public static final String TAG = "EditPostFragment";

    EditText mPostInput;

    Button mCancelButton;
    Button mUpdateButton;

    ArrayList<Post> mPosts;
    Post mPost;

    int mPosition;



    // System generated methods
    public static EditPostFragment newInstance() {
        Bundle args = new Bundle();

        EditPostFragment fragment = new EditPostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        // Call custom methods to set click and focus listeners
        setClickListener(view);
        setFocusListener(view);

        // Call custom method to populate post details
        populate();

        return view;
    }

    @Override
    public void onClick(View view) {
        // If user clicked cancel button, return to previous activity
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        // If user clicked update button...
        else if(view.getId() == R.id.button_update) {

            // Clear input focus
            mPostInput.clearFocus();

            // Hide buttons
            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            // Update post
            if ((mPostInput.getText().toString() != null && mPostInput.getText().toString().trim()!= "")) {
                mPosts.get(mPosition).setPostMessage(mPostInput.getText().toString());

                PostUtils.updatePost(getActivity(), mPost, mPosts);
            }

            // Return to previous activity
            getActivity().finish();
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        // If input field is in focus, show buttons
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



    // Custom methods
    // Custom method to set click listener
    private void setClickListener(View view) {
        mCancelButton = view.findViewById(R.id.button_cancel);
        mUpdateButton = view.findViewById(R.id.button_update);

        mCancelButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
    }

    // Custom method to set focus listener
    private void setFocusListener(View view) {
        mPostInput = view.findViewById(R.id.input_post);

        mPostInput.setOnFocusChangeListener(this);
    }

    // Custom method to populate post details
    private void populate() {

        // Get selected post from sending intent
        Intent sendingIntent = getActivity().getIntent();
        mPosts = (ArrayList<Post>) sendingIntent.getSerializableExtra(NewsfeedAdapter.EXTRA_POSTS);
        mPosition = sendingIntent.getIntExtra(NewsfeedAdapter.EXTRA_POSITION, 0);
        mPost = mPosts.get(mPosition);

        // Populate post message
        mPostInput.setText(mPost.getPostMessage());
    }
}
