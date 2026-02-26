package leo.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import leo.task.Task;
import leo.task.TaskList;

/**
 * GuiUi handles GUI-specific user interface operations for the Leo chatbot.
 * It extends Ui to provide JavaFX-based dialog bubbles for displaying messages.
 *
 * <p>A-BetterGui: Asymmetric design with distinct styles for user, bot, errors, and undo.
 * Features:
 * - Profile pictures (small, circular) for user and bot
 * - Right-aligned user messages, left-aligned bot messages
 * - Distinct error styling to catch user attention
 * - Optimized for space with clean, minimal design</p>
 */
public class GuiUi extends Ui {
    private VBox dialogContainer;
    private static final int PROFILE_PICTURE_SIZE = 32;

    /**
     * Sets the dialog container for displaying messages.
     *
     * @param container the VBox to use as the dialog container
     */
    public void setDialogContainer(VBox container) {
        this.dialogContainer = container;
    }

    /**
     * Creates a profile picture region with initial.
     *
     * @param initial the letter to display
     * @param isUser true for user (blue), false for bot (green)
     * @return a Region containing the profile picture
     */
    private Region createProfilePictureRegion(String initial, boolean isUser) {
        Circle circle = new Circle(PROFILE_PICTURE_SIZE / 2);
        circle.setFill(isUser ? Color.valueOf("#007bff") : Color.valueOf("#4CAF50"));
        circle.setStroke(Color.valueOf("#e0e0e0"));
        circle.setStrokeWidth(1);

        Label initialLabel = new Label(initial);
        initialLabel.setTextFill(Color.WHITE);
        initialLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane(circle, initialLabel);
        stackPane.setPrefSize(PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE);
        stackPane.setMaxSize(PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE);
        stackPane.setMinSize(PROFILE_PICTURE_SIZE, PROFILE_PICTURE_SIZE);

        return stackPane;
    }

    /**
     * Creates a styled message bubble with profile picture.
     *
     * @param text the text to display
     * @param isUserMessage true if this is a user message, false for bot message
     * @return an HBox containing the complete message with profile picture
     */
    private HBox createMessageBubble(String text, boolean isUserMessage) {
        HBox container = new HBox();
        container.setSpacing(8);
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label messageLabel = new Label(text);
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add(isUserMessage ? "user-message" : "bot-message");
        messageLabel.setMaxWidth(300);
        messageLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

        VBox messageWrapper = new VBox(messageLabel);
        messageWrapper.getStyleClass().add("message-wrapper");
        HBox.setHgrow(messageWrapper, Priority.NEVER);

        if (isUserMessage) {
            // User message: profile picture on right, message aligned right
            container.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            Region profilePicture = createProfilePictureRegion("U", true);
            container.getChildren().addAll(messageWrapper, profilePicture);
        } else {
            // Bot message: profile picture on left, message aligned left
            Region profilePicture = createProfilePictureRegion("L", false);
            container.getChildren().addAll(profilePicture, messageWrapper);
        }

        return container;
    }

    /**
     * Creates a styled error message bubble.
     *
     * @param text the error text to display
     * @return an HBox containing the error message
     */
    private HBox createErrorBubble(String text) {
        HBox container = new HBox();
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        container.setMaxWidth(300);

        Label errorLabel = new Label(text);
        errorLabel.setWrapText(true);
        errorLabel.getStyleClass().add("error-message");
        errorLabel.setMaxWidth(300);
        errorLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

        container.getChildren().add(errorLabel);
        return container;
    }

    /**
     * Creates a styled undo message bubble.
     *
     * @param text the undo text to display
     * @return an HBox containing the undo message
     */
    private HBox createUndoBubble(String text) {
        HBox container = new HBox();
        container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        container.setMaxWidth(300);

        Label undoLabel = new Label(text);
        undoLabel.setWrapText(true);
        undoLabel.getStyleClass().add("undo-message");
        undoLabel.setMaxWidth(300);
        undoLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);

        container.getChildren().add(undoLabel);
        return container;
    }

    /**
     * Adds a message bubble to the dialog container.
     *
     * @param text the text to display
     * @param isUserMessage true if this is a user message, false for bot message
     */
    private void addMessageBubble(String text, boolean isUserMessage) {
        if (dialogContainer != null) {
            Platform.runLater(() -> {
                HBox messageBubble = createMessageBubble(text, isUserMessage);
                dialogContainer.getChildren().add(messageBubble);
            });
        }
    }

    /**
     * Adds a bot message bubble to the dialog.
     *
     * @param text the bot's message text
     */
    private void addBotMessage(String text) {
        addMessageBubble(text, false);
    }

    /**
     * Adds a user message bubble to the dialog.
     *
     * @param text the user's input text
     */
    public void addUserMessage(String text) {
        addMessageBubble(text, true);
    }

    /**
     * Adds an error message bubble with distinct styling.
     *
     * @param text the error message to display
     */
    private void addErrorMessage(String text) {
        if (dialogContainer != null) {
            Platform.runLater(() -> {
                HBox errorBubble = createErrorBubble(text);
                dialogContainer.getChildren().add(errorBubble);
            });
        }
    }

    /**
     * Adds an undo message bubble with success styling.
     *
     * @param text the undo message to display
     */
    private void addUndoMessage(String text) {
        if (dialogContainer != null) {
            Platform.runLater(() -> {
                HBox undoBubble = createUndoBubble(text);
                dialogContainer.getChildren().add(undoBubble);
            });
        }
    }

    @Override
    public void printGreeting() {
        addBotMessage("Hello! I'm Leo");
        addBotMessage("What can I do for you?");
    }

    @Override
    public void printGoodbye() {
        addBotMessage("Bye. Hope to see you again soon!");
    }

    @Override
    public void printList(java.util.ArrayList<Task> tasks) {
        addBotMessage("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            addBotMessage((i + 1) + "." + tasks.get(i));
        }
    }

    @Override
    public void printAddedTask(Task task, int taskCount) {
        addBotMessage("Got it. I've added this task:");
        addBotMessage("  " + task);
        addBotMessage("Now you have " + taskCount + " tasks in the list.");
    }

    @Override
    public void printDeletedTask(Task task, int taskCount) {
        addBotMessage("Noted. I've removed this task:");
        addBotMessage("  " + task);
        addBotMessage("Now you have " + taskCount + " tasks in the list.");
    }

    @Override
    public void printMarkedTask(Task task) {
        addBotMessage("Nice! I've marked this task as done:");
        addBotMessage("  " + task);
    }

    @Override
    public void printUnmarkedTask(Task task) {
        addBotMessage("OK, I've marked this task as not done yet:");
        addBotMessage("  " + task);
    }

    @Override
    public void printError(String message) {
        // A-BetterGui: Error messages use distinct red styling to catch attention
        String[] lines = message.split("\\R");
        for (String line : lines) {
            addErrorMessage(line);
        }
    }

    @Override
    public void printUndo(String message) {
        // A-BetterGui: Undo messages use green success styling
        addUndoMessage(message);
    }

    @Override
    public void printFindResults(String keyword, TaskList tasks, String searchKeyword) {
        addBotMessage("Here are the matching tasks in your list:");

        java.util.ArrayList<Task> allTasks = tasks.getAll();
        int matchCount = 0;
        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            if (task.getDescription().toLowerCase().contains(searchKeyword.toLowerCase())) {
                matchCount++;
                addBotMessage((matchCount) + "." + task);
            }
        }

        if (matchCount == 0) {
            addBotMessage("No tasks found containing \"" + keyword + "\".");
        }
    }
}
