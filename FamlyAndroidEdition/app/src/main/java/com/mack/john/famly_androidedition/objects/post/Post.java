package com.mack.john.famly_androidedition.objects.post;

import android.graphics.Bitmap;

import com.mack.john.famly_androidedition.objects.account.Account;
import com.mack.john.famly_androidedition.objects.account.profile.Profile;
import com.mack.john.famly_androidedition.utils.AccountUtils;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable, Comparable<Post> {



    // Class properties
    String postMessage;
    Date timeStamp;
    String posterId;
    String posterName;
    boolean hasImage;



    // Constructor
    public Post(String postMessage, Date timeStamp, String posterId, String posterName, boolean hasImage) {
        this.postMessage = postMessage;
        this.timeStamp = timeStamp;
        this.posterId = posterId;
        this.posterName = posterName;
        this.hasImage = hasImage;
    }



    // System generated methods
    @Override
    public int compareTo(Post post) {
        return getTimeStamp().compareTo(post.getTimeStamp());
    }




    // Getters / Setters
    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }




    // Custom methods
    public String getPostId() {
        return posterId + timeStamp.getTime();
    }
}
