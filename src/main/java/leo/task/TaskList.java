package leo.task;

import java.util.ArrayList;
import java.util.stream.Collectors;
import leo.exception.LeoException;

/**
 * TaskList manages a collection of Task objects.
 * It provides methods for adding, removing, accessing, and manipulating tasks.
 * The TaskList uses an ArrayList internally for storage.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the specified list of tasks.
     *
     * @param tasks the initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the task list.
     * Checks for duplicate tasks and throws LeoException if a duplicate is found.
     *
     * @param task the task to add
     * @throws LeoException if the task already exists in the list
     */
    public void add(Task task) throws LeoException {
        assert task != null : "Task cannot be null";
        if (containsDuplicate(task)) {
            throw new LeoException("A similar task already exists in the list.");
        }
        tasks.add(task);
    }

    /**
     * Checks if the task list contains a duplicate of the given task.
     * Two tasks are considered duplicates if they have the same type, description,
     * and time fields (if applicable).
     *
     * @param newTask the task to check for duplicates
     * @return true if a duplicate exists, false otherwise
     */
    private boolean containsDuplicate(Task newTask) {
        for (Task existing : tasks) {
            if (isDuplicate(existing, newTask)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if two tasks are duplicates of each other.
     *
     * @param existing the existing task
     * @param newTask the new task to compare
     * @return true if the tasks are duplicates, false otherwise
     */
    private boolean isDuplicate(Task existing, Task newTask) {
        // Different types cannot be duplicates
        if (existing.getType() != newTask.getType()) {
            return false;
        }

        // Compare descriptions (case-insensitive)
        if (!existing.getDescription().equalsIgnoreCase(newTask.getDescription())) {
            return false;
        }

        // For Deadline tasks, compare deadline times
        if (existing instanceof Deadline && newTask instanceof Deadline) {
            Deadline existingDeadline = (Deadline) existing;
            Deadline newDeadline = (Deadline) newTask;
            return existingDeadline.getBy().equals(newDeadline.getBy());
        }

        // For Event tasks, compare start and end times
        if (existing instanceof Event && newTask instanceof Event) {
            Event existingEvent = (Event) existing;
            Event newEvent = (Event) newTask;
            return existingEvent.getFrom().equals(newEvent.getFrom())
                    && existingEvent.getTo().equals(newEvent.getTo());
        }

        // For Todo tasks, description match is sufficient (already checked above)
        return true;
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the zero-based index of the task
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int index) {
        assert index >= 0 : "Index must be non-negative: " + index;
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index the zero-based index of the task to remove
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task remove(int index) {
        assert index >= 0 : "Index must be non-negative: " + index;
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns all tasks in the list.
     * Note: This returns the internal ArrayList reference.
     *
     * @return the ArrayList containing all tasks
     */
    public ArrayList<Task> getAll() {
        return tasks;
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index the zero-based index of the task to mark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markAsDone(int index) {
        assert index >= 0 : "Index must be non-negative: " + index;
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the zero-based index of the task to unmark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markAsNotDone(int index) {
        assert index >= 0 : "Index must be non-negative: " + index;
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns true if the task list is empty.
     *
     * @return true if there are no tasks, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns all done tasks using Java Streams.
     * Uses method reference for filtering.
     *
     * @return an ArrayList containing only done tasks
     */
    public ArrayList<Task> getDoneTasks() {
        return tasks.stream()
                .filter(Task::isDone)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns all not done tasks using Java Streams.
     * Uses lambda expression for filtering.
     *
     * @return an ArrayList containing only not done tasks
     */
    public ArrayList<Task> getNotDoneTasks() {
        return tasks.stream()
                .filter(task -> !task.isDone())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns tasks matching the given keyword using Java Streams.
     * Search is case-insensitive.
     *
     * @param keyword the keyword to search for
     * @return an ArrayList containing matching tasks
     */
    public ArrayList<Task> findTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
