package guiRole1.tests;

/**
 * <p>Title: ViewCreatePostTests</p>
 *
 * <p> Description: This is where all of the tests for the ViewCreatePost class resides.
 * Since all of the tests are manual, each of the methods are empty stubs that contain the procedures of performing each test.
 * This class covers all of the tests regarding creating posts (User Story A).
 *
 * @author Blake Ranniker
 * </p>
 *


*/

public class ViewCreatePostTests {
	/**
	 * <p>Test: Creating a post, blank content field</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click the "Create Post" button
	 *  to bring you to the ViewCreatePost page.</p>
	 *
	 *  <p>2. Click "Submit Post"</p>
	 *
	 *  <p>Expected output: Red text under the content TextArea stating:
	 *  "Error: Post content cannot be empty." You are then exited from the page back to ViewRole1Home page.
	 *  </p>
	 */
	public void createPostNoContent() {
		//Empty method, manual test.
	}

	/**
	 * <p>Test: Setting up post creation but then canceling.</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click the "Create Post" button
	 *  to bring you to the ViewCreatePost page.</p>
	 *
	 *  <p>2. Select cancel.</p>
	 *
	 *  <p>Expected output: You should just be sent back to the ViewRole1Home page, with no post created by you.
	 *  </p>
	 */
	public void cancelCreatePost() {
		//Empty method, manual test.
	}

	/**
	 * <p>Test: Creating a post with between 1 and 300 words</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click the "Create Post" button
	 *  to bring you to the ViewCreatePost page.</p>
	 *
	 *  <p>2. Enter the desired post content. In this case, it must be between 1 and 300 words.</p>
	 *
	 *  <p>3. Click "Submit Post"</p>
	 *
	 *  <p>Expected output: A pop-up stating "Post created successfully!" When you click "OK," you should be exited back to the ViewRole1Home page.
	 *  </p>
	 */
	public void createPostWithContent() {
		//Empty method, manual test.
	}

	/**
	 * <p>Test: Creating a post with more than 300 words</p>
	 *
	 *  <p>Procedure:</p>
	 *  <p>1. Starting in the Student Home Page, click the "Create Post" button
	 *  to bring you to the ViewCreatePost page.</p>
	 *
	 *  <p>2. Enter the desired post content. In this case, it must be more than 300 words.</p>
	 *
	 *  <p>3. Click "Submit Post"</p>
	 *
	 *  <p>Expected output: Red text under the content TextArea stating:
	 *  "Error: Post content cannot be more than 300 words." You are then exited from the page back to ViewRole1Home page.
	 *  </p>
	 */
	public void createPostWithTooMuchContent() {
		//Empty method, manual test.
	}
}