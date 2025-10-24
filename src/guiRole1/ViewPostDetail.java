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
import entityClasses.Reply;
import entityClasses.User;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewPostDetail {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    protected static Label label_PageTitle = new Label("Post Details");
    protected static Button button_Back = new Button("Back to Home");
    protected static Button button_MarkRead = new Button("Mark as Read");
    protected static Button button_AddReply = new Button("Add Reply");
    protected static Button button_DeletePost = new Button("Delete Post"); 
    protected static Button button_EditPost = new Button("Edit Post");

    
    protected static ScrollPane scrollPane_Content = new ScrollPane();
    protected static VBox vbox_Content = new VBox(15);
    
    protected static Line line_Separator1 = new Line(20, 95, width-20, 95);
    protected static Line line_Separator2 = new Line(20, 525, width-20, 525);

    private static Database theDatabase = applicationMain.FoundationsMain.database;
    private static Stage theStage;
    private static Pane theRootPane;
    private static User theUser;
    private static Scene theScene;
    private static Post currentPost;
    private static int currentPostID;

    public static void displayPostDetail(Stage ps, User user, int postID) {
        theStage = ps;
        theUser = user;
        currentPostID = postID;

        theRootPane = new Pane();
        theScene = new Scene(theRootPane, width, height);

        setupUI();
        loadPostAndReplies();

        theStage.setTitle("CSE 360 Foundations: Post Details");
        theStage.setScene(theScene);
        theStage.show();
    }

    private static void setupUI() {
        // Header
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        // Buttons
        setupButtonUI(button_Back, "Dialog", 18, 150, Pos.CENTER, 20, 55);
        button_Back.setOnAction((event) -> {
            ViewRole1Home.displayRole1Home(theStage, theUser);
        });

        setupButtonUI(button_MarkRead, "Dialog", 18, 110, Pos.CENTER, 180, 55);
        button_MarkRead.setOnAction((event) -> {
            try {
                int userID = theDatabase.getUserID(theUser.getUserName());
                theDatabase.markPostAsRead(userID, currentPostID);
                button_MarkRead.setDisable(true);
                button_MarkRead.setText("Marked as Read");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        setupButtonUI(button_AddReply, "Dialog", 18, 110, Pos.CENTER, 320, 55);
        button_AddReply.setOnAction((event) -> {
            showReplyDialog();
        });
        
        setupButtonUI(button_DeletePost, "Dialog", 18, 110, Pos.CENTER, 470, 55);
        button_DeletePost.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
        button_DeletePost.setVisible(false);  // Hidden by default
        button_DeletePost.setOnAction((event) -> {
            showDeleteConfirmation();
        });
        
        setupButtonUI(button_EditPost, "Dialog", 18, 110, Pos.CENTER, 620, 55);
        button_EditPost.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white;");
        button_EditPost.setVisible(false);  // Hidden by default
        button_EditPost.setOnAction((event) -> {
            showEditPostDialog();
        });

        // Content area
        vbox_Content.setPadding(new Insets(20));
        vbox_Content.setStyle("-fx-background-color: white;");
        
        scrollPane_Content.setContent(vbox_Content);
        scrollPane_Content.setLayoutX(20);
        scrollPane_Content.setLayoutY(110);
        scrollPane_Content.setPrefWidth(width - 40);
        scrollPane_Content.setPrefHeight(400);
        scrollPane_Content.setFitToWidth(true);

        // Add all to root pane
        theRootPane.getChildren().addAll(
            label_PageTitle, button_Back, button_MarkRead, button_AddReply,
            line_Separator1, scrollPane_Content, line_Separator2, button_DeletePost, button_EditPost);
    }

    private static void loadPostAndReplies() {
        vbox_Content.getChildren().clear();

        try {
            // Get post from database
            currentPost = getPostByID(currentPostID);
            if (currentPost == null) {
                Label errorLabel = new Label("Post not found.");
                errorLabel.setFont(Font.font("Arial", 16));
                vbox_Content.getChildren().add(errorLabel);
                return;
            }

            int userID = theDatabase.getUserID(theUser.getUserName());
            
            
            if (currentPost.getUserID() == userID && !currentPost.isDeleted()) {
                button_DeletePost.setVisible(true);
                button_EditPost.setVisible(true);  
            } else {
                button_DeletePost.setVisible(false);
                button_EditPost.setVisible(false);  
            }
                
            boolean isRead = theDatabase.isPostReadByUser(userID, currentPostID);
            
            // Update mark read button state
            if (isRead) {
                button_MarkRead.setDisable(true);
                button_MarkRead.setText("Already Read");
            } else {
                button_MarkRead.setDisable(false);
                button_MarkRead.setText("Mark as Read");
            }

            // Display post
            VBox postBox = createPostDisplay(currentPost);
            vbox_Content.getChildren().add(postBox);

            // Add separator
            Separator separator = new Separator();
            separator.setPadding(new Insets(10, 0, 10, 0));
            vbox_Content.getChildren().add(separator);

            // Display replies
            Label repliesTitle = new Label("Replies");
            repliesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            vbox_Content.getChildren().add(repliesTitle);

            List<Reply> replies = theDatabase.getRepliesByPostID(currentPostID);
            if (replies.isEmpty()) {
                Label noReplies = new Label("No replies yet. Be the first to reply!");
                noReplies.setFont(Font.font("Arial", 14));
                noReplies.setTextFill(Color.GRAY);
                noReplies.setPadding(new Insets(10, 0, 0, 0));
                vbox_Content.getChildren().add(noReplies);
            } else {
                for (Reply reply : replies) {
                    VBox replyBox = createReplyDisplay(reply);
                    vbox_Content.getChildren().add(replyBox);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading post: " + e.getMessage());
            errorLabel.setTextFill(Color.RED);
            vbox_Content.getChildren().add(errorLabel);
        }
    }

    private static Post getPostByID(int postID) throws SQLException {
        return theDatabase.getPostByID(postID);

    }
    
    private static void showDeleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Post");
        alert.setHeaderText("Are you sure you want to delete this post?");
        alert.setContentText("This action cannot be undone. The post content will be replaced with a deletion message, but replies will remain.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    theDatabase.softDeletePost(currentPostID);
                    
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Post Deleted");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Post has been deleted successfully.");
                    successAlert.showAndWait();
                    
                    // Go back to home
                    ViewRole1Home.displayRole1Home(theStage, theUser);
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to delete post");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private static VBox createPostDisplay(Post post) {
        VBox postBox = new VBox(10);
        postBox.setPadding(new Insets(15));
        postBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; " +
                         "-fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Thread and metadata
        Label threadLabel = new Label("Thread: " + post.getThread());
        threadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        threadLabel.setTextFill(Color.BLUE);

        String authorName = theDatabase.getUsernameByID(post.getUserID());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm");
        Label metaLabel = new Label("Posted by " + authorName + " on " + 
                                    sdf.format(post.getTimestamp()));
        metaLabel.setFont(Font.font("Arial", 12));
        metaLabel.setTextFill(Color.GRAY);

        // Content
        Label contentLabel = new Label(post.getPostContent());
        contentLabel.setFont(Font.font("Arial", 14));
        contentLabel.setWrapText(true);
        contentLabel.setPadding(new Insets(10, 0, 0, 0));

        postBox.getChildren().addAll(threadLabel, metaLabel, contentLabel);
        return postBox;
    }
    
    private static void showReplyDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Reply");
        dialog.setHeaderText("Write your reply:");

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter your reply here...");
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(40);
        textArea.setWrapText(true);

        VBox content = new VBox(10);
        content.getChildren().add(textArea);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return textArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(replyText -> {
            if (!replyText.trim().isEmpty()) {
                try {
                    int userID = theDatabase.getUserID(theUser.getUserName());
                    addReplyToDatabase(currentPostID, userID, replyText);
                    loadPostAndReplies(); 
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to add reply");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }
    
    private static void showEditReplyDialog(Reply reply) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Reply");
        dialog.setHeaderText("Edit your reply:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        textArea.setText(reply.getReplyContent());  // Pre-fill with current content
        textArea.setPrefRowCount(5);
        textArea.setPrefColumnCount(40);
        textArea.setWrapText(true);

        VBox content = new VBox(10);
        content.getChildren().add(textArea);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return textArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newContent -> {
            if (!newContent.trim().isEmpty()) {
                try {
                    theDatabase.updateReplyContent(reply.getReplyID(), newContent.trim());
                    
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Reply Updated");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Reply has been updated successfully.");
                    successAlert.showAndWait();
                    
                    loadPostAndReplies(); // Refresh the display
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to update reply");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            } else {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Invalid Content");
                warningAlert.setHeaderText("Reply content cannot be empty");
                warningAlert.setContentText("Please enter some content for your reply.");
                warningAlert.showAndWait();
            }
        });
    }


    private static VBox createReplyDisplay(Reply reply) {
        VBox replyBox = new VBox(5);
        replyBox.setPadding(new Insets(10, 10, 10, 20));
        replyBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; " +
                          "-fx-border-width: 0 0 0 3; -fx-border-radius: 0;");

        String replyAuthor = theDatabase.getUsernameByID(reply.getUserID());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");
        
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label authorLabel = new Label(replyAuthor + " - " + sdf.format(reply.getTimestamp()));
        authorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        authorLabel.setTextFill(Color.DARKBLUE);
        
        try {
            int currentUserID = theDatabase.getUserID(theUser.getUserName());
            if (reply.getUserID() == currentUserID) {
                Button editReplyButton = new Button("Edit");
                editReplyButton.setOnAction(e -> showEditReplyDialog(reply));
                
                Button deleteReplyButton = new Button("Delete");
                deleteReplyButton.setOnAction(e -> showDeleteReplyConfirmation(reply));

                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                headerBox.getChildren().addAll(authorLabel, spacer, editReplyButton, deleteReplyButton);
            } else {
                headerBox.getChildren().add(authorLabel);
            }
        } catch (Exception e) {
            headerBox.getChildren().add(authorLabel);
        }
        
        Label contentLabel = new Label(reply.getReplyContent());
        contentLabel.setFont(Font.font("Arial", 13));
        contentLabel.setWrapText(true);

        replyBox.getChildren().addAll(headerBox, authorLabel, contentLabel);
        return replyBox;
    }

   
    
    private static void showEditPostDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Post");
        dialog.setHeaderText("Edit your post content:");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        textArea.setText(currentPost.getPostContent());  // Pre-fill with current content
        textArea.setPrefRowCount(8);
        textArea.setPrefColumnCount(50);
        textArea.setWrapText(true);

        VBox content = new VBox(10);
        content.getChildren().add(textArea);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return textArea.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newContent -> {
            if (!newContent.trim().isEmpty()) {
                try {
                    theDatabase.updatePostContent(currentPostID, newContent.trim());
                    
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Post Updated");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Post has been updated successfully.");
                    successAlert.showAndWait();
                    
                    loadPostAndReplies(); // Refresh the display
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to update post");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            } else {
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Invalid Content");
                warningAlert.setHeaderText("Post content cannot be empty");
                warningAlert.setContentText("Please enter some content for your post.");
                warningAlert.showAndWait();
            }
        });
    }

    /**
     * Displays a confirmation dialog for deleting a reply owned by the current user.
     * If confirmed, permanently removes the reply from the database and refreshes
     * the replies display.
     *
     * @param reply the Reply object to be deleted
     */
    private static void showDeleteReplyConfirmation(Reply reply) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Reply");
        alert.setHeaderText("Are you sure you want to delete this reply?");
        alert.setContentText("This action cannot be undone. The reply will be permanently removed.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    theDatabase.deleteReply(reply.getReplyID());
                    
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Reply Deleted");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Reply has been deleted successfully.");
                    successAlert.showAndWait();
                    
                    loadPostAndReplies(); // Refresh display
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to delete reply");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private static void addReplyToDatabase(int postID, int userID, String content) throws SQLException {
    	theDatabase.insertReply(postID, userID, content);
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
