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
    public static void sendRequest(final Context context, String requestMessage, final Profile requester) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        
        final Request request = new Request(requester.getFirstName(), requester.getProfileId(), requestMessage, new Date());

        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + requester.getProfileId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    ArrayList<Request> requests = loadRequests(context);
                    requests.add(request);

                    Collections.sort(requests);
                    Collections.reverse(requests);

                    saveRequests(context, requests);

                    Account account = AccountUtils.loadAccount(context);

                    Toast.makeText(context, context.getString(R.string.request_sent), Toast.LENGTH_SHORT).show();

                    for(Profile profile : account.getProfiles()) {
                        if(profile instanceof Parent) {
                            database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                    .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                    .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                    }

                                    else {
                                        Toast.makeText(context, context.getString(R.string.request_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
                
                else {
                    Toast.makeText(context, context.getString(R.string.request_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void receiveRequests(final Context context, final Profile profile) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }

                        else {
                            ArrayList<Request> requests = new ArrayList<>();

                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                String requesterName = document.getString("requesterName");
                                String requesterId = document.getString("requesterId");
                                String requestMessage = document.getString("requestMessage");
                                int requestStatus = document.getLong("requestStatus").intValue();
                                Date timeStamp = document.getDate("timeStamp");
                                String firstApprover = document.getString("firstApprover");

                                Request request = new Request(requesterName, requesterId, requestMessage, timeStamp);
                                request.setRequestStatus(requestStatus);

                                if(firstApprover != null && firstApprover != "") {
                                    request.setFirstApprover(firstApprover);
                                }

                                requests.add(request);
                            }

                            saveRequests(context, requests);
                        }
                    }
                });
    }

    public static void receiveResponses(final Context context, Profile profile) {
        FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            e.printStackTrace();
                        }

                        else {
                            ArrayList<Request> requests = new ArrayList<>();

                            for(QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                String requesterName = document.getString("requesterName");
                                String requesterId = document.getString("requesterId");
                                String requestMessage = document.getString("requestMessage");
                                int requestStatus = document.getLong("requestStatus").intValue();
                                Date timeStamp = document.getDate("timeStamp");
                                String firstApprover = document.getString("firstApprover");

                                Request request = new Request(requesterName, requesterId, requestMessage, timeStamp);
                                request.setRequestStatus(requestStatus);

                                if(firstApprover != null && firstApprover != "") {
                                    request.setFirstApprover(firstApprover);
                                }

                                requests.add(request);
                            }

                            saveRequests(context, requests);
                        }
                    }
                });
    }
    
    public static void approveRequest(final Context context, final Request request, final ImageButton grantButton, final ImageButton denyButton) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        
        request.setRequestStatus(request.getRequestStatus() + 1);
        request.setFirstApprover(AccountUtils.loadProfile(context).getProfileId());

        final ArrayList<Request> requests = loadRequests(context);
        requests.add(request);
        
        final Account account = AccountUtils.loadAccount(context);

        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + request.getRequesterId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for(Profile profile : account.getProfiles()) {
                    if(profile instanceof Parent) {
                        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    grantButton.setImageAlpha(1000);
                                    denyButton.setVisibility(View.INVISIBLE);

                                    saveRequests(context, requests);

                                    Toast.makeText(context, context.getString(R.string.request_approved), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static void denyRequest(final Context context, final Request request, final ImageButton grantButton, final ImageButton denyButton) {
        final FirebaseUser firebaseAccount = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        request.setRequestStatus(-1);

        final ArrayList<Request> requests = loadRequests(context);

        final Account account = AccountUtils.loadAccount(context);

        final Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requesterName", request.getRequesterName());
        requestMap.put("requesterId", request.getRequesterId());
        requestMap.put("requestMessage", request.getRequestMessage());
        requestMap.put("requestStatus", request.getRequestStatus());
        requestMap.put("timeStamp", request.getTimeStamp());
        requestMap.put("firstApprover", request.getFirstApprover());

        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                .document(firebaseAccount.getUid() + request.getRequesterId()).collection("requests")
                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                for(Profile profile : account.getProfiles()) {
                    if(profile instanceof Parent) {
                        database.collection("accounts").document(firebaseAccount.getUid()).collection("profiles")
                                .document(firebaseAccount.getUid() + profile.getProfileId()).collection("incomingRequests")
                                .document(request.getRequestId()).set(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    denyButton.setImageAlpha(1000);
                                    grantButton.setVisibility(View.INVISIBLE);

                                    saveRequests(context, requests);

                                    Toast.makeText(context, context.getString(R.string.request_approved), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static final ArrayList<Request> loadRequests(Context context) {
        ArrayList<Request> requests = new ArrayList<>();

        File targetDir = context.getFilesDir();
        File requestsFile = new File(targetDir + "requests.fam");

        try {
            FileInputStream fileInputStream = new FileInputStream(requestsFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            requests = (ArrayList<Request>) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }

        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Collections.sort(requests);
        Collections.reverse(requests);

        return requests;
    }

    public static final void saveRequests(Context context, ArrayList<Request> requests) {
        File targetDir = context.getFilesDir();
        File requestsFile = new File(targetDir + "requests.fam");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(requestsFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(requests);

            objectOutputStream.close();
            fileOutputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
