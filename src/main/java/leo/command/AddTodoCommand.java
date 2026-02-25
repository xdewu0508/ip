package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Todo;
import leo.ui.Ui;

/**
 * AddTodoCommand adds a new Todo task to the task list.
 * A Todo is a simple task with just a description.
 */
public class AddTodoCommand extends Command implements UndoableAddCommand {
    private final String description;

    /**
     * Constructs an AddTodoCommand with the specified task description.
     *
     * @param description the description of the todo task
     */
    public AddTodoCommand(String description) {
        this.description = description;
    }

    /**
     * Executes the add todo command by creating a new Todo task,
     * adding it to the list, saving changes, and displaying confirmation.
     *
     * @param tasks the task list to add the task to
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if adding the task fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        Task t = new Todo(description);
        tasks.add(t);
        storage.save(tasks);
        ui.printAddedTask(t, tasks.size());
    }
}
