package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mack.john.famly_androidedition.NavigationActivity;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.family_profile.FamilyProfileActivity;
import com.mack.john.famly_androidedition.login.MasterLoginActivity;
import com.mack.john.famly_androidedition.login.ProfileLoginActivity;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Child;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AccountUtils {



    // Class properties
    private static final String TAG = "AccountUtils";

    private static final String ACCOUNT_DATA = "account.fam";
    private static final String PROFILE_DATA = "profile.fam";

    private static final String PHOTO_NAME_FAMILY = "family_photo.fam";

    private static final String REFERENCE_PHOTOS = "photos/";
    private static final String COLLECTION_ACCOUNTS = "accounts";

    private static final long ONE_MEGABYTE = 1024 * 1024;

    private static Profile mProfile;
    private static Account mAccount;



    // Custom methods
    // Custom method to save account to file
    public static void saveAccount(Context context, Account account, Bitmap photo) {
        // Reference location for account file
        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        // Save account file
        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write object
            objectOutputStream.writeObject(account);

            // Close streams
            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Save photo file
        if(photo != null) {
            try {
                // Open output stream
                FileOutputStream fileOutputStream = context.openFileOutput(PHOTO_NAME_FAMILY, Context.MODE_PRIVATE);

                // Compress and save file
                photo.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);

                // Close stream
                fileOutputStream.close();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Custom method to add profile to account
    public static void addProfile(Context context, Profile profile, Bitmap photo) {
        Account account = null;

        // Reference account file location
        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        // Load account
        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(accountFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Load account
            account = (Account) objectInputStream.readObject();

            // Close streams
            fileInputStream.close();
            objectInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Add profile to account
        account.addProfile(profile);

        // Save account to file
        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write file
            objectOutputStream.writeObject(account);

            // Close streams
            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        // Reference Firebase account
        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        // If a photo exists...
        if(photo != null) {
            // Reference remote storage location
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");

            // Convert image to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            // Attempt to upload photo
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

        // Map profile data for upload
        Map<String, Object> profileInfo = new HashMap<>();
        profileInfo.put("firstName", profile.getFirstName());
        profileInfo.put("dateOfBirth", profile.getDateOfBirth());
        profileInfo.put("genderId", profile.getGenderID());
        profileInfo.put("profilePin", profile.getProfilePIN());

        if(profile instanceof Parent) {
            profileInfo.put("roleId", ((Parent) profile).getRoleID());
        }


        // Reference upload location and attempt to upload
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).set(profileInfo);

        // If this is the first parent profile added to the account, save it to file
        if(account.getProfiles().size() == 1) {
            saveProfile(context, profile);
        }
    }

    // Custom method to save profile to file
    public static void saveProfile(Context context, Profile profile) {
        // Reference file location
        File targetDir = context.getFilesDir();
        File profileFile = new File(targetDir + PROFILE_DATA);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(profileFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write profile
            objectOutputStream.writeObject(profile);

            // Close streams
            fileOutputStream.close();
            objectOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom method to load account from file
    public static Account loadAccount(Context context) {
        Account account = null;

        // Reference file location
        File targetDir = context.getFilesDir();
        File accountFile = new File(targetDir + ACCOUNT_DATA);

        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(accountFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Read account
            account = (Account) objectInputStream.readObject();

            // Close streams
            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Return loaded account
        return account;
    }

    // Custom method to automatically log in
    public static  void checkAuth(Context context) {
        // Reference last logged in user
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();

        // If a logged in user exists, launch navigation activity
        if (account != null) {
            Intent loginIntent = new Intent(context, NavigationActivity.class);
            context.startActivity(loginIntent);
        }

        // Otherwise, launch login activity
        else {
            Intent intent = new Intent(context, MasterLoginActivity.class);
            context.startActivity(intent);
        }
    }

    // Custom method to create new account
    public static void createAccount(final Context context, final Account account, final Bitmap photo) {
        // Attempt to create new firebase user
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(account.getMasterEmail(), account.getMasterPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If successful...
                        if(task.isSuccessful()) {
                            // Reference current user
                            final FirebaseUser firebaseAccount = firebaseAuth.getCurrentUser();

                            // If user exists...
                            if(firebaseAccount != null) {
                                FirebaseFirestore database = FirebaseFirestore.getInstance();

                                // Map account for upload
                                Map<String, Object> accountInfo = new HashMap<>();
                                accountInfo.put("familyName", account.getFamilyName());
                                accountInfo.put("masterEmail", account.getMasterEmail());
                                accountInfo.put("masterPassword", account.getMasterPassword());
                                accountInfo.put("postalCode", account.getPostalCode());
                                accountInfo.put("streetAddress", account.getStreetAddress());

                                // Reference location and attempt to upload
                                database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(accountInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "onSuccess: Account Created");

                                                // If a photo exists...
                                                if(photo != null) {
                                                    // Reference remote storage location
                                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                    StorageReference storageReference = firebaseStorage.getReference();
                                                    StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

                                                    // Convert image to byte array and compress
                                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                    photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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

                        // If there was a problem, let the user know
                        else {
                            Toast.makeText(context, context.getResources().getString(R.string.signup_unsuccessful), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Custom method to load family photo
    public static void loadAccountPhoto(final Context context, View view) {
        // Reference image view
        final ImageView familyPhoto = view.findViewById(R.id.profile_photo);

        // Reference user account
        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        // Reference storage locationn
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

        // Attempt to download photo and display
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                familyPhoto.setImageBitmap(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // If download failed, attempt to load from cache
                try {
                    // Open input stream
                    FileInputStream fileInputStream = context.openFileInput(PHOTO_NAME_FAMILY);

                    // Convert stream to bitmap and display
                    Bitmap photo = BitmapFactory.decodeStream(fileInputStream);
                    familyPhoto.setImageBitmap(photo);

                    // Close stream
                    fileInputStream.close();
                }

                // If unsuccessful, display default image
                catch (IOException ex) {
                    ex.printStackTrace();

                    familyPhoto.setImageDrawable(context.getDrawable(R.drawable.family_icon_large));
                }
            }
        });
    }

    // Custom method to log into account
    public static void login(final Context context, String email, String password, View view) {
        // Reference UI elemets
        final Button forgotPasswordButton = view.findViewById(R.id.button_forgot_password);
        final Button loginButton = view.findViewById(R.id.button_login);
        final ProgressBar loadingProgress = view.findViewById(R.id.progress_loading);

        // Set default UI element states
        forgotPasswordButton.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);

        // Reference firebase account
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Attempt to log in
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // If successful...
                if(task.isSuccessful()) {
                    // Reference file location
                    File targetDir = context.getFilesDir();
                    final File accountFile = new File(targetDir + ACCOUNT_DATA);

                    // Attempt to load account from file
                    try {
                        // Open input streams
                        FileInputStream fileInputStream = new FileInputStream(accountFile);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                        // Load account
                        mAccount = (Account) objectInputStream.readObject();

                        // Close streams
                        objectInputStream.close();
                        fileInputStream.close();
                    }

                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Reference database location and attempt to download data
                    final FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = database.collection(COLLECTION_ACCOUNTS).document(firebaseAuth.getUid());
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            // If successful...
                            if(task.isSuccessful()) {
                                // Reference account data
                                DocumentSnapshot document = task.getResult();

                                // if data exists...
                                if(document.exists()) {
                                    // Create dummy account object
                                    mAccount = new Account("", "", 0, "", "");

                                    // Populate account object with downloaded data
                                    mAccount.setFamilyName(document.getString("familyName"));
                                    mAccount.setMasterEmail(document.getString("masterEmail"));
                                    mAccount.setMasterPassword(document.getString("masterPassword"));
                                    mAccount.setPostalCode(document.getLong("postalCode").intValue());
                                    mAccount.setStreetAddress(document.getString("streetAddress"));

                                    // Reference profiles from database
                                    database.collection(COLLECTION_ACCOUNTS).document(firebaseAuth.getUid()).collection("profiles").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    // If successful...
                                                    if(task.isSuccessful()) {

                                                        // Loop through documents
                                                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                                            // If profile is a parent...
                                                            if(documentSnapshot.getLong("roleId") != null) {

                                                                // Reference profile data
                                                                Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                                                                String firstName = documentSnapshot.getString("firstName");
                                                                int genderId = documentSnapshot.getLong("genderId").intValue();
                                                                int profilePin = documentSnapshot.getLong("profilePin").intValue();
                                                                int roleId = documentSnapshot.getLong("roleId").intValue();

                                                                // Add profile to account
                                                                mAccount.addProfile(new Parent(firstName, dateOfBirth, genderId, profilePin, roleId));
                                                            }

                                                            // If profile is a child...
                                                            else {

                                                                // Referece profile data
                                                                Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                                                                String firstName = documentSnapshot.getString("firstName");
                                                                int genderId = documentSnapshot.getLong("genderId").intValue();
                                                                int profilePin = documentSnapshot.getLong("profilePin").intValue();

                                                                // Add profile to account
                                                                mAccount.addProfile(new Child(firstName, dateOfBirth, genderId, profilePin));
                                                            }
                                                        }

                                                        // Attempt to save account to file
                                                        try {
                                                            // Open input streams
                                                            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                                                            // Write account to file
                                                            objectOutputStream.writeObject(mAccount);

                                                            // Close streams
                                                            objectOutputStream.close();
                                                            fileOutputStream.close();
                                                        }

                                                        catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });

                    // Launch profile login activity
                    Intent profileLoginIntent = new Intent(context, ProfileLoginActivity.class);
                    context.startActivity(profileLoginIntent);
                }

                // If there was a problem, let the user know and reset UI
                else {
                    Toast.makeText(context, context.getString(R.string.login_unsuccessful), Toast.LENGTH_SHORT).show();

                    forgotPasswordButton.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    // Custom method to log into profile
    public static void loginProfile(final Context context, final String firstName, final String pin, View view) {
        // Reference UI elements
        final Button cancelButton = view.findViewById(R.id.button_cancel);
        final Button continueButton = view.findViewById(R.id.button_continue);
        final ProgressBar loadingProgress = view.findViewById(R.id.progress_loading);

        // Set default UI states
        cancelButton.setVisibility(View.GONE);
        continueButton.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);

        // Reference user account and database
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference database location
        final DocumentReference documentReference = database.collection(COLLECTION_ACCOUNTS).document(account.getUid()).collection("profiles")
                .document(account.getUid() + firstName + pin);

        // Attempt to download
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                // If successful...
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    // If document exists...
                    if (documentSnapshot.exists()) {

                        // If profile is a parent...
                        if (documentSnapshot.getLong("roleId") != null) {

                            // Reference profile data...
                            Date dateofBirth = documentSnapshot.getDate("dateOfBirth");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = Integer.valueOf(pin);
                            int roleId = documentSnapshot.getLong("roleId").intValue();


                            // Create new profile object
                            mProfile = new Parent(firstName, dateofBirth, genderId, profilePin, roleId);
                        }

                        // If profile is a child...
                        else {

                            // Reference profile data
                            Date dateofBirth = documentSnapshot.getDate("dateOfBirth");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = Integer.valueOf(pin);

                            // Create new profile object
                            mProfile = new Child(firstName, dateofBirth, genderId, profilePin);
                        }

                        // If a profile exists, save profile to file and launch navigation activity
                        if (mProfile != null) {
                            saveProfile(context, mProfile);

                            Intent navigationIntent = new Intent(context, NavigationActivity.class);
                            context.startActivity(navigationIntent);
                        }

                        // if there was a problem, let the user know and reset UI
                        else {
                            Toast.makeText(context, context.getString(R.string.login_unsuccessful), Toast.LENGTH_SHORT).show();

                            cancelButton.setVisibility(View.VISIBLE);
                            continueButton.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.GONE);
                        }
                    }

                    // if there was a problem, let the user know and reset UI
                    else {
                        Toast.makeText(context, context.getString(R.string.login_unsuccessful), Toast.LENGTH_SHORT).show();

                        cancelButton.setVisibility(View.VISIBLE);
                        continueButton.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    // Custom method to send password reset email
    public static void resetPassword(final Context context, String email) {
        // Referece firebase auth
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Attempt to send password reset email
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // If successful, let the user know and return to login activity
                        if(task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.sending_reset_email), Toast.LENGTH_SHORT).show();

                            Intent loginIntent = new Intent(context, MasterLoginActivity.class);
                            context.startActivity(loginIntent);
                        }

                        // If there was a problem, let the user know
                        else {
                            Toast.makeText(context, context.getString(R.string.no_account), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Custom method to update account
    public static void updateAccount(final Context context, String familyName, String streetAddress, int postalCode, final String newEmail, final String oldEmail,
                                     final String password, final Bitmap photo) {

        // Create new account object from user input
        final Account account = new Account(familyName, streetAddress, postalCode, newEmail, password);

        // Reference firebase user account
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseAccount = firebaseAuth.getCurrentUser();

        // If an account exists...
        if(firebaseAccount != null) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            // Map account for upload
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("familyName", account.getFamilyName());
            accountInfo.put("masterEmail", account.getMasterEmail());
            accountInfo.put("masterPassword", account.getMasterPassword());
            accountInfo.put("postalCode", account.getPostalCode());
            accountInfo.put("streetAddress", account.getStreetAddress());

            // Reference location and attempt to upload
            database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(accountInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "onSuccess: Account Created");

                            // If a photo exists...
                            if(photo != null) {

                                // Reference remote storage location
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = firebaseStorage.getReference();
                                StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

                                // Convert image to byte array annd compress
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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

                            // Reference file location
                            File targetDir = context.getFilesDir();
                            File accountFile = new File(targetDir + ACCOUNT_DATA);

                            // Attempt to save account to file
                            try {
                                // Open output streams
                                FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                                // Write account
                                objectOutputStream.writeObject(account);

                                // Close streams
                                objectOutputStream.close();
                                fileOutputStream.close();
                            }

                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            // If a photo exists, attempt to save to file
                            if(photo != null) {
                                try {
                                    // Open ouput stream
                                    FileOutputStream fileOutputStream = context.openFileOutput(PHOTO_NAME_FAMILY, Context.MODE_PRIVATE);

                                    // Compress and write
                                    photo.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);

                                    // Close stream
                                    fileOutputStream.close();
                                }

                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Reauthenticate user account
                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), password);
                            firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseAccount.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            // If successful launch family profile activity
                                            if(task.isSuccessful()) {
                                                Intent finishIntent = new Intent(context, FamilyProfileActivity.class);
                                                context.startActivity(finishIntent);
                                            }
                                        }
                                    });
                                }
                            });
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

    // Custom method to update account password
    public static void updatePassword(final Context context, String oldPassword, final String newPassword, final Account account, final Bitmap photo) {
        // Reference firebase user account and reauthenticate user
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), oldPassword);
        firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // If successful...
                if(task.isSuccessful()) {

                    // Attempt to update password
                    firebaseAccount.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // If successful...
                            if(task.isSuccessful()) {

                                // Let the user know
                                Toast.makeText(context, context.getString(R.string.password_updated), Toast.LENGTH_SHORT).show();

                                // Save changes to file
                                account.setMasterPassword(newPassword);
                                saveAccount(context, account, photo);

                                // Launch family profile activity
                                Intent finishIntent = new Intent(context, FamilyProfileActivity.class);
                                context.startActivity(finishIntent);
                            }
                        }
                    });
                }
            }
        });
    }

    // Custom method to upload family photo
    public static void uploadFamilyPhoto(Bitmap photo) {

        // Reference firebase user account
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();

        // If a photo exists...
        if(photo != null) {

            // Reference storage location
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

            // Convert image to byte array and compress
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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
    }

    // Custom method to upload profile photo
    public static void uploadProfilePhoto(Profile profile, Bitmap photo) {

        // Reference firebase user account
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();

        // If a photo exists...
        if(photo != null) {

            // Reference remote storage location
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");

            // Convert image to byte array and compress
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
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
    }

    // Custom method to load profile photo
    public static void loadProfilePhoto(final Context context, View view, String profileId) {
        // Reference image view
        final ImageView profilePhoto = view.findViewById(R.id.profile_photo);

        // Reference firebase user account
        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        // Reference remote storage location
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profileId + ".jpg");

        // Attempt to download and display image
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                profilePhoto.setImageBitmap(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // If unsucessful, attempt to load image from cache
                try {

                    // Open input stream
                    FileInputStream fileInputStream = context.openFileInput(PHOTO_NAME_FAMILY);

                    // Load image and display
                    Bitmap photo = BitmapFactory.decodeStream(fileInputStream);
                    profilePhoto.setImageBitmap(photo);

                    // Close stream
                    fileInputStream.close();
                }

                // If unsuccessful, display default image
                catch (IOException ex) {
                    ex.printStackTrace();

                    profilePhoto.setImageDrawable(context.getDrawable(R.drawable.male_icon_large));
                }
            }
        });
    }

    // Custom method to load profile
    public static Profile loadProfile(Context context) {
        // Create blank profile
        Profile profile = new Profile("", new Date(), 0, 0);

        // Reference file location
        File targetDir = context.getFilesDir();
        File profileFile = new File(targetDir + PROFILE_DATA);

        // Attempt to load profile from file
        try {
            // Open input streams
            FileInputStream fileInputStream = new FileInputStream(profileFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Load profile
            profile = (Profile) objectInputStream.readObject();

            // Close streams
            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Return loaded profile
        return profile;
    }

    // Custom method to delete profile
    public static void deleteProfile(final Context context, boolean deletingSelf, final Profile profile, Account account) {
        // Load account profile list
        ArrayList<Profile> profiles = account.getProfiles();

        // Remove selected profile from list
        for(int i = 0; i < profiles.size(); i++) {
            if(profile.getFirstName().equals(profiles.get(i).getFirstName())) {
                profiles.remove(i);
            }
        }

        // Save changes to file
        account.setProfiles(profiles);
        saveAccount(context, account, null);

        // Reference firebase user account and database
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference database location and attempt to upload
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // Let the user know the profile has been removed
                Toast.makeText(context, profile.getFirstName() + " has been removed from the family.", Toast.LENGTH_SHORT).show();

                // Reference photo location in remote storage and delete
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");
                photoReference.delete();
            }
        });

        // If user deleted their own profile, sign out ad return to login activity
        if(deletingSelf) {
            FirebaseAuth.getInstance().signOut();

            Intent signoutIntent = new Intent(context, MasterLoginActivity.class);
            context.startActivity(signoutIntent);
        }

        // Otherwise, return to family profile activity
        else {
            Intent continueIntent = new Intent(context, FamilyProfileActivity.class);
            context.startActivity(continueIntent);
        }
    }

    // Custom method to delete account
    public static void deleteAccount(final Context context) {

        // Reference firebase user account and databse
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference databse location and attempt to delete
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    // If required document exists...
                    if(document.exists()) {

                        // Reauthenticate user
                        String password = document.getString("masterPassword");
                        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), password);
                        firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // If successful...
                                if(task.isSuccessful()) {

                                    // Attempt to delete account
                                    firebaseAccount.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            // If successful, let the user know
                                            if(task.isSuccessful()) {
                                                Toast.makeText(context, context.getString(R.string.account_deleted), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // Custom method to listen for updates to family account and profiles
    public static void listenForUpdates(final Context context) {

        // Reference firebase user account and database
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference account database location and set change listener
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // If account document exists...
                if (documentSnapshot != null && documentSnapshot.exists()) {

                    // Reference account data
                    String familyName = documentSnapshot.getString("familyName");
                    String masterEmail = documentSnapshot.getString("masterEmail");
                    String masterPassword = documentSnapshot.getString("masterPassword");
                    int postalCode = documentSnapshot.getLong("postalCode").intValue();
                    String streetAddress = documentSnapshot.getString("streetAddress");

                    // Save changes to file
                    Account account = new Account(familyName, streetAddress, postalCode, masterEmail, masterPassword);
                    saveAccount(context, account, null);
                }

                else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        // Reference profile database location and set change listener
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // Load account and reset profile list
                Account account = loadAccount(context);
                account.getProfiles().clear();

                // Loop through profiles in database
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.get("firstName") != null) {

                        // If profile is a parent...
                        if (documentSnapshot.getLong("roleId") != null) {

                            // Reference profile data
                            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                            String firstName = documentSnapshot.getString("firstName");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = documentSnapshot.getLong("profilePin").intValue();
                            int roleId = documentSnapshot.getLong("roleId").intValue();

                            // Add profile to account
                            account.addProfile(new Parent(firstName, dateOfBirth, genderId, profilePin, roleId));
                        }

                        // If profile is a child...
                        else {

                            // Reference profile data
                            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                            String firstName = documentSnapshot.getString("firstName");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = documentSnapshot.getLong("profilePin").intValue();

                            // Add profile to account
                            account.addProfile(new Child(firstName, dateOfBirth, genderId, profilePin));
                        }
                    }

                    // Reference account file location
                    File targetDir = context.getFilesDir();
                    File accountFile = new File(targetDir + ACCOUNT_DATA);

                    // Attempt to save account to file
                    try {

                        // Open output streams
                        FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                        // Write account
                        objectOutputStream.writeObject(account);

                        // Close streams
                        objectOutputStream.close();
                        fileOutputStream.close();
                    }

                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // Custom method to sign out
    public static void signout(Context context) {
        // Refrence firebase user account
        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        // Sign out
        firebaseAccount.signOut();

        // Launch login activity
        Intent signoutIntent = new Intent(context, MasterLoginActivity.class);
        context.startActivity(signoutIntent);
    }
}
