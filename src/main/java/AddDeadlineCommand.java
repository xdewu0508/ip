import java.time.LocalDateTime;

public class AddDeadlineCommand extends Command {
    private final String description;
    private final LocalDateTime by;

    public AddDeadlineCommand(String description, LocalDateTime by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        Task d = new Deadline(description, by);
        tasks.add(d);
        storage.save(tasks);
        ui.printAddedTask(d, tasks.size());
    }
}
