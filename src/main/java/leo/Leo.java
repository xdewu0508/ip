package leo;

import leo.command.Command;
import leo.command.Parser;
import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * Leo is a task management chatbot that helps users manage their tasks.
 * It supports three types of tasks: Todo, Deadline, and Event.
 * Users can add, list, mark, unmark, and delete tasks through a command-line interface.
 * The application persists tasks to a file for data persistence across sessions.
 */
public class Leo {
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList tasks;

    /**
     * Constructs a Leo chatbot with the specified file path for data storage.
     * Initializes the UI, parser, storage, and loads existing tasks from storage.
     * If loading fails, starts with an empty task list and displays an error message.
     *
     * @param filePath the path to the file where tasks are stored
     */
    public Leo(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);

        try {
            tasks = storage.load();
        } catch (LeoException e) {
            tasks = new TaskList();
            ui.printError("Save file problem. Starting with an empty list.\n" + e.getMessage());
        }
    }

    /**
     * Runs the main chatbot loop.
     * Displays a greeting message and continuously reads user commands,
     * parses them, executes the corresponding actions, and handles exceptions.
     * The loop continues until an exit command is received.
     */
    public void run() {
        ui.printGreeting();

        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command cmd = parser.parse(input);
                cmd.execute(tasks, storage, ui);
                isExit = cmd.isExit();
            } catch (LeoException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    /**
     * The main entry point of the Leo application.
     * Creates a new Leo instance with the default data file path and runs it.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Leo("data/leo.txt").run();
    }
}
