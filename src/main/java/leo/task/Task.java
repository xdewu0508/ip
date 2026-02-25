package leo.task;

/**
 * Task is the abstract base class representing a task in the Leo chatbot.
 * All task types (Todo, Deadline, Event) extend this class.
 * A task has a description, a done status, and a type.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Constructs a new Task with the specified description and type.
     *
     * @param description the description of the task
     * @param type the type of the task (TODO, DEADLINE, or EVENT)
     */
    public Task(String description, TaskType type) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        assert type != null : "Task type cannot be null";
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status icon for this task.
     *
     * @return "X" if the task is done, " " otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task is done.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the type of this task.
     *
     * @return the TaskType of this task
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this task.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty";
        this.description = description;
    }

    /**
     * Returns a string representation of this task.
     * Format: [Type][Status] Description
     *
     * @return the formatted task string
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
