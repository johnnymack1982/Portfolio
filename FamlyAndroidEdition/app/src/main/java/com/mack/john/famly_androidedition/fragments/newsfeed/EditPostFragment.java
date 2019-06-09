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
        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        setClickListener(view);
        setFocusListener(view);
        populate();

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_cancel) {
            getActivity().finish();
        }

        else if(view.getId() == R.id.button_update) {


            mPostInput.clearFocus();

            mCancelButton.setVisibility(View.GONE);
            mUpdateButton.setVisibility(View.GONE);

            if ((mPostInput.getText().toString() != null && mPostInput.getText().toString().trim()!= "")) {
                mPosts.get(mPosition).setPostMessage(mPostInput.getText().toString());

                PostUtils.updatePost(getActivity(), mPost, mPosts);
            }

            getActivity().finish();
        }
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



    // Custom methods
    private void setClickListener(View view) {
        mCancelButton = view.findViewById(R.id.button_cancel);
        mUpdateButton = view.findViewById(R.id.button_update);

        mCancelButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);
    }

    private void setFocusListener(View view) {
        mPostInput = view.findViewById(R.id.input_post);

        mPostInput.setOnFocusChangeListener(this);
    }

    private void populate() {
        Intent sendingIntent = getActivity().getIntent();
        mPosts = (ArrayList<Post>) sendingIntent.getSerializableExtra(NewsfeedAdapter.EXTRA_POSTS);
        mPosition = sendingIntent.getIntExtra(NewsfeedAdapter.EXTRA_POSITION, 0);
        mPost = mPosts.get(mPosition);

        mPostInput.setText(mPost.getPostMessage());
    }
}
