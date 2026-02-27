package leo.history;

import java.util.ArrayList;
import java.util.List;
import leo.command.Command;

/**
 * CommandHistory tracks the history of executed commands for undo functionality.
 * It maintains a list of executed commands and allows undoing the most recent command.
 */
public class CommandHistory {
    private List<Command> history;
    private int currentIndex;

    /**
     * Constructs a new CommandHistory with empty history.
     */
    public CommandHistory() {
        this.history = new ArrayList<>();
        this.currentIndex = -1;
    }

    /**
     * Adds a command to the history.
     * If we're not at the end of history (after some undos), truncates the history first.
     *
     * @param command the command to add
     */
    public void addCommand(Command command) {
        // If we've undone some commands, remove the "future" history
        if (currentIndex < history.size() - 1) {
            history = new ArrayList<>(history.subList(0, currentIndex + 1));
        }
        history.add(command);
        currentIndex++;
    }

    /**
     * Returns the most recent command that can be undone, or null if no commands to undo.
     *
     * @return the most recent command, or null if history is empty
     */
    public Command getLastCommand() {
        if (currentIndex < 0) {
            return null;
        }
        return history.get(currentIndex);
    }

    /**
     * Moves the current index back by one (undo operation).
     *
     * @return true if undo was possible, false if no commands to undo
     */
    public boolean undo() {
        if (currentIndex >= 0) {
            currentIndex--;
            return true;
        }
        return false;
    }

    /**
     * Returns true if there are commands that can be undone.
     *
     * @return true if can undo, false otherwise
     */
    public boolean canUndo() {
        return currentIndex >= 0;
    }

    /**
     * Returns the number of commands that can be undone.
     *
     * @return the number of undoable commands
     */
    public int getUndoCount() {
        return currentIndex + 1;
    }
}
