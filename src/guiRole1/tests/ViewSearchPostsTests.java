package guiRole1.tests;

/**
 * <p>Title: ViewSearchPosts Tests</p>
 * 
 * <p> Description: This is where all of the tests for the ViewSearchPosts class resides.
 * Since all of the tests are manual, each of the methods are empty stubs that contain the procedures of performing each test.
 * This class covers all of the tests regarding searching for posts (User Story G).
 * 
 * @author Blake Ranniker
 * </p>
 * 


*/

public class ViewSearchPostsTests {
	/**
	 * <p>Test: Searching for a post, not including a keyword</p> 
	 *  
	 *  <p>Procedure:
	 *  1. Starting in the Student Home Page, click the "Search" button
	 *  to bring you to the ViewSearchPosts page.
	 *  
	 *  2. Press search.
	 *  
	 *  Expected output: Red text under the 2nd horizontal separator that states:
	 *  "Please enter a search keyword."
	 *  </p>
	 */
	public void searchPostNoKeyword() {
		//Empty method, manual test.
	}
	
	/**
	 * <p>Test: Searching for a post with a keyword</p> 
	 *  
	 *  <p>Procedure:
	 *  1. Starting in the Student Home Page, click the "Search" button
	 *  to bring you to the ViewSearchPosts page.
	 *  
	 *  2. In the TextBox next to "Enter search keyword:", enter the keyword
	 *  of your choosing.
	 *  
	 *  3. Press search.
	 *  
	 *  Expected output: There are two different expected outputs, depending on what posts exist.
	 *  
	 *  If there are no posts that match the keywords, you should see a Label stating: 
	 *  "No results found for: '{your entered keyword}'" and the box beneath it stating:
	 *  "No post contains this keyword. Try different keywords."
	 *  
	 *  If there is one post or more that matches the keyword, it will be listed in the box
	 *  beneath the search bar. 
	 *  </p>
	 */
	public void searchPostWithKeyword() {
		//Empty method, manual test.
	}
}