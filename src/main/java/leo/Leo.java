package leo;

import leo.exception.LeoException;
import leo.ui.Ui;
import leo.command.Command;
import leo.command.Parser;
import leo.storage.Storage;
import leo.task.TaskList;

public class Leo {
    private Ui ui;
    private Parser parser;
    private Storage storage;
    private TaskList tasks;

    public Leo(String filePath) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(filePath);
        
        try {
            tasks = storage.load();
        } catch (LeoException e) {
            tasks = new TaskList();
            ui.printError("Save file problem. Starting with an empty list.\n" + e.getMessage());
        }
    }

    public void run() {
        ui.printGreeting();
        
        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readCommand();
                Command cmd = parser.parse(input);
                cmd.execute(tasks, storage, ui);
                isExit = cmd.isExit();
            } catch (LeoException e) {
                ui.printError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Leo("data/leo.txt").run();
    }
}
