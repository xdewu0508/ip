package leo.command;

import java.time.LocalDateTime;

import leo.exception.LeoException;
import leo.task.TaskList;
import leo.util.DateTimeUtil;

public class Parser {

    public Parser() {
    }

    /**
     * Parses user input and returns the appropriate Command object.
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
            return parseMarkCommand(trimmedInput);

        case "unmark":
            return parseUnmarkCommand(trimmedInput);

        case "delete":
            return parseDeleteCommand(trimmedInput);

        case "todo":
            return parseTodoCommand(trimmedInput);

        case "deadline":
            return parseDeadlineCommand(trimmedInput);

        case "event":
            return parseEventCommand(trimmedInput);

        case "bye":
            return new ExitCommand();

        default:
            throw new LeoException("Not a valid command. Please use one of the following commands:\n" +
                    "todo, deadline, event, list, mark, unmark, delete, bye");
        }
    }

    private Command parseMarkCommand(String input) throws LeoException {
        int index = parseIndex(input, "mark");
        return new MarkCommand(index);
    }

    private Command parseUnmarkCommand(String input) throws LeoException {
        int index = parseIndex(input, "unmark");
        return new UnmarkCommand(index);
    }

    private Command parseDeleteCommand(String input) throws LeoException {
        int index = parseIndex(input, "delete");
        return new DeleteCommand(index);
    }

    private Command parseTodoCommand(String input) throws LeoException {
        String desc = parseDescription(input, 4, "todo");
        return new AddTodoCommand(desc);
    }

    private Command parseDeadlineCommand(String input) throws LeoException {
        String[] parts = parseDeadlineOrEventInput(input, "deadline", "/by");
        String desc = parts[0];
        LocalDateTime by = DateTimeUtil.parseDateTime(parts[1]);
        return new AddDeadlineCommand(desc, by);
    }

    private Command parseEventCommand(String input) throws LeoException {
        String[] parts = parseEventInput(input);
        String desc = parts[0];
        LocalDateTime from = DateTimeUtil.parseDateTime(parts[1]);
        LocalDateTime to = DateTimeUtil.parseDateTime(parts[2]);
        return new AddEventCommand(desc, from, to);
    }

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

    private String parseDescription(String input, int prefixLength, String commandWord) throws LeoException {
        String desc = input.length() > prefixLength ? input.substring(prefixLength).trim() : "";
        if (desc.isEmpty()) {
            throw new LeoException("The description of a " + commandWord + " cannot be empty.");
        }
        return desc;
    }

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

    private String[] parseEventInput(String input) throws LeoException {
        String rest = input.substring(5).trim();
        if (rest.isEmpty()) {
            throw new LeoException("The description of an event cannot be empty.");
        }

        int fromPos = rest.indexOf("/from");
        int toPos = rest.indexOf("/to");
        if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
            throw new LeoException("Usage: event <description> /from <start> /to <end>");
        }

        String desc = rest.substring(0, fromPos).trim();
        String from = rest.substring(fromPos + 5, toPos).trim();
        String to = rest.substring(toPos + 3).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new LeoException("Usage: event <description> /from <start> /to <end>");
        }

        return new String[]{desc, from, to};
    }
}
