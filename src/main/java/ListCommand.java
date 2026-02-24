public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        ui.printList(tasks.getAll());
    }
}
