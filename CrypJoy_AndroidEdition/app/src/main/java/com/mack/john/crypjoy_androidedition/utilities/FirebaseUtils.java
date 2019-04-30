package com.mack.john.crypjoy_androidedition.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoginActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.objects.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FirebaseUtils {


    private static final String COLLECTION_USERS = "users";

    private static final String COLLECTION_PROGRESS = "collection_progress";
    private static final String DOC_DAILY_PROGRESS = "daily_progress";
    private static final String DOC_LIFETIME_PROGRESS = "lifetime_progress";

    private static final String COLLECTION_WEEKLY_GIVEN = "weekly_given";
    private static final String COLLECTION_WEEKLY_RECEIVED = "weekly_received";

    private static final String FIELD_DATE = "dateCreated";

    public static final String FILE_USER = "user.joy";


    // Custom methods
    //** USER AUTHENTICATION METHODS**
    // Custom method to check for currently signed in user
    public static void checkForAuth(final Context context) {
        // Define Firebase user and database
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // If a user exists...
        if(firebaseUser != null) {
            // Attempt to retrieve detail record for user
            DocumentReference documentReference = database.collection(COLLECTION_USERS).document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        // If detail record for user exists...
                        if(Objects.requireNonNull(document).exists()) {
                            // Convert detail document to User object and save to local storage
                            User user = document.toObject(User.class);
                            cacheUser(user, context);

                            // Call custom method to load daily progress from cloud storage
                            loadDailyProgress(context);
                        }

                        // If there is no such document, the user does not exist
                        // Launch the Login activity
                        else {
                            launchLogin(context);
                        }
                    }

                    // User does not exist
                    // Launch Login activity
                    else {
                        launchLogin(context);
                    }
                }
            });
        }

        // User does not exist
        // Launch Login activity
        else {
            launchLogin(context);
        }
    }

    // Custom method to launch Login activity
    private static void launchLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }

    // Custom method to log user in with entered email and password
    public static void login(String eMail, String password, final Context context, final View view) {
        // Get current user
        FirebaseAuth user = FirebaseAuth.getInstance();

        // Sign in with entered e-mail and password
        user.signInWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // Verify that user has successfully logged in
                    checkForAuth(context);
                }

                // If login is unsuccessful...
                else {
                    // Show Login button
                    Button loginButton = view.findViewById(R.id.button_login);
                    loginButton.setVisibility(View.VISIBLE);

                    // Show Forgot Password button
                    Button forgotPasswordButton = view.findViewById(R.id.button_forgotPassword);
                    forgotPasswordButton.setVisibility(View.VISIBLE);

                    // Hide progress bar
                    ProgressBar loginProgress = view.findViewById(R.id.progress_login);
                    loginProgress.setVisibility(View.GONE);

                    // Let the user know they were not logged in successfully
                    Toast.makeText(context, context.getResources().getString(R.string.invalid_user), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Custom method to reset user password
    public static void forgotPassword(String email) {

        // Send password reset e-mail to entered email address
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {}
        });
    }

    // Custom method to create a new user
    public static void createUser(final User user, final Context context) {
        // Reference database
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Create detail record for new user
        database.collection(COLLECTION_USERS).document(user.getuID()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Check for successful login
                checkForAuth(context);
            }
        })

        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });
    }

    // Custom method to cache user data
    private static void cacheUser(User user, Context context) {
        // Reference target directory and file name
        File targetDir = context.getFilesDir();
        File userFile = new File(targetDir + FILE_USER);

        try {
            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(userFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write user information to file
            objectOutputStream.writeObject(user);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Custom method to log current user out
    public static void logout(Context context) {
        // Log user out
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        // Clear cached data
        JoyUtils joyUtils = new JoyUtils(context);
        joyUtils.clearCache();
    }


    // ** PROGRESS DATA METHODS **
    // Custom method to save daily progress to cloud storage
    public static void saveDailyProgress(Joy joy) {

        // Reference database and current user
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && joy != null) {
            // Write daily progress to cloud storage
            database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_PROGRESS).document(DOC_DAILY_PROGRESS)
                    .set(joy).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {}
            });
        }
    }

    // Custom method to save lifetime progress to cloud storage
    public static void saveLifetimeProgress(Joy joy) {

        // Reference database and current user
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && joy != null) {
            // Write lifetime progress to cloud storage
            database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_PROGRESS).document(DOC_LIFETIME_PROGRESS)
                    .set(joy).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {}
            });
        }
    }

    // Custom method to save weekly give actions to cloud storage
    public static void saveWeeklyGiveActions(ArrayList<Give> gives) {

        // Reference database and current user
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && gives != null) {

            // Loop through each action and save to cloud storage
            for(Give give : gives) {
                database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_GIVEN).document(give.getDateCreated().toString())
                        .set(give).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {}
                });
            }
        }
    }

    // Custom method to save weekly received actions to cloud storage
    public static void saveWeeklyReceivedActions(ArrayList<Get> receives) {

        // Reference database and current user
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && receives != null) {

            // Loop through each action and save to cloud storage
            for(Get received : receives) {
                database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_RECEIVED).document(received.getDateCreated().toString())
                        .set(received).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {}
                });
            }
        }
    }

    //** NOTE: The following methods fire in a cascading effect until all required data has been loaded from cloud storage
    // Custom method to load daily progress from cloud storage
    private static void loadDailyProgress(final Context context) {

        // Reference database and current user
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        // Reference daily progress document
        DocumentReference documentReference = database.collection(COLLECTION_USERS).document(Objects.requireNonNull(user).getUid())
                .collection(COLLECTION_PROGRESS).document(DOC_DAILY_PROGRESS);

        // Attempt to load document from cloud storage
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    // If the document exists...
                    if(Objects.requireNonNull(documentSnapshot).exists()) {

                        // Reference resulting document. Pass to method to load lifetime progress
                        Joy dailyProgress = documentSnapshot.toObject(Joy.class);
                        loadLifetimeProgress(context, database, user, dailyProgress);
                    }

                    // Otherwise, move on with null daily progress data
                    else {
                        loadLifetimeProgress(context, database, user, null);
                    }
                }

                // Otherwise, move on with null daily progress data
                else {
                    loadLifetimeProgress(context, database, user, null);
                }
            }
        });
    }

    // Custom method to load lifetime progress from cloud storage
    private static void loadLifetimeProgress(final Context context, final FirebaseFirestore database, final FirebaseUser user, final Joy dailyProgress) {

        // Reference desired document
        DocumentReference documentReference = database.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_PROGRESS).document(DOC_LIFETIME_PROGRESS);

        // Attempt to load document from cloud storage
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    // If the document exists...
                    if(Objects.requireNonNull(documentSnapshot).exists()) {

                        // Reference resulting document. Pass to methdd to load weekly give data
                        Joy lifetimeProgress = documentSnapshot.toObject(Joy.class);
                        loadWeeklyGives(context, database, user, dailyProgress, lifetimeProgress);
                    }

                    // Otherwise, move on with null lifetime progress data
                    else {
                        loadWeeklyGives(context, database, user, dailyProgress, null);
                    }
                }

                // Otherwise, move on with null lifetime progress data
                else {
                    loadWeeklyGives(context, database, user, dailyProgress, null);
                }
            }
        });
    }

    // Custom method to load weekly give data from cloud storage
    private static void loadWeeklyGives(final Context context, final FirebaseFirestore database, final FirebaseUser user,
                                        final Joy dailyProgress, final Joy lifetimeProgress) {

        // Reference desired collection
        CollectionReference collectionReference = database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_GIVEN);

        // Calculate date seven days in the past
        // This will be used to ONLY pull data from the past seven days
        Date currentDate = new Date();
        Date minDate = new Date(currentDate.getTime() - 604800000L);

        // Reference desired documents in collection
        collectionReference.whereGreaterThanOrEqualTo(FIELD_DATE, minDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    // Get resulting documents
                    List<DocumentSnapshot> documentSnapshots = Objects.requireNonNull(task.getResult()).getDocuments();

                    if (documentSnapshots.size() > 0) {

                        // Prepare to create list of Give objects from pulled documents
                        ArrayList<Give> weeklyGiven = new ArrayList<>();

                        // Loop through documents, convert to Give objects, and add to list
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Give given = documentSnapshot.toObject(Give.class);
                            weeklyGiven.add(given);
                        }

                        // Begin to load weekly receives. Pass loaded weekly gives into method.
                        loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, weeklyGiven);
                    }

                    // Otherwise move on with null weekly gives data
                    else {
                        loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, null);
                    }
                }

                // Otherwise move on with null weekly gives data
                else {
                    loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, null);
                }
            }
        });
    }

    // Custom method to load weekly get data from cloud storage
    private static void loadWeeklyReceives(final Context context, final FirebaseFirestore databsae, final FirebaseUser user,
                                           final Joy dailyProgress, final Joy lifetimeProgress, final ArrayList<Give> weeklyGiven) {

        // Reference the desired collection
        CollectionReference collectionReference = databsae.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_RECEIVED);

        // Calculate date seven days in the past
        // This will be used to ONLY pull data from the past seven days
        Date currentDate = new Date();
        Date minDate = new Date(currentDate.getTime() - 604800000L);

        // Reference desired documents in collection
        collectionReference.whereGreaterThanOrEqualTo(FIELD_DATE, minDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    // Get resulting documents
                    List<DocumentSnapshot> documentSnapshots = Objects.requireNonNull(task.getResult()).getDocuments();

                    if(documentSnapshots.size() > 0) {

                        // Prepare to cereate list of Get objects from pulled documents
                        ArrayList<Get> weeklyReceived = new ArrayList<>();

                        // Loop through documents, convert to Get objects, and add to list
                        for(DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Get received = documentSnapshot.toObject(Get.class);
                            weeklyReceived.add(received);
                        }

                        // Call custom method to cache all loaded data, passing weekly get data in with all other loaded data
                        cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, weeklyReceived);
                    }

                    // Otherwise move on with null weekly get data
                    else {
                        cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, null);
                    }
                }

                // Otherwise move on with null weekly get data
                else {
                    cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, null);
                }
            }
        });
    }

    // Custom method to cache all loaded data and move on to next stage in the app
    private static void cacheAndContinue(final Context context, FirebaseFirestore database, final FirebaseUser user, Joy dailyProgress, Joy lifetimeProgress,
                                         ArrayList<Give> weeklyGiven, ArrayList<Get> weeklyReceived) {

        // Only proceed if a valid user exists
        if(user != null) {

            // Retrieve and cache user details
            DocumentReference documentReference = database.collection(COLLECTION_USERS).document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if(Objects.requireNonNull(document).exists()) {
                            User loadedUser = document.toObject(User.class);
                            cacheUser(loadedUser, context);
                        }
                    }
                }
            });
        }

        // Save data pulled from cloud storage to local storage
        JoyUtils joyUtils = new JoyUtils(context);
        joyUtils.saveProgress(dailyProgress, lifetimeProgress, weeklyGiven, weeklyReceived);

        // Start Login activity
        Intent loginIntent = new Intent(context, DailyDetailsActivity.class);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }
}
