package guiRole1;

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

public class ViewCreatePost {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    protected static Label label_PageTitle = new Label("Create New Post");
    protected static Label label_Thread = new Label("Thread:");
    protected static TextField textField_Thread = new TextField();
    protected static Label label_Content = new Label("Content:");
    protected static TextArea textArea_Content = new TextArea();
    protected static Button button_Submit = new Button("Submit Post");
    protected static Button button_Cancel = new Button("Cancel");
    protected static Label label_Status = new Label();
    
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

    private static Database theDatabase = applicationMain.FoundationsMain.database;
    private static Stage theStage;
    private static Pane theRootPane;
    private static User theUser;
    private static Scene theScene;

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

    private static void setupUI() {
        // Header
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        // Thread field
        setupLabelUI(label_Thread, "Arial", 16, 150, Pos.BASELINE_LEFT, 50, 120);
        
        textField_Thread.setLayoutX(50);
        textField_Thread.setLayoutY(150);
        textField_Thread.setPrefWidth(700);
        textField_Thread.setPromptText("Enter thread name (leave empty for 'General')");
        textField_Thread.setFont(Font.font("Arial", 14));

        // Content area
        setupLabelUI(label_Content, "Arial", 16, 150, Pos.BASELINE_LEFT, 50, 200);
        
        textArea_Content.setLayoutX(50);
        textArea_Content.setLayoutY(230);
        textArea_Content.setPrefWidth(700);
        textArea_Content.setPrefHeight(200);
        textArea_Content.setPromptText("Write your post content here...");
        textArea_Content.setFont(Font.font("Arial", 14));
        textArea_Content.setWrapText(true);

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
            label_PageTitle, line_Separator1, label_Thread, textField_Thread,
            label_Content, textArea_Content, button_Submit, button_Cancel, label_Status);
    }

    private static void submitPost() {
    	
        label_Status.setText("");

        String thread = textField_Thread.getText().trim();
        String content = textArea_Content.getText().trim();

        // Validation
        if (thread.isEmpty()) {
            thread = "General";
        }

        if (content.isEmpty()) {
            label_Status.setText("Error: Post content cannot be empty");
            label_Status.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }
        
        String[] words = content.trim().split("\\s+");
        
        if (words.length > 300) {
            label_Status.setText("Error: Post content cannot be more than 300 words");
            label_Status.setTextFill(javafx.scene.paint.Color.RED);
            return;
        }
        

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

    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
