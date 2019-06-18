package com.mack.john.famly_androidedition.objects.permission_request;

import com.mack.john.famly_androidedition.objects.post.Post;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable, Comparable<Request> {



    // Class properties
    String requesterName;
    String requesterId;
    String requestMessage;
    int requestStatus; // 0 == Pending; 1 == Approved; 2 == Denied
    Date timeStamp;
    String firstApprover;



    // Constructor
    public Request(String requesterName, String requesterId, String requestMessage, Date timeStamp) {
        this.requesterName = requesterName;
        this.requesterId = requesterId;
        this.requestMessage = requestMessage;
        this.requestStatus = requestStatus = 0; // 0 == Pending; 1 == Approved; 2 == Denied
        this.timeStamp = timeStamp;
        this.firstApprover = null;
    }



    // System generated methods
    @Override
    public int compareTo(Request request) {
        return getTimeStamp().compareTo(request.getTimeStamp());
    }




    // Getters / Setters
    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFirstApprover() {
        return firstApprover;
    }

    public void setFirstApprover(String firstApprover) {
        this.firstApprover = firstApprover;
    }




    // Custom method
    public String getRequestId() {
        String requestId = getRequesterId() + getTimeStamp().getTime();

        return requestId;
    }
}
