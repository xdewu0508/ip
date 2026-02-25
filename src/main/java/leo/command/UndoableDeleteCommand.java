package leo.command;

/**
 * Marker interface for delete commands that can be undone.
 */
public interface UndoableDeleteCommand {
    /**
     * Returns the index of the task that was deleted.
     *
     * @return the task index
     */
    int getTaskIndex();
}
