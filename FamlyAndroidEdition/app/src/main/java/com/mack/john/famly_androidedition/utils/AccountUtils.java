package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");

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

        Map<String, Object> profileInfo = new HashMap<>();
        profileInfo.put("firstName", profile.getFirstName());
        profileInfo.put("dateOfBirth", profile.getDateOfBirth());
        profileInfo.put("genderId", profile.getGenderID());
        profileInfo.put("profilePin", profile.getProfilePIN());

        if(profile instanceof Parent) {
            profileInfo.put("roleId", ((Parent) profile).getRoleID());
        }


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).set(profileInfo);

        if(account.getProfiles().size() == 1) {
            saveProfile(context, profile);
        }
    }

    public static void saveProfile(Context context, Profile profile) {
        File targetDir = context.getFilesDir();
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

    public static  void checkAuth(Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser account = firebaseAuth.getCurrentUser();

        if (account != null) {
            Intent loginIntent = new Intent(context, NavigationActivity.class);
            context.startActivity(loginIntent);
        }

        else {

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

                                Map<String, Object> accountInfo = new HashMap<>();
                                accountInfo.put("familyName", account.getFamilyName());
                                accountInfo.put("masterEmail", account.getMasterEmail());
                                accountInfo.put("masterPassword", account.getMasterPassword());
                                accountInfo.put("postalCode", account.getPostalCode());
                                accountInfo.put("streetAddress", account.getStreetAddress());

                                database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(accountInfo)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "onSuccess: Account Created");

                                                if(photo != null) {
                                                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                                    StorageReference storageReference = firebaseStorage.getReference();
                                                    StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

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

    public static void loadAccountPhoto(final Context context, View view) {
        final ImageView familyPhoto = view.findViewById(R.id.profile_photo);

        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                familyPhoto.setImageBitmap(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    FileInputStream fileInputStream = context.openFileInput(PHOTO_NAME_FAMILY);

                    Bitmap photo = BitmapFactory.decodeStream(fileInputStream);

                    familyPhoto.setImageBitmap(photo);

                    fileInputStream.close();
                }

                catch (IOException ex) {
                    ex.printStackTrace();

                    familyPhoto.setImageDrawable(context.getDrawable(R.drawable.family_icon_large));
                }
            }
        });
    }

    public static void login(final Context context, String email, String password) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    File targetDir = context.getFilesDir();
                    final File accountFile = new File(targetDir + ACCOUNT_DATA);

                    try {
                        FileInputStream fileInputStream = new FileInputStream(accountFile);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                        mAccount = (Account) objectInputStream.readObject();

                        objectInputStream.close();
                        fileInputStream.close();
                    }

                    catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    final FirebaseFirestore database = FirebaseFirestore.getInstance();

                    DocumentReference documentReference = database.collection(COLLECTION_ACCOUNTS).document(firebaseAuth.getUid());
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();

                                if(document.exists()) {
                                    mAccount = new Account("", "", 0, "", "");

                                    mAccount.setFamilyName(document.getString("familyName"));
                                    mAccount.setMasterEmail(document.getString("masterEmail"));
                                    mAccount.setMasterPassword(document.getString("masterPassword"));
                                    mAccount.setPostalCode(document.getLong("postalCode").intValue());
                                    mAccount.setStreetAddress(document.getString("streetAddress"));

                                    database.collection(COLLECTION_ACCOUNTS).document(firebaseAuth.getUid()).collection("profiles").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                            if(documentSnapshot.getLong("roleId") != null) {
                                                                Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                                                                String firstName = documentSnapshot.getString("firstName");
                                                                int genderId = documentSnapshot.getLong("genderId").intValue();
                                                                int profilePin = documentSnapshot.getLong("profilePin").intValue();
                                                                int roleId = documentSnapshot.getLong("roleId").intValue();

                                                                mAccount.addProfile(new Parent(firstName, dateOfBirth, genderId, profilePin, roleId));
                                                            }

                                                            else {
                                                                Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                                                                String firstName = documentSnapshot.getString("firstName");
                                                                int genderId = documentSnapshot.getLong("genderId").intValue();
                                                                int profilePin = documentSnapshot.getLong("profilePin").intValue();

                                                                mAccount.addProfile(new Child(firstName, dateOfBirth, genderId, profilePin));
                                                            }
                                                        }

                                                        try {
                                                            FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                                                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                                                            objectOutputStream.writeObject(mAccount);

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

                    Intent profileLoginIntent = new Intent(context, ProfileLoginActivity.class);
                    context.startActivity(profileLoginIntent);
                }

                else {
                    Toast.makeText(context, context.getString(R.string.login_unsuccessful), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void loginProfile(final Context context, final String firstName, final String pin) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = database.collection(COLLECTION_ACCOUNTS).document(account.getUid()).collection("profiles")
                .document(account.getUid() + firstName + pin);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.getLong("roleId") != null) {
                        Date dateofBirth = documentSnapshot.getDate("dateOfBirth");
                        int genderId = documentSnapshot.getLong("genderId").intValue();
                        int profilePin = Integer.valueOf(pin);
                        int roleId = documentSnapshot.getLong("roleId").intValue();

                        mProfile = new Parent(firstName, dateofBirth, genderId, profilePin, roleId);
                    }

                    else {
                        Date dateofBirth = documentSnapshot.getDate("dateOfBirth");
                        int genderId = documentSnapshot.getLong("genderId").intValue();
                        int profilePin = Integer.valueOf(pin);

                        mProfile = new Child(firstName, dateofBirth, genderId, profilePin);
                    }

                    if(mProfile != null) {
                        saveProfile(context, mProfile);

                        Intent navigationIntent = new Intent(context, NavigationActivity.class);
                        context.startActivity(navigationIntent);
                    }
                }
            }
        });
    }

    public static void resetPassword(final Context context, String email) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(context, context.getString(R.string.sending_reset_email), Toast.LENGTH_SHORT).show();

                            Intent loginIntent = new Intent(context, MasterLoginActivity.class);
                            context.startActivity(loginIntent);
                        }
                        
                        else {
                            Toast.makeText(context, context.getString(R.string.no_account), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void updateAccount(final Context context, String familyName, String streetAddress, int postalCode, final String newEmail, final String oldEmail,
                                     final String password, final Bitmap photo) {

        final Account account = new Account(familyName, streetAddress, postalCode, newEmail, password);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseAccount = firebaseAuth.getCurrentUser();

        if(firebaseAccount != null) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();

            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("familyName", account.getFamilyName());
            accountInfo.put("masterEmail", account.getMasterEmail());
            accountInfo.put("masterPassword", account.getMasterPassword());
            accountInfo.put("postalCode", account.getPostalCode());
            accountInfo.put("streetAddress", account.getStreetAddress());

            database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).set(accountInfo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "onSuccess: Account Created");

                            if(photo != null) {
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageReference = firebaseStorage.getReference();
                                StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

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

                            AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), password);

                            firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseAccount.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
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

    public static void updatePassword(final Context context, String oldPassword, final String newPassword, final Account account, final Bitmap photo) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), oldPassword);

        firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    firebaseAccount.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(context, context.getString(R.string.password_updated), Toast.LENGTH_SHORT).show();

                                account.setMasterPassword(newPassword);

                                saveAccount(context, account, photo);

                                Intent finishIntent = new Intent(context, FamilyProfileActivity.class);
                                context.startActivity(finishIntent);
                            }
                        }
                    });
                }
            }
        });
    }

    public static void uploadFamilyPhoto(Bitmap photo) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();

        if(photo != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + ".jpg");

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

    public static void uploadProfilePhoto(Profile profile, Bitmap photo) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();

        if(photo != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");

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

    public static void loadProfilePhoto(final Context context, View view, String profileId) {
        final ImageView profilePhoto = view.findViewById(R.id.profile_photo);

        FirebaseAuth firebaseAccount = FirebaseAuth.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profileId + ".jpg");

        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                profilePhoto.setImageBitmap(photo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    FileInputStream fileInputStream = context.openFileInput(PHOTO_NAME_FAMILY);

                    Bitmap photo = BitmapFactory.decodeStream(fileInputStream);

                    profilePhoto.setImageBitmap(photo);

                    fileInputStream.close();
                }

                catch (IOException ex) {
                    ex.printStackTrace();

                    profilePhoto.setImageDrawable(context.getDrawable(R.drawable.male_icon_large));
                }
            }
        });
    }

    public static Profile loadProfile(Context context) {
        Profile profile = new Profile("", new Date(), 0, 0);

        File targetDir = context.getFilesDir();
        File profileFile = new File(targetDir + PROFILE_DATA);

        try {
            FileInputStream fileInputStream = new FileInputStream(profileFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            profile = (Profile) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return profile;
    }

    public static void deleteProfile(final Context context, boolean deletingSelf, final Profile profile, Account account) {
        ArrayList<Profile> profiles = account.getProfiles();

        for(int i = 0; i < profiles.size(); i++) {
            if(profile.getFirstName().equals(profiles.get(i).getFirstName())) {
                profiles.remove(i);
            }
        }

        account.setProfiles(profiles);

        saveAccount(context, account, null);

        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, profile.getFirstName() + " has been removed from the family.", Toast.LENGTH_SHORT).show();

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                StorageReference photoReference = storageReference.child(REFERENCE_PHOTOS + firebaseAccount.getUid() + profile.getProfileId() + ".jpg");
                photoReference.delete();
            }
        });

        if(deletingSelf) {
            FirebaseAuth.getInstance().signOut();

            Intent signoutIntent = new Intent(context, MasterLoginActivity.class);
            context.startActivity(signoutIntent);
        }

        else {
            Intent continueIntent = new Intent(context, FamilyProfileActivity.class);
            context.startActivity(continueIntent);
        }
    }

    public static void deleteAccount(final Context context) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        String password = document.getString("masterPassword");

                        AuthCredential credential = EmailAuthProvider.getCredential(firebaseAccount.getEmail(), password);

                        firebaseAccount.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    firebaseAccount.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
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

    public static void listenForUpdates(final Context context) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    String familyName = documentSnapshot.getString("familyName");
                    String masterEmail = documentSnapshot.getString("masterEmail");
                    String masterPassword = documentSnapshot.getString("masterPassword");
                    int postalCode = documentSnapshot.getLong("postalCode").intValue();
                    String streetAddress = documentSnapshot.getString("streetAddress");

                    Account account = new Account(familyName, streetAddress, postalCode, masterEmail, masterPassword);

                    saveAccount(context, account, null);
                }

                else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        database.collection(COLLECTION_ACCOUNTS).document(firebaseAccount.getUid()).collection("profiles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                Account account = loadAccount(context);
                account.getProfiles().clear();

                for (QueryDocumentSnapshot documentSnapshot : value) {
                    if (documentSnapshot.get("firstName") != null) {
                        if (documentSnapshot.getLong("roleId") != null) {
                            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                            String firstName = documentSnapshot.getString("firstName");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = documentSnapshot.getLong("profilePin").intValue();
                            int roleId = documentSnapshot.getLong("roleId").intValue();

                            account.addProfile(new Parent(firstName, dateOfBirth, genderId, profilePin, roleId));
                        } else {
                            Date dateOfBirth = documentSnapshot.getDate("dateOfBirth");
                            String firstName = documentSnapshot.getString("firstName");
                            int genderId = documentSnapshot.getLong("genderId").intValue();
                            int profilePin = documentSnapshot.getLong("profilePin").intValue();

                            account.addProfile(new Child(firstName, dateOfBirth, genderId, profilePin));
                        }
                    }

                    File targetDir = context.getFilesDir();
                    File accountFile = new File(targetDir + ACCOUNT_DATA);

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(accountFile);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

                        objectOutputStream.writeObject(account);

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
}
