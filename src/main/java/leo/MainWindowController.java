package leo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import leo.command.Command;
import leo.command.Parser;
import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.GuiUi;

/**
 * MainWindowController handles the GUI logic for the Leo chatbot.
 * It manages user input, displays dialog bubbles, and processes commands.
 */
public class MainWindowController {
    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private VBox dialogBox;

    @FXML
    private ScrollPane scrollPane;

    private Parser parser;
    private Storage storage;
    private TaskList tasks;
    private GuiUi ui;

    /**
     * Initializes the controller and sets up the chatbot.
     */
    @FXML
    public void initialize() {
        storage = new Storage("data/leo.txt");
        parser = new Parser();
        ui = new GuiUi();

        try {
            tasks = storage.load();
        } catch (LeoException e) {
            tasks = new TaskList();
        }

        // Set initial greeting
        ui.setDialogContainer(dialogBox);
        ui.printGreeting();
    }

    /**
     * Handles the send button click event.
     */
    @FXML
    private void handleButtonClicked() {
        handleUserInput();
    }

    /**
     * Handles key press events in the text field.
     *
     * @param event the key event
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleUserInput();
        }
    }

    /**
     * Processes the user input and displays the response.
     */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        // Display user input
        ui.addUserMessage(input);

        // Clear input field
        userInput.clear();

        // Process command
        try {
            Command cmd = parser.parse(input);
            cmd.execute(tasks, storage, ui);

            // Scroll to bottom after response
            scrollPane.setVvalue(1.0);

            // Check if exit command
            if (cmd.isExit()) {
                handleClose();
            }
        } catch (LeoException e) {
            ui.printError(e.getMessage());
            scrollPane.setVvalue(1.0);
        }
    }

    /**
     * Handles the window close event.
     */
    private void handleClose() {
        ui.printGoodbye();
        // Delay closing to allow user to see goodbye message
        javafx.application.Platform.runLater(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // Ignore
            }
            javafx.application.Platform.exit();
        });
    }
}
