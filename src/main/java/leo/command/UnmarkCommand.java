package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * UnmarkCommand marks a task as not done.
 * It validates the task index, updates the task status, saves changes, and confirms to the user.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Constructs an UnmarkCommand for the specified task index.
     *
     * @param index the zero-based index of the task to mark as not done
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the unmark command by marking the specified task as not done.
     * Validates the index, updates the task, persists changes, and displays confirmation.
     *
     * @param tasks the task list containing the task to unmark
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if the index is invalid or there are no tasks
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        validateIndex(index, tasks.size(), "unmark");
        tasks.markAsNotDone(index);
        storage.save(tasks);
        ui.printUnmarkedTask(tasks.get(index));
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
