package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.NewsfeedAdapter;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.post.Post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PostUtils {



    // Class properties
    private static final String TAG = "PostUtils";

    private static final String REFERENCE_PHOTOS = "photos/";

    private static final long ONE_MEGABYTE = 1024 * 1024;



    // Custom methods
    // Custom method to create post
    public static void createPost(final Context context, final Post post, final Profile poster, final Bitmap postPhoto) {
        // Load newsfeed and add post to list
        final ArrayList<Post> posts = loadNewsfeed(context);
        posts.add(0, post);

        // Reference firebase user account and databse
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Map post for upload
        final Map<String, Object> postMap = new HashMap<>();
        postMap.put("postMessage", post.getPostMessage());
        postMap.put("timeStamp", post.getTimeStamp());
        postMap.put("posterId", post.getPosterId());
        postMap.put("posterName", post.getPosterName());
        postMap.put("hasImage", post.getHasImage());

        // Reference database location for newsfeed and attempt to upload
        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed").document(post.getPostId())
                .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Reference databse location for profile timeline and attempt to upload
                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + poster.getProfileId()).collection("posts").document(post.getPostId()).set(postMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // If a photo is included...
                        if(postPhoto != null) {

                            // Reference remote storage location
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReference();
                            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + post.getPostId() + ".jpg");

                            // Convert image to byte array and compress
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            postPhoto.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                            byte[] data = byteArrayOutputStream.toByteArray();

                            // Attempt to upload
                            UploadTask uploadTask = photoReference.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ", e);
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.i(TAG, "onSuccess: Photo Uploaded");
                                }
                            });
                        }

                        // Let the user know the post has been created
                        Toast.makeText(context, context.getString(R.string.post_created), Toast.LENGTH_SHORT).show();

                        // Reference file location
                        File targetDir = context.getFilesDir();
                        File newsfeedFile = new File(targetDir + "newsfeed.fam");

                        // Attempt to save posts to file
                        try {

                            // Open output streams
                            FileOutputStream fileOutputStream = new FileOutputStream(newsfeedFile);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                            // Write posts
                            objectOutputStream.writeObject(posts);

                            // Close output streams
                            objectOutputStream.close();
                            fileOutputStream.close();
                        }

                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // Custom method to liste for updates to nenwsfeed
    public static void listenForNews(final Context context) {

        // Reference firebase user account and databse
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference date seven days in the past
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysPast = calendar.getTime();

        // Reference databse location and attempt to download posts from the past 7 days
        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .whereGreaterThan("timeStamp", sevenDaysPast).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // Create empty post list
                ArrayList<Post> posts = new ArrayList<>();

                // Loop through posts in database
                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {

                    // Reference post data
                    String postMessage = document.getString("postMessage");
                    String posterId = document.getString("posterId");
                    String posterName = document.getString("posterName");
                    Date timeStamp = document.getDate("timeStamp");
                    boolean hasImage = document.getBoolean("hasImage");

                    // Add post to list
                    Post post = new Post(postMessage, timeStamp, posterId, posterName, hasImage);
                    posts.add(post);
                }

                // Sort post list
                Collections.sort(posts);
                Collections.reverse(posts);

                // Reference file location
                File targetDir = context.getFilesDir();
                File accountFile = new File(targetDir + "newsfeed.fam");

                // Attempt to save posts to file
                try {

                    // Open output streams
                    FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                    // Write posts
                    objectOutputStream.writeObject(posts);

                    // Close output streams
                    objectOutputStream.close();
                    fileOutputStream.close();
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Custom method to listen for profile timeline updates
    public static void listenForTimeline(final Context context, Profile profile, final Account account, final ListView timeline) {

        // Reference firebase user account and databse
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference date seven days in the past
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysPast = calendar.getTime();

        // Reference database location and attempt to download posts from last 7 days
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("posts")
                .whereGreaterThan("timeStamp", sevenDaysPast).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Post> posts = new ArrayList<>();

                // Set timeline adapter
                NewsfeedAdapter newsfeedAdapter = new NewsfeedAdapter(context, posts, account);
                timeline.setAdapter(newsfeedAdapter);

                // If posts exist...
                if (queryDocumentSnapshots != null) {

                    // Loop through posts in database
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        // Referece post data
                        String postMessage = document.getString("postMessage");
                        String posterId = document.getString("posterId");
                        String posterName = document.getString("posterName");
                        Date timeStamp = document.getDate("timeStamp");
                        boolean hasImage = document.getBoolean("hasImage");

                        // Add post to list and update display
                        Post post = new Post(postMessage, timeStamp, posterId, posterName, hasImage);
                        posts.add(post);
                        newsfeedAdapter.notifyDataSetChanged();
                    }

                    // Sort post list
                    Collections.sort(posts);
                    Collections.reverse(posts);

                    // Reference file location
                    File targetDir = context.getFilesDir();
                    File accountFile = new File(targetDir + "timeline.fam");

                    // Attempt to save posts to file
                    try {

                        // Open output streams
                        FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                        // Write posts
                        objectOutputStream.writeObject(posts);

                        //Close streams
                        objectOutputStream.close();
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // Custom method to load newsfeed from file
    public static ArrayList<Post> loadNewsfeed(Context context) {

        // Reference file location
        File targetDir = context.getFilesDir();
        File newsfeedFile = new File(targetDir + "newsfeed.fam");

        // Create empty post list
        ArrayList<Post> posts = new ArrayList<>();

        try {

            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(newsfeedFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Load posts
            posts = (ArrayList<Post>) objectInputStream.readObject();

            // Close streams
            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Return post list
        return posts;
    }

    // Custom method to load post image
    public static void loadPostImage(View view, String postId) {

        // Reference image view
        final ImageView postImage = view.findViewById(R.id.display_post_image);

        // Reference firebase user account and remote storage location
        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + postId + ".jpg");

        // Attempt to download and display image
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                if(photo != null) {
                    postImage.setImageBitmap(photo);
                    postImage.setVisibility(View.VISIBLE);
                }

                else {
                    postImage.setVisibility(View.GONE);
                }
            }
        });
    }

    // Custom method to delete post
    public static void deletePost(final Context context, final String postId, final String posterId) {

        // Referece firebase user account and databse
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference remote storage location
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + postId + ".jpg");

        // Reference newsfeed database location and attempt to delete
        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .document(postId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Reference profile timeline database locationn and attempt to delete
                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + posterId).collection("posts").document(postId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // Let the user know the post has been deleted
                                Toast.makeText(context, context.getString(R.string.post_deleted), Toast.LENGTH_SHORT).show();

                                // Delete photo from remote storage
                                photoReference.delete();
                            }
                        });
            }
        });
    }

    // Custom method to update post
    public static void updatePost(final Context context, final Post post, ArrayList<Post> posts) {

        // Reference file location
        File targetDir = context.getFilesDir();
        File newsfeedFile = new File(targetDir + "newsfeed.fam");

        try {

            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(newsfeedFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write posts
            objectOutputStream.writeObject(posts);

            // Close streams
            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Reference firebase user account and databse
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Map post for upload
        final Map<String, Object> postMap = new HashMap<>();
        postMap.put("postMessage", post.getPostMessage());
        postMap.put("timeStamp", post.getTimeStamp());
        postMap.put("posterId", post.getPosterId());
        postMap.put("posterName", post.getPosterName());
        postMap.put("hasImage", post.getHasImage());

        // Reference database location for newsfeed and attempt to upload
        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .document(post.getPostId()).set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Reference database location for profile timeline annd attempt to upload
                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + post.getPosterId()).collection("posts").document(post.getPostId())
                        .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Let the user know the post has been updated
                        Toast.makeText(context, context.getString(R.string.post_updated), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
