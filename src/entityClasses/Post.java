package entityClasses;

import java.util.Date;

public class Post {
    private int postID;
    private String thread;
    private int userID;
    private String postContent;
    private Date timestamp;
    private boolean isDeleted;

    public Post(int postID, int userID, String thread, String postContent, Date timestamp, boolean isDeleted) {
        this.postID = postID;
        this.userID = userID;
        this.thread = thread;
        this.postContent = postContent;
        this.timestamp = timestamp;
        this.isDeleted = isDeleted;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
