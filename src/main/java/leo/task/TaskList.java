package leo.task;

import java.util.ArrayList;

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
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index the zero-based index of the task
     * @return the task at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Task get(int index) {
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
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the zero-based index of the task to unmark
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markAsNotDone(int index) {
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
}
