public abstract class Task {
    protected final TaskType type;
    protected final String description;
    protected boolean isDone;

    protected Task(TaskType type, String description) {
        this.type = type;
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

    @Override
    public String toString() {
        return type.tag() + statusIcon() + " " + description;
    }
}
