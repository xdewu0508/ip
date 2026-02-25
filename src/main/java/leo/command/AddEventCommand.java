package leo.command;

import java.time.LocalDateTime;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Event;
import leo.task.Task;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * AddEventCommand adds a new Event task to the task list.
 * An Event is a task with a description and a time range (start and end).
 */
public class AddEventCommand extends Command implements UndoableAddCommand {
    private final String description;
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an AddEventCommand with the specified description and time range.
     *
     * @param description the description of the event task
     * @param from the start date/time of the event
     * @param to the end date/time of the event
     */
    public AddEventCommand(String description, LocalDateTime from, LocalDateTime to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    /**
     * Executes the add event command by creating a new Event task,
     * adding it to the list, saving changes, and displaying confirmation.
     * Validates that the end time is not before the start time.
     *
     * @param tasks the task list to add the task to
     * @param storage the storage for saving changes
     * @param ui the UI for displaying confirmation
     * @throws LeoException if the end time is before start time, or if adding fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        if (to.isBefore(from)) {
            throw new LeoException("Event end time cannot be before start time.");
        }
        Task e = new Event(description, from, to);
        tasks.add(e);
        storage.save(tasks);
        ui.printAddedTask(e, tasks.size());
    }
}
