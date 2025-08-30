import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        final String BAR = "____________________";

        System.out.println(BAR);
        System.out.println("Hello! I'm Leo.");
        System.out.println("What can I do for you?");
        System.out.println(BAR);

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine();
            if (line.equals("bye")) {
                break;
            }
            System.out.println(line);
        }
        sc.close();

        System.out.println(BAR);
        System.out.println("Bye! Hope to see you soon!");
    }
}
