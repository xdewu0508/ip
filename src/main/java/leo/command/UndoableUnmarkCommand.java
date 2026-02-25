package leo.command;

/**
 * Marker interface for unmark commands that can be undone.
 */
public interface UndoableUnmarkCommand {
    /**
     * Returns the index of the task that was unmarked.
     *
     * @return the task index
     */
    int getTaskIndex();
}
