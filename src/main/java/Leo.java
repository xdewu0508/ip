//package

import java.util.Scanner;
public class Leo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String greeting = "____________________________________________________________\n" +
                " Hello! I'm Leo\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n";
        System.out.println(greeting);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("____________________________________________________________\n" +
                        "Bye. Hope to see you again soon!\n" +
                        "____________________________________________________________");
                break;
            }
            System.out.println("____________________________________________________________\n" +
                    input +
                    "\n____________________________________________________________");
        }
    }
}
