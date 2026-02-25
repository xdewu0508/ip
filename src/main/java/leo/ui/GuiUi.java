package leo.ui;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import leo.task.Task;
import leo.task.TaskList;

/**
 * GuiUi handles GUI-specific user interface operations for the Leo chatbot.
 * It extends Ui to provide JavaFX-based dialog bubbles for displaying messages.
 */
public class GuiUi extends Ui {
    private VBox dialogContainer;

    /**
     * Sets the dialog container for displaying messages.
     *
     * @param container the VBox to use as the dialog container
     */
    public void setDialogContainer(VBox container) {
        this.dialogContainer = container;
    }

    /**
     * Adds a dialog bubble with the specified text to the container.
     *
     * @param text the text to display
     * @param isUserMessage true if this is a user message (blue bubble), false otherwise (gray bubble)
     */
    private void addDialogBubble(String text, boolean isUserMessage) {
        if (dialogContainer != null) {
            Platform.runLater(() -> {
                Label label = new Label(text);
                label.setWrapText(true);
                label.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
                if (isUserMessage) {
                    label.setStyle("-fx-background-color: #007bff; -fx-background-radius: 10; -fx-text-fill: white;");
                } else {
                    label.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 10;");
                }
                label.setMaxWidth(Double.MAX_VALUE);
                dialogContainer.getChildren().add(label);
            });
        }
    }

    /**
     * Adds a dialog bubble with the specified text (bot message).
     *
     * @param text the text to display
     */
    private void addDialogBubble(String text) {
        addDialogBubble(text, false);
    }

    /**
     * Adds a user message bubble to the dialog.
     *
     * @param text the user's input text
     */
    public void addUserMessage(String text) {
        addDialogBubble(text, true);
    }

    @Override
    public void printLine() {
        addDialogBubble(LINE);
    }

    @Override
    public void printGreeting() {
        addDialogBubble(LINE);
        addDialogBubble("Hello! I'm Leo");
        addDialogBubble("What can I do for you?");
        addDialogBubble(LINE);
    }

    @Override
    public void printGoodbye() {
        addDialogBubble(LINE);
        addDialogBubble("Bye. Hope to see you again soon!");
        addDialogBubble(LINE);
    }

    @Override
    public void printList(java.util.ArrayList<Task> tasks) {
        addDialogBubble(LINE);
        addDialogBubble("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            addDialogBubble((i + 1) + "." + tasks.get(i));
        }
        addDialogBubble(LINE);
    }

    @Override
    public void printAddedTask(Task task, int taskCount) {
        addDialogBubble(LINE);
        addDialogBubble("Got it. I've added this task:");
        addDialogBubble("  " + task);
        addDialogBubble("Now you have " + taskCount + " tasks in the list.");
        addDialogBubble(LINE);
    }

    @Override
    public void printDeletedTask(Task task, int taskCount) {
        addDialogBubble(LINE);
        addDialogBubble("Noted. I've removed this task:");
        addDialogBubble("  " + task);
        addDialogBubble("Now you have " + taskCount + " tasks in the list.");
        addDialogBubble(LINE);
    }

    @Override
    public void printMarkedTask(Task task) {
        addDialogBubble(LINE);
        addDialogBubble("Nice! I've marked this task as done:");
        addDialogBubble("  " + task);
        addDialogBubble(LINE);
    }

    @Override
    public void printUnmarkedTask(Task task) {
        addDialogBubble(LINE);
        addDialogBubble("OK, I've marked this task as not done yet:");
        addDialogBubble("  " + task);
        addDialogBubble(LINE);
    }

    @Override
    public void printError(String message) {
        addDialogBubble(LINE);
        String[] lines = message.split("\\R");
        for (String line : lines) {
            addDialogBubble(line);
        }
        addDialogBubble(LINE);
    }

    @Override
    public void printFindResults(String keyword, TaskList tasks, String searchKeyword) {
        addDialogBubble(LINE);
        addDialogBubble("Here are the matching tasks in your list:");

        java.util.ArrayList<Task> allTasks = tasks.getAll();
        int matchCount = 0;
        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            if (task.getDescription().toLowerCase().contains(searchKeyword.toLowerCase())) {
                matchCount++;
                addDialogBubble((matchCount) + "." + task);
            }
        }

        if (matchCount == 0) {
            addDialogBubble("No tasks found containing \"" + keyword + "\".");
        }
        addDialogBubble(LINE);
    }
}
