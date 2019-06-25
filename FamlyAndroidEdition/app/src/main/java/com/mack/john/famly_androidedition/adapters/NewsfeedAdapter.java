package com.mack.john.famly_androidedition.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.newsfeed.EditPostActivity;
import com.mack.john.famly_androidedition.newsfeed.FullScreenPhotoActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.post.Post;
import com.mack.john.famly_androidedition.utils.AccountUtils;
import com.mack.john.famly_androidedition.utils.PostUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NewsfeedAdapter extends BaseAdapter {



    // Class properties
    private static final String TAG = "NewsfeedAdapter";

    public static final String EXTRA_POSTS = "extra_posts";
    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_PHOTO = "extra_photo";
    
    private final Context context;
    ArrayList<Post> posts;
    Account account;



    // Constructor
    public NewsfeedAdapter(Context context, ArrayList<Post> posts, Account account) {
        this.context = context;
        this.posts = posts;
        this.account = account;
    }



    // System generated methods

    @Override
    public int getCount() {
        // Return post list size
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        // Return position or current item
        return position;
    }

    @Override
    public Object getItem(int position) {
        // Return current item
        return posts.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Reference current post
        final Post post = posts.get(position);

        // Inflate cell
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cell_newsfeed, null);
        }

        // Display profile name
        TextView profileNameDisplay = convertView.findViewById(R.id.display_profile_name);
        String name = post.getPosterName() + " " + account.getFamilyName();
        profileNameDisplay.setText(name);

        // Display timestamp
        TextView timestampDisplay = convertView.findViewById(R.id.display_timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy @ hh:mm a", Locale.getDefault());
        String timestamp = dateFormat.format(post.getTimeStamp());
        timestampDisplay.setText(timestamp);

        // Display post message
        TextView postMessageDisplay = convertView.findViewById(R.id.display_post_message);
        postMessageDisplay.setText(post.getPostMessage());

        // Display profile photo
        AccountUtils.loadProfilePhoto(context, convertView, post.getPosterId());

        // Show image display if the post has an image
        if (post.getHasImage()) {
            ImageView postImage = convertView.findViewById(R.id.display_post_image);
            postImage.setVisibility(View.GONE);
        }

        // Hide the image display if the post has no image
        else {
            ImageView postImage = convertView.findViewById(R.id.display_post_image);
            postImage.setVisibility(View.GONE);
        }

        // Load post image
        PostUtils.loadPostImage(convertView, post.getPostId());

        // Reference delete and edit buttons
        ImageButton deleteButton = convertView.findViewById(R.id.button_delete_post);
        ImageButton editButton = convertView.findViewById(R.id.button_edit_post);

        // Call custom method to delete and edit buttons depending on age o post
        toggleButtons(post, editButton, deleteButton);

        // Set delete button listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build alert
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle(context.getString(R.string.delete_post));
                alertBuilder.setMessage(context.getString(R.string.delete_post_message));

                // Add delete button to alert
                alertBuilder.setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Remove post and update post list
                        posts.remove(position);
                        PostUtils.deletePost(context, post.getPostId(), post.getPosterId());
                        notifyDataSetChanged();
                        PostUtils.listenForNews(context);
                    }
                });

                // Add cancel button to alert
                alertBuilder.setNegativeButton(context.getString(R.string.cancel), null);

                // Show alert
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });

        // Set edit button listener
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Send current post to edit activity and launch
                Intent editIntent = new Intent(context, EditPostActivity.class);
                editIntent.putExtra(EXTRA_POSTS, posts);
                editIntent.putExtra(EXTRA_POSITION, position);

                context.startActivity(editIntent);
            }
        });

        try {
            // Get bitmap rom selected image
            final ImageView postImageDisplay = convertView.findViewById(R.id.display_post_image);
            BitmapDrawable drawable = (BitmapDrawable) postImageDisplay.getDrawable();
            final Bitmap image = drawable.getBitmap();

            // Compress bimap
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

            // Set image click listener
            postImageDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Send image to full-screen activity and launch
                    Intent fullScreenIntent = new Intent(context, FullScreenPhotoActivity.class);
                    fullScreenIntent.putExtra(EXTRA_PHOTO, byteArrayOutputStream.toByteArray());

                    context.startActivity(fullScreenIntent);
                }
            });
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        // Return cell
        return convertView;
    }



    // Custom methods
    // Custom method to toggle delete and edit buttons based on age of post
    private void toggleButtons(Post post, ImageButton editButton, ImageButton deleteButton) {
        // Reference two minutes from post timestamp
        long twoMinutes = System.currentTimeMillis() - (2 * 60 * 1000);

        // Reference five minutes from post timestamp
        long fiveMinutes = System.currentTimeMillis() - (5 * 60 * 1000);

        // If post is more than 5 minutes old, hide edit button
        if (post.getTimeStamp().getTime() < fiveMinutes) {
            editButton.setVisibility(View.GONE);
        }

        // Otherwise, show edit button
        else {
            editButton.setVisibility(View.VISIBLE);
        }

        // If post is more than two minutes old, hide delete button
        if (post.getTimeStamp().getTime() < twoMinutes) {
            deleteButton.setVisibility(View.GONE);
        }

        // Otherwise, show delete button
        else {
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
