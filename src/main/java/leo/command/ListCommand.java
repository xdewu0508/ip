package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * ListCommand displays all tasks in the task list to the user.
 * This command does not modify the task list or storage.
 */
public class ListCommand extends Command {
    /**
     * Executes the list command by displaying all tasks.
     *
     * @param tasks the task list to display
     * @param storage the storage (not used for this command)
     * @param ui the UI for displaying the task list
     * @throws LeoException if listing fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        ui.printList(tasks.getAll());
    }
}
