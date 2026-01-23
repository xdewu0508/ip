import java.time.LocalDateTime;

public class Deadline extends Task {
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    public LocalDateTime getBy() { return by; }

    @Override
    public String toString() {
        return super.toString() + " (by: " + DateTimeUtil.formatForUser(by) + ")";
    }
}
