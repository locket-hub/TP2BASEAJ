package guiAdminHome;

import database.Database;
import java.util.Date;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		long deadline = System.currentTimeMillis() + 86400000;
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole, deadline);
		Date date = new Date(deadline);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress + " with a deadline at " + date;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to set a one time password for a user. </p>
	 */
	protected static void setOnetimePassword () {
		guiOneTimePassword.ViewOneTimePassword.displayOneTimePassword(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {
		java.util.List<String> users = theDatabase.getUserList();
		// Remove the "<Select a User>" placeholder
		users.remove("<Select a User>");
		// Remove the current admin from the list, as they cannot delete themselves
		users.remove(ViewAdminHome.theUser.getUserName());

		if (users.isEmpty()) {
			ViewAdminHome.alertNotImplemented.setTitle("Delete User Issue");
			ViewAdminHome.alertNotImplemented.setHeaderText("No Users to Delete");
			ViewAdminHome.alertNotImplemented.setContentText("There are no other users in the system to delete.");
			ViewAdminHome.alertNotImplemented.showAndWait();
			return;
		}

		javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(users.get(0), users);
		dialog.setTitle("Delete User");
		dialog.setHeaderText("Select a user to delete:");
		dialog.setContentText("Choose a user:");

		java.util.Optional<String> result = dialog.showAndWait();

		result.ifPresent(userToDelete -> {
			theDatabase.deleteUser(userToDelete);

			ViewAdminHome.label_NumberOfUsers.setText("Number of users: " + theDatabase.getNumberOfUsers());

			ViewAdminHome.alertNotImplemented.setTitle("User Deleted");
			ViewAdminHome.alertNotImplemented.setHeaderText("Success");
			ViewAdminHome.alertNotImplemented.setContentText("The user '" + userToDelete + "' has been deleted.");
			ViewAdminHome.alertNotImplemented.showAndWait();
		});
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
		java.util.List<String> users = theDatabase.getUserList();
		String userList = "";
			
		if (!users.isEmpty()) {
			for (String individualUser : users) {
				if (!individualUser.contains("<Select a User>")) {
					userList += "Username: ";
					userList += individualUser + "\n";
					
						if(theDatabase.getFirstName(individualUser).isEmpty()) {
							userList += "  No First Name Set\n";
						} else {
							userList += "  First Name: " + theDatabase.getFirstName(individualUser) + "\n";
						}
						
						if(theDatabase.getMiddleName(individualUser).isEmpty()) {
							userList += "  No Middle Name Set\n";
						} else {
							userList += "  Middle Name: " + theDatabase.getMiddleName(individualUser) + "\n";
						}
						
						if(theDatabase.getLastName(individualUser).isEmpty()) {
							userList += "  No Last Name Set\n";
						} else {
							userList += "  Last Name: " + theDatabase.getLastName(individualUser) + "\n";
						}

						if(theDatabase.getEmailAddress(individualUser).isEmpty()) {
							userList += "  No Email Address Set\n";
						} else {
							userList += "  Email: " + theDatabase.getEmailAddress(individualUser) + "\n";
						}
						
						userList += "  Roles: \n";
						theDatabase.getUserAccountDetails(individualUser);
						if(theDatabase.getCurrentAdminRole()) {
							userList += "    Admin\n";
						} 
						if(theDatabase.getCurrentNewRole1()) {
							userList += "    Role 1\n";
						} 
						if(theDatabase.getCurrentNewRole2()) {
							userList += "    Role 2\n";
						}
						userList += "---------------------------------------------------" + "\n";
				}
			}
			
			ViewAdminHome.alert_ListUsers.setTitle("List All Users");
			ViewAdminHome.alert_ListUsers.setHeaderText("Current Users");
			ViewAdminHome.alert_ListUsers.setContentText(userList);
			ViewAdminHome.alert_ListUsers.showAndWait();
			
		} else {
			System.out.println("\n*** WARNING ***: There are no users!");
			ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
			ViewAdminHome.alertNotImplemented.setHeaderText("List User Issue");
			ViewAdminHome.alertNotImplemented.setContentText("There are no users!");
			ViewAdminHome.alertNotImplemented.showAndWait();
		}
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
