import java.util.Scanner;

public class Leo {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showGreeting();

        Storage storage = new Storage();
        TaskList tasks = new TaskList(storage.load());
        Parser parser = new Parser(ui, tasks, storage);

        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) break; // EOF-safe
            String line = sc.nextLine().trim();
            if (line.equals("bye")) break;
            try {
                parser.handle(line);
            } catch (LeoException e) {
                ui.showBoxLine("OOPS!!! " + e.getMessage());
            }
        }
        sc.close();
        ui.showFarewell();
    }
}
