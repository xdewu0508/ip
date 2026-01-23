import java.time.LocalDateTime;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() { return from; }
    public LocalDateTime getTo() { return to; }

    @Override
    public String toString() {
        if (from != null && to != null) {
            return super.toString() + " (from: " + DateTimeUtil.formatForUser(from)
                    + " to: " + DateTimeUtil.formatForUser(to) + ")";
        }
        if (from != null) {
            return super.toString() + " (at: " + DateTimeUtil.formatForUser(from) + ")";
        }
        return super.toString();
    }
}
