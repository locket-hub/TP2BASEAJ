package guiRole1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import database.Database;
import entityClasses.Post;
import entityClasses.User;
import guiUserUpdate.ViewUserUpdate;


/*******
 * <p> Title: GUIReviewerHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Role1 Home Page.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Blake Ranniker (Documentation), Arjun Rajesh Korath (Code)
 */

public class ViewRole1Home {
	
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	protected static Button button_CreatePost = new Button("Create Post");
	protected static Button button_Search = new Button("Search");


	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
	
	protected static Line vert_line_seperator = new Line(200, 95, 200, 525);

	// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
	// would contain the widgets needed for the user to play the assigned role.
	protected static Label label_ThreadsTitle = new Label("Threads");
    protected static ListView<String> listView_Threads = new ListView<>();
    protected static ScrollPane scrollPane_Threads = new ScrollPane();

    protected static RadioButton radio_AllPosts = new RadioButton("All Posts");
    protected static RadioButton radio_ReadPosts = new RadioButton("Read Posts");
    protected static RadioButton radio_UnreadPosts = new RadioButton("Unread Posts");
    protected static RadioButton radio_MyPosts = new RadioButton("My Posts");
    protected static ToggleGroup toggleGroup_PostFilter = new ToggleGroup();
    protected static ListView<HBox> listView_Posts = new ListView<>();
    protected static ScrollPane scrollPane_Posts = new ScrollPane();

    // Separators
	
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewRole1Home theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	

	private static Scene theViewRole1HomeScene;	// The shared Scene each invocation populates
	protected static final int theRole = 2;		// Admin: 1; Role1: 2; Role2: 3
	
	
	 private static String selectedThread = null;
	 private static String selectedFilter = "All";

	/*-*******************************************************************************************

	Constructors
	
	 */


	/**********
	 * <p> Method: displayRole1Home(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role1 Home page to be displayed.
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
	public static void displayRole1Home(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewRole1Home();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		
		loadThreads();
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Student Home Page");
		theStage.setScene(theViewRole1HomeScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: ViewRole1Home() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayRole2Home method.</p>
	 * 
	 */
	private ViewRole1Home() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theViewRole1HomeScene = new Scene(theRootPane, width, height);	// Create the scene
		
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Student Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 18, width, Pos.BASELINE_LEFT, 20, 65);
		

		
		setupButtonUI(button_Search, "Dialog", 18, 170, Pos.CENTER, 420, 55);
		button_Search.setOnAction((event) -> {
			ViewSearchPosts.displaySearchPosts(theStage, theUser);
            
        });

		
		
		setupButtonUI(button_CreatePost, "Dialog", 18, 170, Pos.CENTER, 600, 55);
		button_CreatePost.setOnAction((event) -> {
			ViewCreatePost.displayCreatePost(theStage, theUser);
        });
		
		
		
		
		// GUI Area 2 - Left: Thread List
        setupLabelUI(label_ThreadsTitle, "Arial", 18, 150, Pos.CENTER, 25, 110);
        label_ThreadsTitle.setStyle("-fx-font-weight: bold;");

        listView_Threads.setLayoutX(20);
        listView_Threads.setLayoutY(145);
        listView_Threads.setPrefWidth(170);
        listView_Threads.setPrefHeight(370);
        
        // Handle thread selection
        listView_Threads.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedThread = newValue;
                loadPosts();
            }
        });
        
     // GUI Area 2 - Right: Filter Radio Buttons
        radio_AllPosts.setToggleGroup(toggleGroup_PostFilter);
        radio_AllPosts.setSelected(true);
        radio_AllPosts.setLayoutX(220);
        radio_AllPosts.setLayoutY(110);
        radio_AllPosts.setFont(Font.font("Arial", 14));
        
        radio_ReadPosts.setToggleGroup(toggleGroup_PostFilter);
        radio_ReadPosts.setLayoutX(330);
        radio_ReadPosts.setLayoutY(110);
        radio_ReadPosts.setFont(Font.font("Arial", 14));
        
        radio_UnreadPosts.setToggleGroup(toggleGroup_PostFilter);
        radio_UnreadPosts.setLayoutX(460);
        radio_UnreadPosts.setLayoutY(110);
        radio_UnreadPosts.setFont(Font.font("Arial", 14));
        
        radio_MyPosts.setToggleGroup(toggleGroup_PostFilter);
        radio_MyPosts.setLayoutX(600);
        radio_MyPosts.setLayoutY(110);
        radio_MyPosts.setFont(Font.font("Arial", 14));

        // Add listener for filter changes
        toggleGroup_PostFilter.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == radio_AllPosts) {
                selectedFilter = "All";
            } else if (newValue == radio_ReadPosts) {
                selectedFilter = "Read";
            } else if (newValue == radio_UnreadPosts) {
                selectedFilter = "Unread";
            } else if (newValue == radio_MyPosts) {
                selectedFilter = "MyPosts";
            }
            loadPosts();
        });
        
        // GUI Area 2 - Right: Posts List
        listView_Posts.setLayoutX(220);
        listView_Posts.setLayoutY(145);
        listView_Posts.setPrefWidth(width - 240);
        listView_Posts.setPrefHeight(370);
        
        // Handle post selection - navigate to post detail page
        listView_Posts.setOnMouseClicked(event -> {
            HBox selectedItem = listView_Posts.getSelectionModel().getSelectedItem();
            if (selectedItem != null && event.getClickCount() == 1) {
                // Get the post ID from the HBox's userData
                Integer postID = (Integer) selectedItem.getUserData();
                if (postID != null) {
                    ViewPostDetail.displayPostDetail(theStage, theUser, postID);
                }
            }
        });
		
		
		
		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 200, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((event) -> {ControllerRole1Home.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 200, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerRole1Home.performQuit(); });
        
        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 200, Pos.CENTER, 580, 540);
		button_UpdateThisUser.setOnAction((event) ->
			{ViewUserUpdate.displayUserUpdate(theStage, theUser); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
         theRootPane.getChildren().addAll(
        		 label_PageTitle, label_UserDetails, button_UpdateThisUser, button_CreatePost,
        		 vert_line_seperator, line_Separator1, button_Search, line_Separator4,
                 button_Logout, button_Quit, label_ThreadsTitle, listView_Threads,
                 radio_AllPosts, radio_ReadPosts, radio_UnreadPosts, radio_MyPosts, listView_Posts);
	}
	
	/**
	 * <p>Method: loadPosts()</p>
	 * 
	 * <p>Description: This method loads the posts that the user is going to see in the page. It checks for filters and
	 * displays posts based on those filters.
	 * </p>
	 */
    private static void loadPosts() {
        listView_Posts.getItems().clear();
        
        if (selectedThread == null) return;
        
        try {
            int userID = theDatabase.getUserID(theUser.getUserName());
            List<Post> posts;

            // Apply filter
            if ("Unread".equals(selectedFilter)) {
                posts = theDatabase.getUnreadPostsByThread(userID, selectedThread);
            } else if ("Read".equals(selectedFilter)) {
                posts = theDatabase.getReadPostsByThread(userID, selectedThread);
            } else if ("MyPosts".equals(selectedFilter)) {
                posts = theDatabase.getPostsByUser(userID, selectedThread);  // NEW
            } else {
                posts = theDatabase.getAllPostsByThread(selectedThread);
            }

            // Display posts
            for (Post post : posts) {
                HBox postItem = createPostListItem(post, userID);
                listView_Posts.getItems().add(postItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load posts");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
	
    /**
	 * <p>Method: loadThreads()</p>
	 * 
	 * <p>Description: This method loads the threads that the user is going to see in the page on the left hand side.
	 * As of right now, it should just be "General."
	 * </p>
	 */
    private static void loadThreads() {
        listView_Threads.getItems().clear();
        List<String> threads = theDatabase.getAllThreads();
        listView_Threads.getItems().addAll(threads);
        
        // Auto-select first thread if available
        if (!threads.isEmpty()) {
            listView_Threads.getSelectionModel().selectFirst();
            selectedThread = threads.get(0);
            loadPosts();
        }
	
    }
	
    /**
	 * <p>Method: createPostListItem()</p>
	 * 
	 * <p>Description: This method takes a post and a user ID and creates an HBox with the post content.
	 * It checks the DB to see if the post is read by the current user.
	 * </p>
	 * 
	 * @param post is the post that is being added to the list.
	 * @param currentUserID is the ID of the current user.
	 * 
	 * @return HBox to be added to the list of posts.
	 */
    private static HBox createPostListItem(Post post, int currentUserID) {
        HBox postBox = new HBox(10);
        postBox.setPadding(new Insets(10));
        postBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; " +
                         "-fx-border-width: 0 0 1 0;");
        postBox.setUserData(post.getPostID()); // Store post ID for click handling

        // Read/Unread indicator
        boolean isRead = theDatabase.isPostReadByUser(currentUserID, post.getPostID());
        Label readIndicator = new Label();
        readIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        readIndicator.setTextFill(isRead ? javafx.scene.paint.Color.GREEN : 
                                           javafx.scene.paint.Color.ORANGE);
        readIndicator.setMinWidth(25);

        // Post info
        VBox postInfo = new VBox(5);
        
        String authorName = theDatabase.getUsernameByID(post.getUserID());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        Label titleLabel = new Label("Post by " + authorName + " - " + 
                                     sdf.format(post.getTimestamp()));
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        // Truncate content if too long
        String content = post.getPostContent();
        if (content.length() > 100) {
            content = content.substring(0, 97) + "...";
        }
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Arial", 11));
        contentLabel.setWrapText(true);
        
        int replyCount = 0;
        try {
            replyCount = theDatabase.getReplyCountByPostID(post.getPostID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Label replyCountLabel = new Label(replyCount + " replies");
        replyCountLabel.setFont(Font.font("Arial", 10));
        replyCountLabel.setTextFill(javafx.scene.paint.Color.GRAY);
        
        postInfo.getChildren().addAll(titleLabel, contentLabel, replyCountLabel);
        HBox.setHgrow(postInfo, Priority.ALWAYS);

        postBox.getChildren().addAll(readIndicator, postInfo);
        
        return postBox;
    }
    
    /*-********************************************************************************************

	Helper methods to reduce code length

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
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
			double y){
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
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
   
}
