package leo.ui;

import java.util.ArrayList;
import java.util.Scanner;

import leo.task.Task;
import leo.task.TaskList;

/**
 * Ui handles all user interface operations for the Leo chatbot.
 * It manages user input and output, including printing messages,
 * displaying task lists, and reading user commands.
 */
public class Ui {
    protected static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    /**
     * Constructs a new Ui instance with a scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a separator line to the console.
     */
    public void printLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the greeting message when the chatbot starts.
     */
    public void printGreeting() {
        printLine();
        System.out.println("Hello! I'm Leo");
        System.out.println("What can I do for you?");
        printLine();
    }

    /**
     * Prints the goodbye message when the chatbot exits.
     */
    public void printGoodbye() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    /**
     * Prints all tasks in the given task list.
     *
     * @param tasks the list of tasks to display
     */
    public void printList(ArrayList<Task> tasks) {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        printLine();
    }

    /**
     * Prints a confirmation message after adding a new task.
     *
     * @param task the task that was added
     * @param taskCount the total number of tasks after adding
     */
    public void printAddedTask(Task task, int taskCount) {
        printLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    /**
     * Prints a confirmation message after deleting a task.
     *
     * @param task the task that was removed
     * @param taskCount the total number of tasks after deletion
     */
    public void printDeletedTask(Task task, int taskCount) {
        printLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    /**
     * Prints a confirmation message after marking a task as done.
     *
     * @param task the task that was marked as done
     */
    public void printMarkedTask(Task task) {
        printLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        printLine();
    }

    /**
     * Prints a confirmation message after marking a task as not done.
     *
     * @param task the task that was marked as not done
     */
    public void printUnmarkedTask(Task task) {
        printLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        printLine();
    }

    /**
     * Prints an error message to the user.
     * Handles multi-line messages by splitting on line breaks.
     *
     * @param message the error message to display
     */
    public void printError(String message) {
        printLine();
        String[] lines = message.split("\\R");
        for (String line : lines) {
            System.out.println(line);
        }
        printLine();
    }

    /**
     * Reads a command from user input.
     *
     * @return the trimmed user input as a string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Prints the results of a find command.
     * Displays all tasks containing the keyword in their description.
     *
     * @param keyword the keyword that was searched
     * @param tasks the task list to search
     * @param searchKeyword the keyword to match against task descriptions
     */
    public void printFindResults(String keyword, TaskList tasks, String searchKeyword) {
        printLine();
        System.out.println("Here are the matching tasks in your list:");

        ArrayList<Task> allTasks = tasks.getAll();
        int matchCount = 0;
        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            if (task.getDescription().toLowerCase().contains(searchKeyword.toLowerCase())) {
                matchCount++;
                System.out.println((matchCount) + "." + task);
            }
        }

        if (matchCount == 0) {
            System.out.println("No tasks found containing \"" + keyword + "\".");
        }
        printLine();
    }
}
