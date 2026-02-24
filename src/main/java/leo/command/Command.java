package leo.command;

import leo.exception.LeoException;
import leo.task.TaskList;
import leo.storage.Storage;
import leo.ui.Ui;

public abstract class Command {
    /**
     * Executes the command.
     */
    public abstract void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException;

    /**
     * Returns true if this command should exit the application.
     */
    public boolean isExit() {
        return false;
    }
}
