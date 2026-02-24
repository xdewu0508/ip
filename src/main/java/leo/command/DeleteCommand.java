package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.Task;
import leo.task.TaskList;
import leo.ui.Ui;

public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        validateIndex(index, tasks.size(), "delete");
        Task removed = tasks.remove(index);
        storage.save(tasks);
        ui.printDeletedTask(removed, tasks.size());
    }

    private void validateIndex(int index, int taskCount, String commandWord) throws LeoException {
        if (taskCount == 0) {
            throw new LeoException("There are no tasks to " + commandWord + ".");
        }
        if (index < 0 || index >= taskCount) {
            throw new LeoException("Please give a valid task number to " + commandWord + ".");
        }
    }
}
