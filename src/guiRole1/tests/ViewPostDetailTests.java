package guiRole1.tests;

/**
 * <p>Title: ViewPostDetailTests</p>
 *
 * <p> Description: This is where all of the tests for the ViewPostDetail class resides.
 * Since all of the tests are manual, each of the methods are empty stubs that contain the procedures of performing each test.
 * This class covers all of the tests regarding viewing posts.
 *
 * @author Blake Ranniker
 * </p>
 *


*/

public class ViewPostDetailTests {
	/**
	 * <p>Test: Canceling the deletion of one of your posts.</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click on a post you made that you want to delete.
	 *  This brings you to the ViewPostDetail page.</p>
	 *
	 *  <p>2. Click "Delete Post." On the "Are you sure?" Alert, select "cancel".</p>
	 *
	 *  <p>Expected output: There should be no real change in output here. The pop-up should disappear and your post content
	 *  should still be accessible.
	 *  </p>
	 */
	public void cancelDeleteMyPost() {
		//Empty method, manual test.
	}

	/**
	 * <p>Test: Deleting one of your posts.</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click on a post you made that you want to delete.
	 *  This brings you to the ViewPostDetail page.</p>
	 *
	 *  <p>2. Click "Delete Post." On the "Are you sure?" Alert, select yes.</p>
	 *
	 *  <p>Expected output: Pop-up stating "Post deleted successfully!" You are then sent back to the ViewRole1Home page.
	 *  Upon looking at the post you deleted, you should see that the contents have changed. It should now say something along the lines of:
	 *  "This post has been deleted by its author."
	 *  </p>
	 */
	public void deleteMyPost() {
		//Empty method, manual test.
	}

	/**
	 * <p>Test: Deleting one of your posts.</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click on a post made by someone else that you want to (try to) delete.
	 *  This brings you to the ViewPostDetail page.</p>
	 *
	 *  <p>2. There should be no "Delete Post" button for you to click. This validates that you can only delete your own posts.
	 *  </p>
	 */
	public void deleteOtherPost() {
		//Empty method, manual test.
	}
}