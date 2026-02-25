package leo.task;

/**
 * Todo represents a simple task with just a description.
 * Todos are the most basic task type without any time constraints.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo with the specified description.
     *
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
