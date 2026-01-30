import java.util.Scanner;

public class Leo {
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;

        printGreeting();

        while (true) {
            String task = scanner.nextLine().trim();

            if (task.equals("bye")) {
                printGoodbye();
                break;
            }

            if (task.equals("list")) {
                printList(tasks, taskCount);
                continue;
            }

            if (taskCount < MAX_TASKS) {
                tasks[taskCount] = task;
                taskCount++;
                printAdded(task);
            } else {
                printBox("Too many tasks. I can only store up to " + MAX_TASKS + " tasks. ");
            }
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

    private static void printAdded(String task) {
        printLine();
        System.out.println("added: " + task);
        printLine();
    }

    private static void printList(String[] tasks, int taskCount) {
        printLine();
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
        printLine();
    }

    private static void printBox(String message) {

    }
}
