import java.util.Scanner;

public class Leo {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printGreeting();

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                printGoodbye();
                break;
            }

            if (input.equals("list")) {
                printList(tasks, taskCount);
                continue;
            }

            if (input.startsWith("mark ")) {
                int index = parseIndex(input); // 0-based
                tasks[index].markAsDone();
                printMarked(tasks[index]);
                continue;
            }

            if (input.startsWith("unmark ")) {
                int index = parseIndex(input); // 0-based
                tasks[index].markAsNotDone();
                printUnmarked(tasks[index]);
                continue;
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                tasks[taskCount] = new Todo(desc);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                int byPos = rest.indexOf("/by");
                String desc = rest.substring(0, byPos).trim();
                String by = rest.substring(byPos + 3).trim();
                tasks[taskCount] = new Deadline(desc, by);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int fromPos = rest.indexOf("/from");
                int toPos = rest.indexOf("/to");
                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 5, toPos).trim();
                String to = rest.substring(toPos + 3).trim();
                tasks[taskCount] = new Event(desc, from, to);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
                continue;
            }

            if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Todo(input);
                taskCount++;
                printAddedTask(tasks[taskCount - 1], taskCount);
            } else {
                printBox("Too many tasks. I can only store up to " + MAX_TASKS + " tasks. ");
            }
        }
    }

    private static int parseIndex(String input) {
        String[] parts = input.split(" ");
        return Integer.parseInt(parts[1]) - 1;
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

    private static void printAddedTask(Task task, int taskCount) {
        printLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        printLine();
    }

    private static void printList(Task[] tasks, int taskCount) {
        printLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
        printLine();
    }

    private static void printMarked(Task task) {
        printLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
        printLine();
    }

    private static void printUnmarked(Task task) {
        printLine();
        System.out.println("OK, I've unmarked this task as not done yet:");
        System.out.println("  " + task);
        printLine();
    }

    private static void printBox(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }
}
