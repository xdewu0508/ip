package leo.task;

import java.time.LocalDateTime;
import leo.util.DateTimeUtil;

/**
 * Event represents a task with a time range (start and end).
 * Events have a description and occur between specific start and end times.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an Event with the specified description and time range.
     *
     * @param description the description of the event
     * @param from the start date/time of the event
     * @param to the end date/time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date/time of this event.
     *
     * @return the start date/time
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end date/time of this event.
     *
     * @return the end date/time
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns a string representation of this event.
     * Format: [E][Status] Description (from: DateTime to: DateTime)
     *
     * @return the formatted event string
     */
    @Override
    public String toString() {
        return super.toString()
                + " (from: " + DateTimeUtil.format(from)
                + " to: " + DateTimeUtil.format(to) + ")";
    }
}
