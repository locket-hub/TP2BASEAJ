package entityClasses;

import java.util.Date;

/*******
 * <p> Title: Post Class </p>
 * 
 * <p> Description: This Post class represents a post entity in the system.  It contains the users' post
 *  details such as postID, userID, threads, postContent, timestamp, and whether it's been deleted
 *  by student. </p>
 * 
 * @author team37
 * 
 * 
 */ 

public class Post {
	
	/*
	 * These are the private attributes for this entity object
	 */
    private int postID;
    private String thread;
    private int userID;
    private String postContent;
    private Date timestamp;
    private boolean isDeleted;

    
    /*****
     * <p> Method: Post(int postID, int userID, String thread, String postContent, Date timestamp,
     * 	   boolean isDeleted) </p>
     * 
     * <p> Description: This constructor is used to establish post entity objects. </p>
     * 
     * @param postID specifies the unique ID for this post
     * 
     * @param userID specifies the unique userID associated with this post 
     * 
     * @param thread specifies the thread category for this post
     * 
     * @param postContent specifies the content text for this post
     * 
     * @param timestamp specifies the time and date of this post
     * 
     * @param isDeleted specifies whether or not this post has been deleted (TRUE or FALSE)
     *      
     */
    // Constructor to initialize a new Post object with postID, userID, thread, password, and role.
    public Post(int postID, int userID, String thread, String postContent, Date timestamp, boolean isDeleted) {
        this.postID = postID;
        this.userID = userID;
        this.thread = thread;
        this.postContent = postContent;
        this.timestamp = timestamp;
        this.isDeleted = isDeleted;
    }

    /*****
     * <p> Method: int getPostID() </p>
     * 
     * <p> Description: This getter returns unique postID number. </p>
     * 
     * @return postID: unique integer greater than 0
     * 
     */
    // Gets the user's postID.
    public int getPostID() {
        return postID;
    }

    /*****
     * <p> Method: void setRole1User(int postID) </p>
     * 
     * <p> Description: This setter defines the postID. </p>
     * 
     * @param postID is an integer that specifies a post's unique postID.
     * 
     */
    // Sets the user's postID.
    public void setPostID(int postID) {
        this.postID = postID;
    }
    
    /*****
     * <p> Method: String getThread() </p>
     * 
     * <p> Description: This getter returns the thread category of the post. </p>
     * 
     * @return thread: category name
     * 
     */
    // Gets the thread category of post.
    public String getThread() {
        return thread;
    }

    /*****
     * <p> Method: void setThread(String thread) </p>
     * 
     * <p> Description: This setter defines the thread category. </p>
     * 
     * @param thread is a String that specifies what thread the post is in.
     * 
     */
    // Sets the thread category of post.
    public void setThread(String thread) {
        this.thread = thread;
    }

    /*****
     * <p> Method: int getUserID() </p>
     * 
     * <p> Description: This getter returns the unique userID associated with the post. </p>
     * 
     * @return userID: unique integer greater than 0
     * 
     */
    // Gets the userID of post.
    public int getUserID() {
        return userID;
    }

    /*****
     * <p> Method: void setUserID(int userID) </p>
     * 
     * <p> Description: This setter defines the userID of the post. </p>
     * 
     * @param userID is an integer that specifies the userID of post.
     * 
     */
    // Sets the userID of post.
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /*****
     * <p> Method: String getPostContent() </p>
     * 
     * <p> Description: This getter returns content text of the post. </p>
     * 
     * @return postContent: characterText
     * 
     */
    // Gets the content text of post.
    public String getPostContent() {
        return postContent;
    }

    /*****
     * <p> Method: void setPostContent(String postContent) </p>
     * 
     * <p> Description: This setter defines the content text of the post. </p>
     * 
     * @param postContent is a String that specifies the content of post.
     * 
     */
    // Sets the content text of post.
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /*****
     * <p> Method: Date getTimestamp() </p>
     * 
     * <p> Description: This getter returns timestamp of the post. </p>
     * 
     * @return timestamp: Date and Time
     * 
     */
    // Gets the timestamp of post.
    public Date getTimestamp() {
        return timestamp;
    }

    /*****
     * <p> Method: void setTimestamp(Date timestamp) </p>
     * 
     * <p> Description: This setter defines the time and date of the post. </p>
     * 
     * @param timestamp is a Date that specifies the time the post was created.
     * 
     */
    // Sets the timestamp of post.
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /*****
     * <p> Method: boolean isDeleted() </p>
     * 
     * <p> Description: This getter returns whether or not the post has been deleted. </p>
     * 
     * @return isDeleted: True or False
     * 
     */
    // Gets the isDeleted status of post.
    public boolean isDeleted() {
        return isDeleted;
    }

    /*****
     * <p> Method: void setDeleted(boolean isDeleted) </p>
     * 
     * <p> Description: This setter defines whether or not the post has been deleted. </p>
     * 
     * @param isDeleted is a boolean that specifies, True or False, whether a post is to be deleted.
     * 
     */
    // Sets the isDeleted status of post.
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
