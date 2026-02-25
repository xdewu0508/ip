package leo.command;

import leo.exception.LeoException;
import leo.task.TaskList;
import leo.storage.Storage;
import leo.ui.Ui;

/**
 * Command is the abstract base class for all commands in the Leo chatbot.
 * Each command represents a user action (e.g., add task, list tasks, delete task).
 * Commands follow the Command pattern, encapsulating the logic for executing specific actions.
 */
public abstract class Command {
    /**
     * Executes the command's logic.
     * This method should modify the task list, storage, and/or UI as needed.
     *
     * @param tasks the task list to operate on
     * @param storage the storage for persisting changes
     * @param ui the UI for displaying results
     * @throws LeoException if the command execution fails
     */
    public abstract void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException;

    /**
     * Returns true if this command should cause the application to exit.
     * Override this method in exit commands to return true.
     *
     * @return true if the application should exit, false otherwise
     */
    public boolean isExit() {
        return false;
    }
}
