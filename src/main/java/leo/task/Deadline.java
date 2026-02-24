package leo.task;

import java.time.LocalDateTime;
import leo.util.DateTimeUtil;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString()
                + " (by: " + DateTimeUtil.format(by) + ")";
    }
}
