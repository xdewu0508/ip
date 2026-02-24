public abstract class Command {
    /**
     * Executes the command.
     */
    public abstract void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException;

    /**
     * Returns true if this command should exit the application.
     */
    public boolean isExit() {
        return false;
    }
}
