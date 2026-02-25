package leo.command;

import java.time.LocalDateTime;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Deadline;
import leo.task.Task;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * AddDeadlineCommand adds a new Deadline task to the task list.
 * A Deadline is a task with a description and a due date/time.
 */
public class AddDeadlineCommand extends Command implements UndoableAddCommand {
    private final String description;
    private final LocalDateTime by;

    /**
     * Constructs an AddDeadlineCommand with the specified description and deadline.
     *
     * @param description the description of the deadline task
     * @param by the deadline date/time by which the task must be completed
     */
    public AddDeadlineCommand(String description, LocalDateTime by) {
        this.description = description;
        this.by = by;
    }

    /**
     * Executes the add deadline command by creating a new Deadline task,
     * adding it to the list, saving changes, and displaying confirmation.
     *
     * @param tasks the task list to add the task to
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if adding the task fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        Task d = new Deadline(description, by);
        tasks.add(d);
        storage.save(tasks);
        ui.printAddedTask(d, tasks.size());
    }
}
