package leo.command;

import leo.exception.LeoException;
import leo.history.CommandHistory;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * UndoCommand undoes the most recent command that modified the task list.
 * It uses the CommandHistory to track and reverse command effects.
 */
public class UndoCommand extends Command {
    private CommandHistory history;

    /**
     * Constructs an UndoCommand with the specified command history.
     *
     * @param history the command history to undo from
     */
    public UndoCommand(CommandHistory history) {
        this.history = history;
    }

    /**
     * Executes the undo operation by reversing the last command.
     *
     * @param tasks the task list to modify
     * @param storage the storage for persisting changes
     * @param ui the UI for displaying results
     * @throws LeoException if undo fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        if (!history.canUndo()) {
            throw new LeoException("Nothing to undo.");
        }

        Command lastCommand = history.getLastCommand();
        history.undo();

        // Undo the command by reversing its effect
        undoCommand(lastCommand, tasks, storage, ui);
    }

    /**
     * Undoes the effect of the given command.
     *
     * @param command the command to undo
     * @param tasks the task list to modify
     * @param storage the storage for persisting changes
     * @param ui the UI for displaying results
     * @throws LeoException if undo fails
     */
    private void undoCommand(Command command, TaskList tasks, Storage storage, Ui ui) throws LeoException {
        // For add commands, we remove the last added task
        if (command instanceof UndoableAddCommand) {
            UndoableAddCommand addCommand = (UndoableAddCommand) command;
            int taskCount = tasks.size();
            if (taskCount > 0) {
                tasks.remove(taskCount - 1);
                storage.save(tasks);
                ui.printUndo("Undone add command. Task removed.");
            }
        } else if (command instanceof UndoableMarkCommand) {
            UndoableMarkCommand markCommand = (UndoableMarkCommand) command;
            int index = markCommand.getTaskIndex();
            if (index >= 0 && index < tasks.size()) {
                tasks.get(index).markAsNotDone();
                storage.save(tasks);
                ui.printUndo("Undone mark command. Task marked as not done.");
            }
        } else if (command instanceof UndoableUnmarkCommand) {
            UndoableUnmarkCommand unmarkCommand = (UndoableUnmarkCommand) command;
            int index = unmarkCommand.getTaskIndex();
            if (index >= 0 && index < tasks.size()) {
                tasks.get(index).markAsDone();
                storage.save(tasks);
                ui.printUndo("Undone unmark command. Task marked as done.");
            }
        } else if (command instanceof UndoableDeleteCommand) {
            UndoableDeleteCommand deleteCommand = (UndoableDeleteCommand) command;
            int index = deleteCommand.getTaskIndex();
            // Note: This is a simplified undo - we can't fully restore without storing the deleted task
            ui.printUndo("Delete command cannot be fully undone. Task was at index " + (index + 1));
        } else {
            ui.printUndo("Command type cannot be undone.");
        }
    }
}
