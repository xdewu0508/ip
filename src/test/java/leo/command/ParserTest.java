package leo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import leo.exception.LeoException;
import leo.task.Deadline;
import leo.task.Event;
import leo.task.Task;
import leo.task.Todo;

/**
 * Tests for the Parser class.
 * Tests cover valid commands, invalid commands, and edge cases.
 */
public class ParserTest {

    private final Parser parser = new Parser();

    // ==================== Valid Command Tests ====================

    @Test
    public void parse_listCommand_success() throws LeoException {
        Command result = parser.parse("list");
        assertTrue(result instanceof ListCommand);
    }

    @Test
    public void parse_byeCommand_success() throws LeoException {
        Command result = parser.parse("bye");
        assertTrue(result instanceof ExitCommand);
    }

    @Test
    public void parse_todoCommand_success() throws LeoException {
        Command result = parser.parse("todo buy groceries");
        assertTrue(result instanceof AddTodoCommand);
    }

    @Test
    public void parse_deadlineCommand_success() throws LeoException {
        Command result = parser.parse("deadline finish report /by 2025-12-31 2359");
        assertTrue(result instanceof AddDeadlineCommand);
    }

    @Test
    public void parse_eventCommand_success() throws LeoException {
        Command result = parser.parse("event team lunch /from 2025-06-15 1200 /to 2025-06-15 1400");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_markCommand_success() throws LeoException {
        Command result = parser.parse("mark 1");
        assertTrue(result instanceof MarkCommand);
    }

    @Test
    public void parse_unmarkCommand_success() throws LeoException {
        Command result = parser.parse("unmark 3");
        assertTrue(result instanceof UnmarkCommand);
    }

    @Test
    public void parse_deleteCommand_success() throws LeoException {
        Command result = parser.parse("delete 2");
        assertTrue(result instanceof DeleteCommand);
    }

    // ==================== Case Insensitivity Tests ====================

    @Test
    public void parse_commandCaseInsensitive_list() throws LeoException {
        Command result = parser.parse("LIST");
        assertTrue(result instanceof ListCommand);
    }

    @Test
    public void parse_commandCaseInsensitive_todo() throws LeoException {
        Command result = parser.parse("TODO homework");
        assertTrue(result instanceof AddTodoCommand);
    }

    @Test
    public void parse_commandCaseInsensitive_bye() throws LeoException {
        Command result = parser.parse("BYE");
        assertTrue(result instanceof ExitCommand);
    }

    // ==================== Input Validation Tests ====================

    @Test
    public void parse_emptyInput_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("");
        });
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void parse_whitespaceOnlyInput_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("   ");
        });
        assertTrue(exception.getMessage().contains("empty"));
    }

    @Test
    public void parse_unknownCommand_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("invalid");
        });
        assertTrue(exception.getMessage().contains("Not a valid command"));
    }

    // ==================== Todo Command Tests ====================

    @Test
    public void parse_todoWithEmptyDescription_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("todo");
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    public void parse_todoWithOnlySpacesDescription_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("todo   ");
        });
        assertTrue(exception.getMessage().contains("cannot be empty"));
    }

    @Test
    public void parse_todoWithValidDescription_success() throws LeoException {
        Command result = parser.parse("todo read book");
        assertTrue(result instanceof AddTodoCommand);
    }

    // ==================== Deadline Command Tests ====================

    @Test
    public void parse_deadlineWithoutByKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("deadline homework");
        });
        assertTrue(exception.getMessage().contains("/by"));
    }

    @Test
    public void parse_deadlineWithEmptyDescription_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("deadline /by 2025-12-31");
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    @Test
    public void parse_deadlineWithEmptyTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("deadline homework /by");
        });
        assertTrue(exception.getMessage().contains("/by"));
    }

    // ==================== Event Command Tests ====================

    @Test
    public void parse_eventWithoutFromKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event party /to 2025-06-15 1800");
        });
        assertTrue(exception.getMessage().contains("/from"));
    }

    @Test
    public void parse_eventWithoutToKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event party /from 2025-06-15 1800");
        });
        assertTrue(exception.getMessage().contains("/to"));
    }

    @Test
    public void parse_eventWithEmptyDescription_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event /from 2025-06-15 1800 /to 2025-06-15 2000");
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    // ==================== Mark/Unmark/Delete Index Tests ====================

    @Test
    public void parse_markWithoutIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("mark");
        });
        assertTrue(exception.getMessage().contains("<task number>"));
    }

    @Test
    public void parse_markWithInvalidIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("mark abc");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_unmarkWithoutIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("unmark");
        });
        assertTrue(exception.getMessage().contains("<task number>"));
    }

    @Test
    public void parse_deleteWithoutIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("delete");
        });
        assertTrue(exception.getMessage().contains("<task number>"));
    }

    // ==================== Whitespace Handling Tests ====================

    @Test
    public void parse_commandWithLeadingWhitespace_success() throws LeoException {
        Command result = parser.parse("   list");
        assertTrue(result instanceof ListCommand);
    }

    @Test
    public void parse_commandWithTrailingWhitespace_success() throws LeoException {
        Command result = parser.parse("bye   ");
        assertTrue(result instanceof ExitCommand);
    }

    @Test
    public void parse_commandWithExtraSpaces_success() throws LeoException {
        Command result = parser.parse("todo    buy    milk");
        assertTrue(result instanceof AddTodoCommand);
    }
}
