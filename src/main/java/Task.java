public abstract class Task {
    protected final String description;
    protected boolean isDone;

    protected Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    protected String statusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    public abstract String typeLetter();

    @Override
    public String toString() {
        return typeLetter() + statusIcon() + " " + description;
    }
}
