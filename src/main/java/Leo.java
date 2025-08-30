import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        final String BAR = "____________________";

        System.out.println(BAR);
        System.out.println("Hello! I'm Leo.");
        System.out.println("What can I do for you?");
        System.out.println(BAR);

        String[] tasks = new String[100];
        boolean[] done = new boolean[100];
        int taskCount = 0;

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine().trim();
            if (line.equals("bye")) {
                break;
            }

            if (line.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    String status = done[i] ? "[X]" : "[ ]";
                    System.out.println((i + 1) + ". " + status + " " + tasks[i]);
                }
                continue;
            }

            if (line.startsWith("mark ")) {
                Integer idx = parseIndex(line.substring(5));
                if (idx == null || idx < 1 || idx > taskCount) {
                    System.out.println("Please give a valid task number to mark.");
                } else {
                    done[idx - 1] = true;
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  [X] " + tasks[idx - 1]);
                }
                continue;
            }

            if (line.startsWith("unmark ")) {
                Integer idx = parseIndex(line.substring(7));
                if (idx == null || idx < 1 || idx > taskCount) {
                    System.out.println("Please give a valid task number to unmark.");
                } else {
                    done[idx - 1] = false;
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  [ ] " + tasks[idx - 1]);
                }
                continue;
            }

            if (!line.isEmpty()) {
                if (taskCount < tasks.length) {
                    tasks[taskCount] = line;
                    done[taskCount] = false;
                    taskCount++;
                    System.out.println("added: " + line);
                } else {
                    System.out.println("Sorry, I can only store up to 100 tasks for now.");
                }
            }
        }
        sc.close();

        System.out.println(BAR);
        System.out.println("Bye! Hope to see you soon!");
    }

    private static Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
