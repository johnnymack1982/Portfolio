package com.mack.john.famly_androidedition.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mack.john.famly_androidedition.R;
import com.mack.john.famly_androidedition.adapters.ChildPermissionRequestAdapter;
import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Child;
import com.mack.john.famly_androidedition.objects.account.profile.Parent;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.objects.permission_request.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PermissionRequestUtils {



    // Custom methods
    // Custom method to send permission request
    public static void sendRequest(final Context context, String requestMessage, final Profile requester) {

        // Reference firebase user account and database
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Create new request object
        final Request request = new Request(requester.getFirstName(), requester.getProfileId(), requestMessage, new Date());

        // Map request for upload
        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        // Reference database location and attempt to upload
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + requester.getProfileId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // If successful...
                if(task.isSuccessful()) {

                    // Add request to profile and sort request list
                    ArrayList<Request> requests = loadRequests(context);
                    requests.add(request);
                    Collections.sort(requests);
                    Collections.reverse(requests);

                    // Save requests to file
                    saveRequests(context, requests);

                    // Load account
                    Account account = AccountUtils.loadAccount(context);

                    // Let the user know the request has been sent
                    Toast.makeText(context, context.getString(R.string.request_sent), Toast.LENGTH_SHORT).show();

                    // Loop through profiles on account
                    for(Profile profile : account.getProfiles()) {

                        // If profile is a parent add request to incoming requests for profile
                        if(profile instanceof Parent) {

                            // Reference databse location and attempt to upload
                            database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                    .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                    .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                    }

                                    // If there is a problem, let the user know
                                    else {
                                        Toast.makeText(context, context.getString(R.string.request_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

                // If there is a problem, let the user know
                else {
                    Toast.makeText(context, context.getString(R.string.request_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Custom method to receive incoming permission requests
    public static void receiveRequests(final Context context, final Profile profile) {

        // Reference firebase user account and database
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference databse location and add listeer
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }

                        else {

                            // Create blank request list
                            ArrayList<Request> requests = new ArrayList<>();

                            // Loop through lists in database
                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {

                                // Reference request data
                                String requesterName = document.getString("requesterName");
                                String requesterId = document.getString("requesterId");
                                String requestMessage = document.getString("requestMessage");
                                int requestStatus = document.getLong("requestStatus").intValue();
                                Date timeStamp = document.getDate("timeStamp");
                                String firstApprover = document.getString("firstApprover");

                                // Create new request obect
                                Request request = new Request(requesterName, requesterId, requestMessage, timeStamp);
                                request.setRequestStatus(requestStatus);

                                if(firstApprover != null && firstApprover != "") {
                                    request.setFirstApprover(firstApprover);
                                }

                                // Add request to list
                                requests.add(request);
                            }

                            // Save request list to file
                            saveRequests(context, requests);
                        }
                    }
                });
    }

    // Custom method to receive request responses
    public static void receiveResponses(final Context context, Profile profile) {

        // Reference firebase user account and database
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Reference database location and set listener
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }

                        else {

                            // Create empty request list
                            ArrayList<Request> requests = new ArrayList<>();

                            // Loop through requests in database
                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {

                                // Reference request data
                                String requesterName = document.getString("requesterName");
                                String requesterId = document.getString("requesterId");
                                String requestMessage = document.getString("requestMessage");
                                int requestStatus = document.getLong("requestStatus").intValue();
                                Date timeStamp = document.getDate("timeStamp");
                                String firstApprover = document.getString("firstApprover");

                                // Create new request object
                                Request request = new Request(requesterName, requesterId, requestMessage, timeStamp);
                                request.setRequestStatus(requestStatus);

                                if(firstApprover != null && firstApprover != "") {
                                    request.setFirstApprover(firstApprover);
                                }

                                // Add request to list
                                requests.add(request);
                            }

                            // Save requests to file
                            saveRequests(context, requests);
                        }
                    }
                });
    }

    // Custom method to approve permission request
    public static void approveRequest(final Context context, final Request request, final ImageButton grantButton, final ImageButton denyButton) {

        // Reference firebase user account and database
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Indicate request is approved by this parent
        request.setRequestStatus(request.getRequestStatus() + 1);
        request.setFirstApprover(AccountUtils.loadProfile(context).getProfileId());

        // Add updated request to list
        final ArrayList<Request> requests = loadRequests(context);
        requests.add(request);

        // Load account
        final Account account = AccountUtils.loadAccount(context);

        // Map request for upload
        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        // Reference databse location and attempt to upload
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + request.getRequesterId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // Loop through profiles in account
                for(Profile profile : account.getProfiles()) {

                    // If profile is a paret...
                    if(profile instanceof Parent) {

                        // Reference database location and attempt to upload
                        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // If successful...
                                if(task.isSuccessful()) {

                                    // Visually indicate request is approved by this parent
                                    grantButton.setImageAlpha(1000);
                                    denyButton.setVisibility(View.INVISIBLE);

                                    // Save requests to file
                                    saveRequests(context, requests);

                                    // Let user know the request has been approved by this parent
                                    Toast.makeText(context, context.getString(R.string.request_approved), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // Custom method to deny permissio request
    public static void denyRequest(final Context context, final Request request, final ImageButton grantButton, final ImageButton denyButton) {

        // Reference firebase user account and databse
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Indicate request is denied
        request.setRequestStatus(-1);

        // Update request in request list
        final ArrayList<Request> requests = loadRequests(context);
        final Account account = AccountUtils.loadAccount(context);

        // Map request for upload
        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        // Reference database location and attempt to upload
        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + request.getRequesterId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // Loop through profiles in account
                for(Profile profile : account.getProfiles()) {

                    // If profile is a parent...
                    if(profile instanceof Parent) {

                        // Reference database location and attempt to upload
                        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // If successful...
                                if(task.isSuccessful()) {

                                    // Visually indicate that request has been denied
                                    denyButton.setImageAlpha(1000);
                                    grantButton.setVisibility(View.INVISIBLE);

                                    // Save requests to file
                                    saveRequests(context, requests);

                                    // Let the user know they have denied the request
                                    Toast.makeText(context, context.getString(R.string.request_approved), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // Custom method to load requests from file
    public static final ArrayList<Request> loadRequests(Context context) {

        // Create empty request list
        ArrayList<Request> requests = new ArrayList<>();

        // Reference file location
        File targetDir = context.getFilesDir();
        File requestsFile = new File(targetDir + "requests.fam");

        try {

            // Open iput streams
            FileInputStream fileInputStream = new FileInputStream(requestsFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            // Load requests
            requests = (ArrayList<Request>) objectInputStream.readObject();

            // Close input streams
            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Sort request list
        Collections.sort(requests);
        Collections.reverse(requests);

        // Return request list
        return requests;
    }

    // Custom method to save requests to file
    public static final void saveRequests(Context context, ArrayList<Request> requests) {

        // Reference file location
        File targetDir = context.getFilesDir();
        File requestsFile = new File(targetDir + "requests.fam");

        try {

            // Open output streams
            FileOutputStream fileOutputStream = new FileOutputStream(requestsFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            // Write requests
            objectOutputStream.writeObject(requests);

            // Close output streams
            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
