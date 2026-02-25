package leo.command;

import leo.exception.LeoException;
import leo.storage.Storage;
import leo.task.TaskList;
import leo.ui.Ui;

/**
 * FindCommand searches for tasks containing a keyword in their description.
 * It displays all matching tasks to the user.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified search keyword.
     *
     * @param keyword the keyword to search for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for tasks containing the keyword.
     * Displays all matching tasks to the user.
     *
     * @param tasks the task list to search
     * @param storage the storage (not used for this command)
     * @param ui the UI for displaying results
     * @throws LeoException if the find operation fails
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) throws LeoException {
        ui.printFindResults(keyword, tasks, keyword);
    }
}
