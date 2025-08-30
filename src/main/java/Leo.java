import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showGreeting();

        TaskList tasks = new TaskList();
        Parser parser = new Parser(ui, tasks);

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine().trim();
            if (line.equals("bye")) {
                break;
            }
            parser.handle(line);
        }
        sc.close();

        ui.showFarewell();
    }
}
