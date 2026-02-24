public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        validateIndex(index, tasks.size(), "mark");
        tasks.markAsDone(index);
        storage.save(tasks);
        ui.printMarkedTask(tasks.get(index));
    }

    private void validateIndex(int index, int taskCount, String commandWord) throws LeoException {
        if (taskCount == 0) {
            throw new LeoException("There are no tasks to " + commandWord + ".");
        }
        if (index < 0 || index >= taskCount) {
            throw new LeoException("Please give a valid task number to " + commandWord + ".");
        }
    }
}
