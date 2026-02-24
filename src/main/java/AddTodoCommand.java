public class AddTodoCommand extends Command {
    private final String description;

    public AddTodoCommand(String description) {
        this.description = description;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        Task t = new Todo(description);
        tasks.add(t);
        storage.save(tasks);
        ui.printAddedTask(t, tasks.size());
    }
}
