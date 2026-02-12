import java.util.ArrayList;
import java.util.Scanner;

public class Leo {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Storage storage = new Storage("data/leo.txt");
        ArrayList<Task> tasks;

        try {
            tasks = storage.load();
        } catch (LeoException e) {
            tasks = new ArrayList<>();
            printError("Save file problem. Starting with an empty list.\n" + e.getMessage());
        }

        printGreeting();

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                printGoodbye();
                break;
            }

            try {
                handleCommand(input, tasks, storage);
            } catch (LeoException e) {
                printError(e.getMessage());
            }
        }
    }

    private static void handleCommand(String input, ArrayList<Task> tasks, Storage storage) throws LeoException {
        if (input.equals("list")) {
            printList(tasks);
            return;
        }

        if (input.startsWith("mark")) {
            int index = parseIndex(input, "mark");
            ensureIndexValid(index, tasks.size(), "mark");
            tasks.get(index).markAsDone();
            storage.save(tasks);
            printMarkedTask(tasks.get(index));
            return;
        }

        if (input.startsWith("unmark")) {
            int index = parseIndex(input, "unmark");
            ensureIndexValid(index, tasks.size(), "unmark");
            tasks.get(index).markAsNotDone();
            storage.save(tasks);
            printUnmarkedTask(tasks.get(index));
            return;
        }

        if (input.startsWith("delete")) {
            int index = parseIndex(input, "delete");
            ensureIndexValid(index, tasks.size(), "delete");
            Task removed = tasks.remove(index);
            storage.save(tasks);
            printDeletedTask(removed, tasks.size());
            return;
        }

        if (input.startsWith("todo")) {
            String desc = input.length() > 4 ? input.substring(4).trim() : "";
            if (desc.isEmpty()) {
                throw new LeoException("The description of a todo cannot be empty.");
            }
            ensureCapacity(tasks.size());
            Task t = new Todo(desc);
            tasks.add(t);
            storage.save(tasks);
            printAddedTask(t, tasks.size());
            return;
        }

        if (input.startsWith("deadline")) {
            String rest = input.length() > 8 ? input.substring(8).trim() : "";
            if (rest.isEmpty()) {
                throw new LeoException("The description of a deadline cannot be empty.");
            }
            int byPos = rest.indexOf("/by");
            if (byPos == -1) {
                throw new LeoException("Usage: deadline <description> /by <time>");
            }

            String desc = rest.substring(0, byPos).trim();
            String by = rest.substring(byPos + 3).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new LeoException("Usage: deadline <description> /by <time>");
            }

            ensureCapacity(tasks.size());
            Task d = new Deadline(desc, by);
            tasks.add(d);
            storage.save(tasks);
            printAddedTask(d, tasks.size());
            return;
        }

        if (input.startsWith("event")) {
            String rest = input.length() > 5 ? input.substring(5).trim() : "";
            if (rest.isEmpty()) {
                throw new LeoException("The description of an event cannot be empty.");
            }

            int fromPos = rest.indexOf("/from");
            int toPos = rest.indexOf("/to");
            if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                throw new LeoException("Usage: event <description> /from <start> /to <end>");
            }

            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 5, toPos).trim();
            String to = rest.substring(toPos + 3).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new LeoException("Usage: event <description> /from <start> /to <end>");
            }

            ensureCapacity(tasks.size());
            Task e = new Event(desc, from, to);
            tasks.add(e);
            storage.save(tasks);
            printAddedTask(e, tasks.size());
            return;
        }

        throw new LeoException("Not a valid command. Please use one of the following commands:\n" +
                "todo, deadline, event, list, mark, unmark, delete, bye");
    }

    private static void ensureCapacity(int size) throws LeoException {
        if (size >= MAX_TASKS) {
            throw new LeoException("Too many tasks. I can only store up to " + MAX_TASKS + " tasks.");
        }
    }

    private static int parseIndex(String input, String commandWord) throws LeoException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new LeoException("Usage: " + commandWord + " <task number>");
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new LeoException("Usage: " + commandWord + " <task number>");
        }
    }

    private static void ensureIndexValid(int index, int taskCount, String commandWord) throws LeoException {
        if (taskCount == 0) {
            throw new LeoException("There are no tasks to " + commandWord + ".");
        }
        if (index < 0 || index >= taskCount) {
            throw new LeoException("Please give a valid task number to " + commandWord + ".");
        }
    }

    private static void printLine() {
        System.out.println(LINE);
    }

    private static void printGreeting() {
        printLine();
        System.out.println("Hello! I'm Leo");
        System.out.println("What can I do for you?");
        printLine();
    }

    private static void printGoodbye() {
        printLine();
        System.out.println("Bye. Hope to see you again soon!");
        printLine();
    }

    private static void printList(ArrayList<Task> tasks) {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        printLine();
    }

    private static void printAddedTask(Task task, int taskCount) {
        printLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    private static void printDeletedTask(Task task, int taskCount) {
        printLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    private static void printMarkedTask(Task task) {
        printLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        printLine();
    }

    private static void printUnmarkedTask(Task task) {
        printLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
        printLine();
    }

    private static void printError(String message) {
        printLine();
        String[] lines = message.split("\\R");
        for (String line : lines) {
            System.out.println(line);
        }
        printLine();
    }
}
