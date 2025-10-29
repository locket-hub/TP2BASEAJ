package guiRole1;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ViewCreatePosts Class. </p>
 * 
 * <p> Description: The Java/FX-based ViewCreatePost page.  The page is for users who want to 
 * create a post with an optional thread selection. We decided a new view would be good for this because
 * it offers a nice separation of concerns.
 * </p>
 * 
 * @author Blake Ranniker (Documentation), Arjun Rajesh Korath (Code)
 */
public class ViewCreatePost {
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    //These are the widgets for the GUI.
    //This spot is the top where it has the title and thread selection
    protected static Label label_PageTitle = new Label("Create New Post");
    protected static Label label_Thread = new Label("Select a thread to post to:");
    protected static ComboBox <String> combobox_SelectThread = new ComboBox <String>();
    
    //This spot is where you fill in the content to post
    protected static Label label_Content = new Label("Content:");
    protected static TextArea textArea_Content = new TextArea();
    protected static Button button_Submit = new Button("Submit Post");
    protected static Button button_Cancel = new Button("Cancel");
    protected static Label label_Status = new Label();
    
    //Separator
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    //These are the attributes used to configure the page.
    private static Database theDatabase = applicationMain.FoundationsMain.database;
    private static Stage theStage;
    private static Pane theRootPane;
    private static User theUser;
    private static Scene theScene;

    /*-*******************************************************************************************

	Constructors
	
	 */


	/**********
	 * <p> Method: displaySearchPosts(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Create Post View to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
    public static void displayCreatePost(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        // Create new scene
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        setupUI();

        theStage.setTitle("CSE 360 Foundations: Create Post");
        theStage.setScene(theScene);
        theStage.show();
    }

    
    /**********
	 * <p> Method: setupUI() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the view editor methods.</p>
	 * 
	 */
    private static void setupUI() {
        // Header
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        // Thread field
        setupLabelUI(label_Thread, "Arial", 18, width, Pos.BASELINE_LEFT, 150, 100);
        setupComboBoxUI(combobox_SelectThread, "Dialog", 16, 150, 150, 140);
        List<String> userList = new ArrayList<String>();
        userList.add("General");
        combobox_SelectThread.setItems(FXCollections.observableArrayList(userList));
		combobox_SelectThread.getSelectionModel().select(0);
		combobox_SelectThread.getSelectionModel().selectedItemProperty();

        // Content area
        setupLabelUI(label_Content, "Arial", 16, 150, Pos.BASELINE_LEFT, 50, 200);
        setupTextAreaUI(textArea_Content, 50, 230, 700, 200, "Write your post content here...", "Arial", true);
        

        // Buttons
        setupButtonUI(button_Submit, "Dialog", 18, 150, Pos.CENTER, 200, 450);
        button_Submit.setOnAction((event) -> {
            submitPost();
            ViewRole1Home.displayRole1Home(theStage, theUser);
        });

        setupButtonUI(button_Cancel, "Dialog", 18, 150, Pos.CENTER, 450, 450);
        button_Cancel.setOnAction((event) -> {
            ViewRole1Home.displayRole1Home(theStage, theUser);
        });

        // Status label
        setupLabelUI(label_Status, "Arial", 14, 700, Pos.CENTER, 50, 500);

        // Add all to root pane
        theRootPane.getChildren().addAll(
            label_PageTitle, line_Separator1, label_Thread, combobox_SelectThread,
            label_Content, textArea_Content, button_Submit, button_Cancel, label_Status);
    }

    /**********
   	 * <p> Method: submitPost() </p>
   	 * 
   	 * <p> Description: This method creates a post to send to the database. It takes the values from the thread and content
   	 * areas to construct the post, as well as the user information. It also handles input validation for content.
   	 * </p> 
   	 */
    private static void submitPost() {
    	//Getting the values from the View widgets
        label_Status.setText("");

        String thread = combobox_SelectThread.getValue();
        String content = textArea_Content.getText().trim();

        // Validation
        if (thread.isEmpty()) {
            thread = "General";
        }

        //Validating that the content string isn't empty
        if (content.isEmpty()) {
            label_Status.setText("Error: Post content cannot be empty");
            label_Status.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }
        
        //Breaking up the content into words to check word count
        String[] words = content.trim().split("\\s+");
        
        //Validating the word count
        if (words.length > 300) {
            label_Status.setText("Error: Post content cannot be more than 300 words");
            label_Status.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }
        

        //Trying to create a post in the Database
        try {
            int userID = theDatabase.getUserID(theUser.getUserName());
            
            // Use the new Database method instead of direct connection access
            theDatabase.insertPost(thread, userID, content);
            label_Status.setText("");
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Post created successfully!");
            alert.showAndWait();

            // Go back to home
            ViewRole1Home.displayRole1Home(theStage, theUser);

        } catch (SQLException e) {
            e.printStackTrace();
            label_Status.setText("Error: Failed to create post - " + e.getMessage());
            label_Status.setTextFill(javafx.scene.paint.Color.RED);
        }
    }

    
    /*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
    
    /**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	protected static void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
	
	/**********
	 * Private local method to initialize the standard fields for a TextArea
	 * 
	 * @param t		The TextArea object to be initialized
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param w		The width of the TextArea
	 * @param h		The height of the TextArea
	 * @param content  The content to be displayed in the TextArea
	 * @param ff	The font to be used
	 * @param wrap  The setWrapText() value
	 */
	protected static void setupTextAreaUI(TextArea t, int x, int y, int w, int h, String content, String ff, boolean wrap) {
		t.setLayoutX(x);
        t.setLayoutY(y);
        t.setPrefWidth(w);
        t.setPrefHeight(h);
        t.setPromptText(content);
        t.setFont(Font.font(ff, 14));
        t.setWrapText(wrap);
	}
	
}
