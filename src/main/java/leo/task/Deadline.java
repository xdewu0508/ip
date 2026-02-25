package leo.task;

import java.time.LocalDateTime;
import leo.util.DateTimeUtil;

/**
 * Deadline represents a task with a due date/time.
 * Deadlines have a description and a specific time by which they must be completed.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a Deadline with the specified description and due date/time.
     *
     * @param description the description of the deadline task
     * @param by the deadline date/time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the due date/time of this deadline.
     *
     * @return the deadline date/time
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns a string representation of this deadline.
     * Format: [D][Status] Description (by: DateTime)
     *
     * @return the formatted deadline string
     */
    @Override
    public String toString() {
        return super.toString()
                + " (by: " + DateTimeUtil.format(by) + ")";
    }
}
