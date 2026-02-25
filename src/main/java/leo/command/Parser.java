package leo.command;

import java.time.LocalDateTime;

import leo.exception.LeoException;
import leo.history.CommandHistory;
import leo.task.TaskList;
import leo.util.DateTimeUtil;

/**
 * Parser parses user input strings and converts them into appropriate Command objects.
 * It handles all supported commands: list, mark, unmark, delete, todo, deadline, event, undo, and bye.
 * The parser validates input format and throws LeoException for invalid commands.
 */
public class Parser {

    private static final int TODO_PREFIX_LENGTH = 4; // Length of "todo"
    private static final int EVENT_PREFIX_LENGTH = 5; // Length of "event"

    private CommandHistory history;

    /**
     * Constructs a new Parser instance.
     */
    public Parser() {
        this.history = new CommandHistory();
    }

    /**
     * Returns the command history for undo functionality.
     *
     * @return the command history
     */
    public CommandHistory getHistory() {
        return history;
    }

    /**
     * Parses user input and returns the appropriate Command object.
     * Handles whitespace trimming and case-insensitive command matching.
     *
     * @param input the raw user input string
     * @return the corresponding Command object
     * @throws LeoException if the input is empty, unknown, or malformed
     */
    public Command parse(String input) throws LeoException {
        String trimmedInput = input.trim();

        if (trimmedInput.isEmpty()) {
            throw new LeoException("Input cannot be empty.");
        }

        String[] parts = trimmedInput.split("\\s+", 2);
        String commandWord = parts[0].toLowerCase();

        switch (commandWord) {
        case "list":
            return new ListCommand();

        case "mark":
            Command markCmd = parseMarkCommand(trimmedInput);
            history.addCommand(markCmd);
            return markCmd;

        case "unmark":
            Command unmarkCmd = parseUnmarkCommand(trimmedInput);
            history.addCommand(unmarkCmd);
            return unmarkCmd;

        case "delete":
            Command deleteCmd = parseDeleteCommand(trimmedInput);
            history.addCommand(deleteCmd);
            return deleteCmd;

        case "todo":
            Command todoCmd = parseTodoCommand(trimmedInput);
            history.addCommand(todoCmd);
            return todoCmd;

        case "deadline":
            Command deadlineCmd = parseDeadlineCommand(trimmedInput);
            history.addCommand(deadlineCmd);
            return deadlineCmd;

        case "event":
            Command eventCmd = parseEventCommand(trimmedInput);
            history.addCommand(eventCmd);
            return eventCmd;

        case "find":
            return parseFindCommand(trimmedInput);

        case "undo":
            return new UndoCommand(history);

        case "bye":
            return new ExitCommand();

        default:
            throw new LeoException("Not a valid command. Please use one of the following commands:\n"
                    + "todo, deadline, event, list, mark, unmark, delete, find, undo, bye");
        }
    }

    /**
     * Parses the mark command input and returns a MarkCommand.
     *
     * @param input the full mark command string
     * @return a MarkCommand with the parsed task index
     * @throws LeoException if the index is missing or invalid
     */
    private Command parseMarkCommand(String input) throws LeoException {
        int index = parseIndex(input, "mark");
        return new MarkCommand(index);
    }

    /**
     * Parses the unmark command input and returns an UnmarkCommand.
     *
     * @param input the full unmark command string
     * @return an UnmarkCommand with the parsed task index
     * @throws LeoException if the index is missing or invalid
     */
    private Command parseUnmarkCommand(String input) throws LeoException {
        int index = parseIndex(input, "unmark");
        return new UnmarkCommand(index);
    }

    /**
     * Parses the delete command input and returns a DeleteCommand.
     *
     * @param input the full delete command string
     * @return a DeleteCommand with the parsed task index
     * @throws LeoException if the index is missing or invalid
     */
    private Command parseDeleteCommand(String input) throws LeoException {
        int index = parseIndex(input, "delete");
        return new DeleteCommand(index);
    }

    /**
     * Parses the todo command input and returns an AddTodoCommand.
     *
     * @param input the full todo command string
     * @return an AddTodoCommand with the parsed description
     * @throws LeoException if the description is missing or empty
     */
    private Command parseTodoCommand(String input) throws LeoException {
        String desc = parseDescription(input, TODO_PREFIX_LENGTH, "todo");
        return new AddTodoCommand(desc);
    }

    /**
     * Parses the deadline command input and returns an AddDeadlineCommand.
     * Extracts the description and deadline from the input.
     *
     * @param input the full deadline command string
     * @return an AddDeadlineCommand with the parsed description and deadline
     * @throws LeoException if the description or deadline is missing
     */
    private Command parseDeadlineCommand(String input) throws LeoException {
        String[] parts = parseDeadlineOrEventInput(input, "deadline", "/by");
        String desc = parts[0];
        LocalDateTime by = DateTimeUtil.parseDateTime(parts[1]);
        return new AddDeadlineCommand(desc, by);
    }

    /**
     * Parses the event command input and returns an AddEventCommand.
     * Extracts the description, start time, and end time from the input.
     *
     * @param input the full event command string
     * @return an AddEventCommand with the parsed description and time range
     * @throws LeoException if any required part is missing
     */
    private Command parseEventCommand(String input) throws LeoException {
        String[] parts = parseEventInput(input);
        String desc = parts[0];
        LocalDateTime from = DateTimeUtil.parseDateTime(parts[1]);
        LocalDateTime to = DateTimeUtil.parseDateTime(parts[2]);
        return new AddEventCommand(desc, from, to);
    }

    /**
     * Parses the find command input and returns a FindCommand.
     *
     * @param input the full find command string
     * @return a FindCommand with the parsed search keyword
     * @throws LeoException if the keyword is missing
     */
    private Command parseFindCommand(String input) throws LeoException {
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            throw new LeoException("Usage: find <keyword>");
        }
        String keyword = parts[1].trim();
        if (keyword.isEmpty()) {
            throw new LeoException("Usage: find <keyword>");
        }
        return new FindCommand(keyword);
    }

    /**
     * Parses the task index from a mark/unmark/delete command.
     * Converts from 1-based user input to 0-based internal index.
     *
     * @param input the full command string
     * @param commandWord the command word for error messages
     * @return the zero-based task index
     * @throws LeoException if the index is missing or not a number
     */
    private int parseIndex(String input, String commandWord) throws LeoException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) {
            throw new LeoException("Usage: " + commandWord + " <task number>");
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new LeoException("Usage: " + commandWord + " <task number>");
        }
    }

    /**
     * Parses the description from a todo command.
     *
     * @param input the full command string
     * @param prefixLength the length of the command prefix to skip
     * @param commandWord the command word for error messages
     * @return the task description
     * @throws LeoException if the description is empty
     */
    private String parseDescription(String input, int prefixLength, String commandWord) throws LeoException {
        boolean hasDescription = input.length() > prefixLength;
        String desc = hasDescription ? input.substring(prefixLength).trim() : "";

        if (desc.isEmpty()) {
            throw new LeoException("The description of a " + commandWord + " cannot be empty.");
        }
        return desc;
    }

    /**
     * Parses the description and deadline from a deadline command.
     *
     * @param input the full command string
     * @param commandWord the command word for error messages
     * @param timeKeyword the keyword separating description from deadline (e.g., "/by")
     * @return a string array with description at index 0 and deadline at index 1
     * @throws LeoException if description or deadline is missing
     */
    private String[] parseDeadlineOrEventInput(String input, String commandWord, String timeKeyword)
            throws LeoException {
        String rest = input.substring(commandWord.length()).trim();
        if (rest.isEmpty()) {
            throw new LeoException("The description of a " + commandWord + " cannot be empty.");
        }
        int timePos = rest.indexOf(timeKeyword);
        if (timePos == -1) {
            throw new LeoException("Usage: " + commandWord + " <description> " + timeKeyword + " <time>");
        }

        String desc = rest.substring(0, timePos).trim();
        String time = rest.substring(timePos + timeKeyword.length()).trim();
        if (desc.isEmpty() || time.isEmpty()) {
            throw new LeoException("Usage: " + commandWord + " <description> " + timeKeyword + " <time>");
        }

        return new String[]{desc, time};
    }

    /**
     * Parses the description, start time, and end time from an event command.
     *
     * @param input the full event command string
     * @return a string array with description, start time, and end time
     * @throws LeoException if any part is missing or malformed
     */
    private String[] parseEventInput(String input) throws LeoException {
        String rest = input.substring(EVENT_PREFIX_LENGTH).trim();

        // Guard clause: check description exists
        if (rest.isEmpty()) {
            throw new LeoException("The description of an event cannot be empty.");
        }

        // Find time markers
        int fromPos = rest.indexOf("/from");
        int toPos = rest.indexOf("/to");

        // Guard clause: validate format
        boolean hasInvalidFormat = fromPos == -1 || toPos == -1 || toPos < fromPos;
        if (hasInvalidFormat) {
            throw new LeoException("Usage: event <description> /from <start> /to <end>");
        }

        // Extract parts
        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + 5, toPos).trim();
        String to = rest.substring(toPos + 3).trim();

        // Guard clause: validate all parts are non-empty
        boolean hasEmptyPart = desc.isEmpty() || from.isEmpty() || to.isEmpty();
        if (hasEmptyPart) {
            throw new LeoException("Usage: event <description> /from <start> /to <end>");
        }

        return new String[]{desc, from, to};
    }
}
