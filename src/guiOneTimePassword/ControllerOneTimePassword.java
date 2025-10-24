package guiOneTimePassword;

import database.Database;

public class ControllerOneTimePassword {
	
	/*-********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	
	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectUser() {
		ViewOneTimePassword.theSelectedUser = 
				(String) ViewOneTimePassword.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewOneTimePassword.theSelectedUser);
		setupSelectedUser();
	}
	
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewOneTimePassword.theRootPane.getChildren().clear();
		
		// Determine which of the two views to show to the user
		if (ViewOneTimePassword.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			ViewOneTimePassword.theRootPane.getChildren().addAll(
					ViewOneTimePassword.label_PageTitle, ViewOneTimePassword.label_UserDetails, 
					ViewOneTimePassword.button_UpdateThisUser, ViewOneTimePassword.line_Separator1,
					ViewOneTimePassword.label_SelectUser, ViewOneTimePassword.combobox_SelectUser, 
					ViewOneTimePassword.line_Separator4, ViewOneTimePassword.button_Return,
					ViewOneTimePassword.button_Logout, ViewOneTimePassword.button_Quit);
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			ViewOneTimePassword.theRootPane.getChildren().addAll(
					ViewOneTimePassword.label_PageTitle, ViewOneTimePassword.label_UserDetails,
					ViewOneTimePassword.button_UpdateThisUser, ViewOneTimePassword.line_Separator1,
					ViewOneTimePassword.label_SelectUser,
					ViewOneTimePassword.combobox_SelectUser, 
					ViewOneTimePassword.button_SetOneTimePassword,
					ViewOneTimePassword.line_Separator4, 
					ViewOneTimePassword.button_Return,
					ViewOneTimePassword.button_Logout,
					ViewOneTimePassword.button_Quit);
		}
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewOneTimePassword.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewOneTimePassword.theStage.setScene(ViewOneTimePassword.theOneTimePasswordScene);
		ViewOneTimePassword.theStage.show();
	}
	
	
	/**********
	 * <p> Method: setupSelectedUser() </p>
	 * 
	 * <p> Description: This method fetches the current values for the widgets whose values change
	 * based on which user has been selected and any actions that the admin takes. </p>
	 * 
	 */
	private static void setupSelectedUser() {
		System.out.println("*** Entering setupSelectedUser");
		

		// Repaint the window showing this new values
		repaintTheWindow();

	}
	
	
	/**********
	 * <p> Method: performeGenerateOneTimePassword() </p>
	 * 
	 * <p> Description: This method generates and sets a new password to the specified user. </p>
	 * 
	 */
	protected static void performGenerateOneTimePassword() {
		String userName = ViewOneTimePassword.combobox_SelectUser.getValue();
		
		//If the user already had a reset issued, deny the new password.
		if (theDatabase.userNameHasBeenUsedForOTP(userName)) {
			ViewOneTimePassword.alertOneTimePasswordSet.setContentText(
					"A password reset has already been issued to this user.");
			ViewOneTimePassword.alertOneTimePasswordSet.showAndWait();
			return;
		}
		
		//Generate a password for the user
		String oneTimePassword = theDatabase.generateOneTimePassword(userName);
		
		//Display the generated password so the admin can copy it down and pass the message.
		String msg = "Code: " + oneTimePassword + " for user " + userName + 
				" was set";
		System.out.println(msg);
		ViewOneTimePassword.alertOneTimePasswordSet.setContentText(msg);
		ViewOneTimePassword.alertOneTimePasswordSet.showAndWait();
	}
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewOneTimePassword.theStage,
				ViewOneTimePassword.theUser);
	}
	
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}