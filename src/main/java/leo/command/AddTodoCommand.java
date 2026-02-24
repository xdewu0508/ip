package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Todo;
import leo.ui.Ui;

public class AddTodoCommand extends Command {
    private final String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        Task t = new Todo(description);
        tasks.add(t);
        storage.save(tasks);
        ui.printAddedTask(t, tasks.size());
    }
}
