package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Task;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * DeleteCommand removes a task from the task list.
 * It validates the task index, removes the task, saves changes, and confirms to the user.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Constructs a DeleteCommand for the specified task index.
     *
     * @param index the zero-based index of the task to delete
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the delete command by removing the specified task.
     * Validates the index, removes the task, persists changes, and displays confirmation.
     *
     * @param tasks the task list containing the task to delete
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if the index is invalid or there are no tasks
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        validateIndex(index, tasks.size(), "delete");
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.printDeletedTask(removed, tasks.size());
    }

    /**
     * Validates that the given index is within the valid range for the task list.
     *
     * @param index the index to validate
     * @param taskCount the total number of tasks in the list
     * @param commandWord the command word for error messages
     * @throws LeoException if the task list is empty or the index is out of bounds
     */
    private void validateIndex(int index, int taskCount, String commandWord) throws LeoException {
        if (taskCount == 0) {
            throw new LeoException("There are no tasks to " + commandWord + ".");
        }
        if (index < 0 || index >= taskCount) {
            throw new LeoException("Please give a valid task number to " + commandWord + ".");
        }
    }
}
