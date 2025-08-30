public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String typeLetter() {
        return "[E]";
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
