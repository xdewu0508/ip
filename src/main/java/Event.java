public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        if (from != null && to != null) {
            return super.toString() + " (from: " + from + " to: " + to + ")";
        } else if (from != null) {
            return super.toString() + " (at: " + from + ")";
        } else {
            return super.toString();
        }
    }
}
