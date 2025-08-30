public class Ui {
    private static final String BAR = "____________________________________________________________";

    public void showGreeting() {
        System.out.println(BAR);
        System.out.println("Hello! I'm Leo.");
        System.out.println("What can I do for you?");
        System.out.println(BAR);
    }

    public void showFarewell() {
        System.out.println(BAR);
        System.out.println("Bye! Hope to see you soon!");
    }

    public void showBox(String... lines) {
        System.out.println(BAR);
        for (String line : lines) {
            System.out.println(" " + line);
        }
        System.out.println(BAR);
    }

    public void showBoxLine(String line) {
        showBox(line);
    }
}
