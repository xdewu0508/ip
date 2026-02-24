package leo.task;

import java.time.LocalDateTime;
import leo.util.DateTimeUtil;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return super.toString()
                + " (from: " + DateTimeUtil.format(from)
                + " to: " + DateTimeUtil.format(to) + ")";
    }
}
