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
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Post post = posts.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.cell_newsfeed, null);
        }

        TextView profileNameDisplay = convertView.findViewById(R.id.display_profile_name);
        String name = post.getPosterName() + " " + account.getFamilyName();
        profileNameDisplay.setText(name);

        TextView timestampDisplay = convertView.findViewById(R.id.display_timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy @ hh:mm a", Locale.getDefault());
        String timestamp = dateFormat.format(post.getTimeStamp());
        timestampDisplay.setText(timestamp);

        TextView postMessageDisplay = convertView.findViewById(R.id.display_post_message);
        postMessageDisplay.setText(post.getPostMessage());

        AccountUtils.loadProfilePhoto(context, convertView, post.getPosterId());

        if (post.getHasImage()) {
            ImageView postImage = convertView.findViewById(R.id.display_post_image);
            postImage.setVisibility(View.GONE);
        }

        else {
            ImageView postImage = convertView.findViewById(R.id.display_post_image);
            postImage.setVisibility(View.GONE);
        }

        PostUtils.loadPostImage(convertView, post.getPostId());

        ImageButton deleteButton = convertView.findViewById(R.id.button_delete_post);
        ImageButton editButton = convertView.findViewById(R.id.button_edit_post);

        toggleButtons(post, editButton, deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle(context.getString(R.string.delete_post));
                alertBuilder.setMessage(context.getString(R.string.delete_post_message));

                alertBuilder.setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        posts.remove(position);
                        PostUtils.deletePost(context, post.getPostId(), post.getPosterId());
                        notifyDataSetChanged();
                        PostUtils.listenForNews(context);
                    }
                });

                alertBuilder.setNegativeButton(context.getString(R.string.cancel), null);

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
        
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(context, EditPostActivity.class);
                editIntent.putExtra(EXTRA_POSTS, posts);
                editIntent.putExtra(EXTRA_POSITION, position);

                context.startActivity(editIntent);
            }
        });

        try {
            final ImageView postImageDisplay = convertView.findViewById(R.id.display_post_image);
            BitmapDrawable drawable = (BitmapDrawable) postImageDisplay.getDrawable();
            final Bitmap image = drawable.getBitmap();

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

            postImageDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(context, FullScreenPhotoActivity.class);
                    fullScreenIntent.putExtra(EXTRA_PHOTO, byteArrayOutputStream.toByteArray());

                    context.startActivity(fullScreenIntent);
                }
            });
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }



    // Custom methods
    private void toggleButtons(Post post, ImageButton editButton, ImageButton deleteButton) {
        long twoMinutes = System.currentTimeMillis() - (2 * 60 * 1000);
        long fiveMinutes = System.currentTimeMillis() - (5 * 60 * 1000);

        if (post.getTimeStamp().getTime() < fiveMinutes) {
            editButton.setVisibility(View.GONE);
        }

        else {
            editButton.setVisibility(View.VISIBLE);
        }

        if (post.getTimeStamp().getTime() < twoMinutes) {
            deleteButton.setVisibility(View.GONE);
        }

        else {
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
