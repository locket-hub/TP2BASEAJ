package entityClasses;

import java.util.Date;

public class Reply {
    private int replyID;
    private int postID;
    private int userID;
    private String replyContent;
    private Date timestamp;

    public Reply(int replyID, int postID, int userID, String replyContent, Date timestamp) {
        this.replyID = replyID;
        this.postID = postID;
        this.userID = userID;
        this.replyContent = replyContent;
        this.timestamp = timestamp;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
