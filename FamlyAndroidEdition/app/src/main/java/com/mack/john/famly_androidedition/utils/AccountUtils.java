package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AccountUtils {



    // Class properties
    private static final String TAG = "AccountUtils";

    private static final String ACCOUNT_DATA = "account.fam";
    private static final String PROFILE_DATA = "profile.fam";

    private static final String PHOTO_NAME_FAMILY = "family_photo.fam";

    private static final String REFERENCE_PHOTOS = "photos/";
    private static final String COLLECTION_ACCOUNTS = "accounts";



    // Custom methods
    public static void saveAccount(Context context, Account account, Bitmap photo) {
        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(account);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        if(photo != null) {
            try {
                FileOutputStream fileOutputStream = context.openFileOutput(PHOTO_NAME_FAMILY, Context.MODE_PRIVATE);

                photo.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);

                fileOutputStream.close();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addProfile(Context context, Profile profile, Bitmap photo) {
        Account account = null;

        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(accountFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            account = (Account) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        account.addProfile(profile);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(account);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(account);

        if(account.getProfiles().size() == 1) {
            File profileFile = new File(targetDir + PROFILE_DATA);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(profileFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                objectOutputStream.writeObject(profile);

                fileOutputStream.close();
                objectOutputStream.close();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Account loadAccount(Context context) {
        Account account = null;

        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(accountFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            account = (Account) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return account;
    }

    public static  boolean checkAuth() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser account = firebaseAuth.getCurrentUser();

        if (account != null) {
            return true;
        }

        else {
            return false;
        }
    }

    public static void createAccount(final Context context, final Account account, final Bitmap photo) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(account.getMasterEmail(), account.getMasterPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            final FirebaseUser firebaseAccount = firebaseAuth.getCurrentUser();

                            if(firebaseAccount != null) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(account)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "onSuccess: Account Created");

                                                if(photo != null) {
                                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                    StorageReference storageReference = firebaseStorage.getReference();
                                                    StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid());

                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                    photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: " + e );
                                            }
                                        });

                            }
                        }

                        else {
                            Toast.makeText(context, context.getResources().getString(R.string.signup_unsuccessful), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static Bitmap loadAccountPhoto(Context context) {
        Bitmap photo = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(PHOTO_NAME_FAMILY);

            photo = BitmapFactory.decodeStream(fileInputStream);

            fileInputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }
}
