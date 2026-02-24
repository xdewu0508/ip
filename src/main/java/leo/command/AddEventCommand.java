package leo.command;

import java.time.LocalDateTime;
import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Event;
import leo.ui.Ui;

public class AddEventCommand extends Command {
    private final String description;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public AddEventCommand(String description, LocalDateTime from, LocalDateTime to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

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
