import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        final String BAR = "____________________";

        System.out.println(BAR);
        System.out.println("Hello! I'm Leo.");
        System.out.println("What can I do for you?");
        System.out.println(BAR);

        String[] tasks = new String[100];
        int taskCount = 0;

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine().trim();

            if (line.equals("bye")) {
                break;
            } else if (line.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (!line.isEmpty()) {
                if (taskCount < tasks.length) {
                    tasks[taskCount++] = line;
                    System.out.println("added: " + line);
                } else {
                    // friendly message when >100 items
                    System.out.println("Sorry, I can only store up to 100 tasks for now.");
                }
            }
        }
        sc.close();

        System.out.println(BAR);
        System.out.println("Bye! Hope to see you soon!");
    }
}
