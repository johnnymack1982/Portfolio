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
    public static void createPost(final Context context, final Post post, final Profile poster, final Bitmap postPhoto) {
        final ArrayList<Post> posts = loadNewsfeed(context);
        posts.add(0, post);

        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final Map<String, Object> postMap = new HashMap<>();
        postMap.put("postMessage", post.getPostMessage());
        postMap.put("timeStamp", post.getTimeStamp());
        postMap.put("posterId", post.getPosterId());
        postMap.put("posterName", post.getPosterName());
        postMap.put("hasImage", post.getHasImage());

        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed").document(post.getPostId())
                .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + poster.getProfileId()).collection("posts").document(post.getPostId()).set(postMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(postPhoto != null) {
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReference();
                            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + post.getPostId() + ".jpg");

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            postPhoto.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                            byte[] data = byteArrayOutputStream.toByteArray();

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

                        Toast.makeText(context, context.getString(R.string.post_created), Toast.LENGTH_SHORT).show();

                        File targetDir = context.getFilesDir();
                        File newsfeedFile = new File(targetDir + "newsfeed.fam");

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(newsfeedFile);
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                            objectOutputStream.writeObject(posts);

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

    public static void listenForNews(final Context context) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysPast = calendar.getTime();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .whereGreaterThan("timeStamp", sevenDaysPast).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                ArrayList<Post> posts = new ArrayList<>();

                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    String postMessage = document.getString("postMessage");
                    String posterId = document.getString("posterId");
                    String posterName = document.getString("posterName");
                    Date timeStamp = document.getDate("timeStamp");
                    boolean hasImage = document.getBoolean("hasImage");

                    Post post = new Post(postMessage, timeStamp, posterId, posterName, hasImage);
                    posts.add(post);
                }

                Collections.reverse(posts);

                File targetDir = context.getFilesDir();
                File accountFile = new File(targetDir + "newsfeed.fam");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                    objectOutputStream.writeObject(posts);

                    objectOutputStream.close();
                    fileOutputStream.close();
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void listenForTimeline(final Context context, Profile profile, final Account account, final ListView timeline) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysPast = calendar.getTime();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("posts")
                .whereGreaterThan("timeStamp", sevenDaysPast).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Post> posts = new ArrayList<>();

                NewsfeedAdapter newsfeedAdapter = new NewsfeedAdapter(context, posts, account);
                timeline.setAdapter(newsfeedAdapter);

                for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                    String postMessage = document.getString("postMessage");
                    String posterId = document.getString("posterId");
                    String posterName = document.getString("posterName");
                    Date timeStamp = document.getDate("timeStamp");
                    boolean hasImage = document.getBoolean("hasImage");

                    Post post = new Post(postMessage, timeStamp, posterId, posterName, hasImage);
                    posts.add(post);

                    newsfeedAdapter.notifyDataSetChanged();
                }

                Collections.reverse(posts);

                File targetDir = context.getFilesDir();
                File accountFile = new File(targetDir + "timeline.fam");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                    objectOutputStream.writeObject(posts);

                    objectOutputStream.close();
                    fileOutputStream.close();
                }

                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static ArrayList<Post> loadNewsfeed(Context context) {
        File targetDir = context.getFilesDir();
        File newsfeedFile = new File(targetDir + "newsfeed.fam");

        ArrayList<Post> posts = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(newsfeedFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            posts = (ArrayList<Post>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static ArrayList<Post> loadTimeline(Context context) {
        File targetDir = context.getFilesDir();
        File timelineFile = new File(targetDir + "timeline.fam");

        ArrayList<Post> posts = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(timelineFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            posts = (ArrayList<Post>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public static void loadPostImage(View view, String postId) {
        final ImageView postImage = view.findViewById(R.id.display_post_image);

        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + postId + ".jpg");

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
    
    public static void deletePost(final Context context, final String postId, final String posterId) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + postId + ".jpg");
        
        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .document(postId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + posterId).collection("posts").document(postId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, context.getString(R.string.post_deleted), Toast.LENGTH_SHORT).show();

                                photoReference.delete();
                            }
                        });
            }
        });
    }

    public static void updatePost(final Context context, final Post post, ArrayList<Post> posts) {
        File targetDir = context.getFilesDir();
        File newsfeedFile = new File(targetDir + "newsfeed.fam");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newsfeedFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(posts);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final Map<String, Object> postMap = new HashMap<>();
        postMap.put("postMessage", post.getPostMessage());
        postMap.put("timeStamp", post.getTimeStamp());
        postMap.put("posterId", post.getPosterId());
        postMap.put("posterName", post.getPosterName());
        postMap.put("hasImage", post.getHasImage());

        database.collection("accounts").document(firebaseAccount.getUid()).collection("newsfeed")
                .document(post.getPostId()).set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                        .document(firebaseAccount.getUid() + post.getPosterId()).collection("posts").document(post.getPostId())
                        .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.post_updated), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
