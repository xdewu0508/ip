package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * ExitCommand terminates the Leo chatbot application.
 * It displays a goodbye message and signals the main loop to exit.
 */
public class ExitCommand extends Command {
    /**
     * Executes the exit command by printing a goodbye message.
     *
     * @param tasks the task list (not used for this command)
     * @param storage the storage (not used for this command)
     * @param ui the UI for displaying the goodbye message
     * @throws LeoException if exit fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        ui.printGoodbye();
    }

    /**
     * Returns true to signal that the application should exit.
     *
     * @return true always
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
