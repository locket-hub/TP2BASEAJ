package guiPasswordReset;

import database.Database;
import guiUserLogin.ViewUserLogin;
import passwordEvaluation.PasswordEvaluation;

public class ControllerPasswordReset {
	/*-********************************************************************************************

	The controller attributes for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String password1 = "";
	private static String password2 = "";		
	protected static Database theDatabase = applicationMain.FoundationsMain.database;		

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	*/
	
	
	/**********
	 * <p> Method: setPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setPassword1() {
		password1 = ViewPasswordReset.text_Password1.getText();
		ViewPasswordReset.label_PasswordsDoNotMatch.setText("");
	}
	
	
	/**********
	 * <p> Method: setPassword2() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 2 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setPassword2() {
		password2 = ViewPasswordReset.text_Password2.getText();
		ViewPasswordReset.label_PasswordsDoNotMatch.setText("");
	}
	
	
	/**********
	 * <p> Method: setNewPassword() </p>
	 * 
	 * <p> Description: This method is called when the user presses the button to set up the new
	 * password.  </p>
	 * 
	 */
	protected static void setNewPassword() {
		
		// Make sure the two passwords are the same
		if (password1.compareTo(password2) == 0) {
			if(!PasswordEvaluation.evaluatePassword(password1).equals("")) {
				ViewPasswordReset.alertPasswordError.setTitle("Password Error");
				ViewPasswordReset.alertPasswordError.setHeaderText("Invalid Password");
				ViewPasswordReset.alertPasswordError.setContentText(PasswordEvaluation.evaluatePassword(password1));
				ViewPasswordReset.alertPasswordError.showAndWait();
				
				ViewPasswordReset.text_Password1.setText("");
				ViewPasswordReset.text_Password2.setText("");
				
				return;
			}
			
			theDatabase.updatePassword(ViewPasswordReset.text_Username.getText(), password1);
			theDatabase.removeOTPAfterUse(ViewPasswordReset.text_Username.getText());
			ViewUserLogin.displayUserLogin(ViewPasswordReset.theStage);
		}
		
		else {
			// The two passwords are NOT the same, so clear the passwords, explain the passwords
			// must be the same, and clear the message as soon as the first character is typed.
			ViewPasswordReset.text_Password1.setText("");
			ViewPasswordReset.text_Password2.setText("");
			ViewPasswordReset.label_PasswordsDoNotMatch.setText(
					"The two passwords must match. Please try again!");
		}
		
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
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}

