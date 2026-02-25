package leo.command;

/**
 * Marker interface for mark commands that can be undone.
 */
public interface UndoableMarkCommand {
    /**
     * Returns the index of the task that was marked.
     *
     * @return the task index
     */
    int getTaskIndex();
}
