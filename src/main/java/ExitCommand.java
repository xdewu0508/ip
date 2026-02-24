public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        ui.printGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
