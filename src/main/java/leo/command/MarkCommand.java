package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * MarkCommand marks a task as done.
 * It validates the task index, updates the task status, saves changes, and confirms to the user.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Constructs a MarkCommand for the specified task index.
     *
     * @param index the zero-based index of the task to mark as done
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Executes the mark command by marking the specified task as done.
     * Validates the index, updates the task, persists changes, and displays confirmation.
     *
     * @param tasks the task list containing the task to mark
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if the index is invalid or there are no tasks
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        validateIndex(index, tasks.size(), "mark");
        tasks.markAsDone(index);
        storage.save(tasks);
        ui.printMarkedTask(tasks.get(index));
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
