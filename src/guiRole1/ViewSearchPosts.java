package guiRole1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import database.Database;
import entityClasses.Post;
import entityClasses.User;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/*******
 * <p> Title: ViewSearchPosts Class. </p>
 * 
 * <p> Description: The Java/FX-based ViewSearchPosts page.  The page is for users who want to 
 * search for posts with a keyword that they select. We decided a new view would be good for this because
 * it offers a nice separation of concerns.
 * </p>
 * 
 * @author of the JavaDoc: Blake Ranniker
 */

public class ViewSearchPosts {

	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    //These are the widgets for the GUI. There are two main areas and the back to home button on the top.
    protected static Button button_Back = new Button("Back to Home");
    
    //This is the top area in between the two lines. This is where users enter search keywords and press search.
    protected static Label label_PageTitle = new Label("Search Posts");
    protected static Label label_SearchPrompt = new Label("Enter search keyword:");
    protected static TextField textField_Search = new TextField();
    protected static Button button_Search = new Button("Search");
    protected static Label label_Results = new Label("Enter a keyword to search across all posts");
    
    //This is the bottom area in the GUI. This is where the results show up when they are received.
    protected static ScrollPane scrollPane_Results = new ScrollPane();
    protected static VBox vbox_Results = new VBox(10);
    
    //Line separators to keep GUi areas separate.
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
    protected static Line line_Separator2 = new Line(20, 170, width-20, 170);
    //This is the end of GUI objects for the page.

    // These attributes are used to configure the page and populate it with this user's information
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
	 * the Search Posts View to be displayed.
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
    public static void displaySearchPosts(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        // Create new scene
        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        setupUI();

        theStage.setTitle("CSE 360 Foundations: Search Posts");
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

        // Back button
        setupButtonUI(button_Back, "Dialog", 18, 150, Pos.CENTER, 20, 55);
        button_Back.setOnAction((event) -> {
            ViewRole1Home.displayRole1Home(theStage, theUser);
        });

        // Search controls
        setupLabelUI(label_SearchPrompt, "Arial", 16, 200, Pos.BASELINE_LEFT, 50, 120);
        
        textField_Search.setLayoutX(250);
        textField_Search.setLayoutY(118);
        textField_Search.setPrefWidth(400);
        textField_Search.setPromptText("Search in post content across all threads...");
        textField_Search.setFont(Font.font("Arial", 14));
        
        // Search on Enter key press
        textField_Search.setOnAction((event) -> {
            performSearch();
        });

        setupButtonUI(button_Search, "Dialog", 16, 100, Pos.CENTER, 670, 115);
        button_Search.setOnAction((event) -> {
            performSearch();
        });

        // Results label
        setupLabelUI(label_Results, "Arial", 16, width - 100, Pos.CENTER, 50, 185);
        label_Results.setStyle("-fx-font-weight: bold;");

        // Results area
        vbox_Results.setPadding(new Insets(15));
        vbox_Results.setStyle("-fx-background-color: white;");
        
        scrollPane_Results.setContent(vbox_Results);
        scrollPane_Results.setLayoutX(20);
        scrollPane_Results.setLayoutY(220);
        scrollPane_Results.setPrefWidth(width - 40);
        scrollPane_Results.setPrefHeight(300);
        scrollPane_Results.setFitToWidth(true);
        scrollPane_Results.setStyle("-fx-background-color: #f5f5f5;");

        // Add all to root pane
        theRootPane.getChildren().addAll(
            label_PageTitle, button_Back, line_Separator1, label_SearchPrompt,
            textField_Search, button_Search, line_Separator2, label_Results, scrollPane_Results);
    }

    /**********
	 * <p> Method: performSearch() </p>
	 * 
	 * <p> Description: This method performs a search given the keyword from the text box
	 * It queries the database for a list of posts matching the keyword, then displays them.
	 * </p> 
	 */
    private static void performSearch() {
        String keyword = textField_Search.getText().trim();
        
        //If there is no keyword, display a warning
        if (keyword.isEmpty()) {
            label_Results.setText("Please enter a search keyword");
            label_Results.setTextFill(Color.RED);
            vbox_Results.getChildren().clear();
            return;
        }

        try {
            // Search across all threads
            List<Post> results = theDatabase.searchPosts(keyword);

            // Display results
            vbox_Results.getChildren().clear();
            
            if (results.isEmpty()) {
                label_Results.setText("No results found for: \"" + keyword + "\"");
                label_Results.setTextFill(Color.GRAY);
                
                Label noResults = new Label("No posts contain this keyword. Try different keywords.");
                noResults.setFont(Font.font("Arial", 14));
                noResults.setTextFill(Color.GRAY);
                noResults.setPadding(new Insets(20));
                vbox_Results.getChildren().add(noResults);
            } else {
                label_Results.setText("Found " + results.size() + " post(s) containing \"" + keyword + "\" across all threads");
                label_Results.setTextFill(Color.DARKGREEN);
                
                int userID = theDatabase.getUserID(theUser.getUserName());
                
                for (Post post : results) {
                    VBox postBox = createSearchResultItem(post, userID);
                    vbox_Results.getChildren().add(postBox);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            label_Results.setText("Error performing search");
            label_Results.setTextFill(Color.RED);
            
            Label errorLabel = new Label("Error: " + e.getMessage());
            errorLabel.setTextFill(Color.RED);
            vbox_Results.getChildren().clear();
            vbox_Results.getChildren().add(errorLabel);
        }
    }

    /**********
	 * <p> Method: createSearchBoxResultItem() </p>
	 * 
	 * <p> Description: This method creates a new box for a post to be displayed in..</p>
	 * 
	 *  @param post is the post to be displayed
	 *  @param currentUserID is the userID of the person performing the search.
	 */
    private static VBox createSearchResultItem(Post post, int currentUserID) {
        //Create and set up the GUI box.
    	VBox postBox = new VBox(8);
        postBox.setPadding(new Insets(15));
        postBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; " +
                         "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        postBox.setCursor(javafx.scene.Cursor.HAND);

        // Make the entire box clickable
        postBox.setOnMouseClicked(event -> {
            ViewPostDetail.displayPostDetail(theStage, theUser, post.getPostID());
        });

        // Header with thread and metadata
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label threadLabel = new Label("[" + post.getThread() + "]");
        threadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        threadLabel.setTextFill(Color.BLUE);

        String authorName = theDatabase.getUsernameByID(post.getUserID());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        Label metaLabel = new Label("by " + authorName + " - " + sdf.format(post.getTimestamp()));
        metaLabel.setFont(Font.font("Arial", 12));
        metaLabel.setTextFill(Color.GRAY);

        // Read/Unread indicator
        boolean isRead = theDatabase.isPostReadByUser(currentUserID, post.getPostID());
        Label readIndicator = new Label(isRead ? "✓ Read" : "● Unread");
        readIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        readIndicator.setTextFill(isRead ? Color.GREEN : Color.ORANGE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(threadLabel, metaLabel, spacer, readIndicator);

        // Post content preview
        String content = post.getPostContent();
        
        // Truncate if too long
        if (content.length() > 200) {
            content = content.substring(0, 197) + "...";
        }
        
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Arial", 13));
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: #333333;");

        // Click to view indicator
        Label clickLabel = new Label("Click to view full post and replies →");
        clickLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        clickLabel.setTextFill(Color.DARKBLUE);

        postBox.getChildren().addAll(headerBox, contentLabel, clickLabel);
        
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
}
