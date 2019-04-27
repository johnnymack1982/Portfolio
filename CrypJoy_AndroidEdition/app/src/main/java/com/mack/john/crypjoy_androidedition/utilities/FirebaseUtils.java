package com.mack.john.crypjoy_androidedition.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mack.john.crypjoy_androidedition.DailyDetailsActivity;
import com.mack.john.crypjoy_androidedition.LoginActivity;
import com.mack.john.crypjoy_androidedition.R;
import com.mack.john.crypjoy_androidedition.objects.Get;
import com.mack.john.crypjoy_androidedition.objects.Give;
import com.mack.john.crypjoy_androidedition.objects.Joy;
import com.mack.john.crypjoy_androidedition.objects.User;

import org.joda.time.Days;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirebaseUtils {



    // Class properties
    private static final String TAG = "FirebaseUtils";

    public static final String COLLECTION_USERS = "users";

    public static final String COLLECTION_PROGRESS = "collection_progress";
    public static final String DOC_DAILY_PROGRESS = "daily_progress";
    public static final String DOC_LIFETIME_PROGRESS = "lifetime_progress";

    public static final String COLLECTION_WEEKLY_GIVEN = "weekly_given";
    public static final String COLLECTION_WEEKLY_RECEIVED = "weekly_received";

    public static final String FIELD_DATE = "dateCreated";
    public static final String FIELD_FIRST_NAME = "firstName";

    public static final String FILE_USER = "user.joy";

    private static User mUser;



    // Custom methods
    public static void checkForAuth(final Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        if(firebaseUser != null) {
            DocumentReference documentReference = database.collection(COLLECTION_USERS).document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if(document.exists()) {
                            User user = document.toObject(User.class);
                            cacheUser(user, context);

                            loadDailyProgress(context);
                        }

                        else {
                            launchLogin(context);
                        }
                    }

                    else {
                        launchLogin(context);
                    }
                }
            });
        }

        else {
            launchLogin(context);
        }
    }

    private static void launchLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }

    public static void login(String eMail, String password, final Context context, final View view) {
        FirebaseAuth user = FirebaseAuth.getInstance();

        user.signInWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    checkForAuth(context);
                }

                else {
                    Log.d(TAG, "onComplete: Login unsuccessful");

                    Button loginButton = view.findViewById(R.id.button_login);
                    ProgressBar loginProgress = view.findViewById(R.id.progress_login);

                    loginButton.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.GONE);

                    Toast.makeText(context, context.getResources().getString(R.string.invalid_user), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static void createUser(final User user, final Context context) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(COLLECTION_USERS).document(user.getuID()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

                checkForAuth(context);
            }
        })

        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    private static void cacheUser(User user, Context context) {
        File targetDir = context.getFilesDir();
        File userFile = new File(targetDir + FILE_USER);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(userFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(user);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logout(Context context) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        JoyUtils joyUtils = new JoyUtils(context);
        joyUtils.clearCache();
    }

    public static void saveDailyProgress(Joy joy) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && joy != null) {
            database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_PROGRESS).document(DOC_DAILY_PROGRESS)
                    .set(joy).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            });
        }
    }

    public static void saveLifetimeProgress(Joy joy) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && joy != null) {
            database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_PROGRESS).document(DOC_LIFETIME_PROGRESS)
                    .set(joy).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            });
        }
    }

    public static void saveWeeklyGiveActions(ArrayList<Give> gives) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && gives != null) {
            for(Give give : gives) {
                database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_GIVEN).document(give.getDateCreated().toString())
                        .set(give).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                });
            }
        }
    }

    public static void saveWeeklyReceivedActions(ArrayList<Get> receives) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null && receives != null) {
            for(Get received : receives) {
                database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_RECEIVED).document(received.getDateCreated().toString())
                        .set(received).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                });
            }
        }
    }

    public static void loadDailyProgress(final Context context) {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        DocumentReference documentReference = database.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_PROGRESS).document(DOC_DAILY_PROGRESS);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()) {
                        Joy dailyProgress = documentSnapshot.toObject(Joy.class);

                        loadLifetimeProgress(context, database, user, dailyProgress);
                    }

                    else {
                        Log.d(TAG, "Daily progress document does not exist");

                        loadLifetimeProgress(context, database, user, null);
                    }
                }

                else {
                    Log.d(TAG, "get failed with ", task.getException());

                    loadLifetimeProgress(context, database, user, null);
                }
            }
        });
    }

    public static void loadLifetimeProgress(final Context context, final FirebaseFirestore database, final FirebaseUser user, final Joy dailyProgress) {
        DocumentReference documentReference = database.collection(COLLECTION_USERS).document(user.getUid())
                .collection(COLLECTION_PROGRESS).document(DOC_LIFETIME_PROGRESS);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()) {
                        Joy lifetimeProgress = documentSnapshot.toObject(Joy.class);

                        loadWeeklyGives(context, database, user, dailyProgress, lifetimeProgress);
                    }

                    else {
                        Log.d(TAG, "Lifetime progress document does not exist");

                        loadWeeklyGives(context, database, user, dailyProgress, null);
                    }
                }

                else {
                    Log.d(TAG, "get failed with ", task.getException());

                    loadWeeklyGives(context, database, user, dailyProgress, null);
                }
            }
        });
    }

    public static void loadWeeklyGives(final Context context, final FirebaseFirestore database, final FirebaseUser user, final Joy dailyProgress, final Joy lifetimeProgress) {
        CollectionReference collectionReference = database.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_GIVEN);

        Date currentDate = new Date();
        Date minDate = new Date(currentDate.getTime() - 604800000L);


        collectionReference.whereGreaterThanOrEqualTo(FIELD_DATE, minDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                    if (documentSnapshots != null && documentSnapshots.size() > 0) {
                        ArrayList<Give> weeklyGiven = new ArrayList<>();

                        // Create calendar item to reference date elements for date/time stamp of object being checked
                        Calendar createdCalendar = Calendar.getInstance();

                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Give given = documentSnapshot.toObject(Give.class);
                            weeklyGiven.add(given);
                        }

                        loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, weeklyGiven);
                    }

                    else {
                        Log.d(TAG, "Weekly gives do not exist");

                        loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, null);
                    }
                }

                else {
                    Log.d(TAG, "get failed with ", task.getException());

                    loadWeeklyReceives(context, database, user, dailyProgress, lifetimeProgress, null);
                }
            }
        });
    }

    public static void loadWeeklyReceives(final Context context, final FirebaseFirestore databsae, final FirebaseUser user, final Joy dailyProgress, final Joy lifetimeProgress,
                                          final ArrayList<Give> weeklyGiven) {

        CollectionReference collectionReference = databsae.collection(COLLECTION_USERS).document(user.getUid()).collection(COLLECTION_WEEKLY_RECEIVED);

        Date currentDate = new Date();
        Date minDate = new Date(currentDate.getTime() - 604800000L);

        collectionReference.whereGreaterThanOrEqualTo(FIELD_DATE, minDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                    if(documentSnapshots != null && documentSnapshots.size() > 0) {
                        ArrayList<Get> weeklyReceived = new ArrayList<>();

                        for(DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Get received = documentSnapshot.toObject(Get.class);
                            weeklyReceived.add(received);
                        }

                        cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, weeklyReceived);
                    }

                    else {
                        Log.d(TAG, "Weekly receives do not exist");

                        cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, null);
                    }
                }

                else {
                    Log.d(TAG, "get failed with ", task.getException());

                    cacheAndContinue(context, databsae, user, dailyProgress, lifetimeProgress, weeklyGiven, null);
                }
            }
        });
    }

    public static void cacheAndContinue(final Context context, FirebaseFirestore database, final FirebaseUser user, Joy dailyProgress, Joy lifetimeProgress,
                                        ArrayList<Give> weeklyGiven, ArrayList<Get> weeklyReceived) {

        if(user != null) {
            DocumentReference documentReference = database.collection(COLLECTION_USERS).document(user.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if(document.exists()) {
                            User loadedUser = document.toObject(User.class);
                            cacheUser(loadedUser, context);
                        }
                    }
                }
            });
        }

        JoyUtils joyUtils = new JoyUtils(context);
        joyUtils.saveProgress(dailyProgress, lifetimeProgress, weeklyGiven, weeklyReceived);

        Intent loginIntent = new Intent(context, DailyDetailsActivity.class);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }
}
