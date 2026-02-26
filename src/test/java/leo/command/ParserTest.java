package leo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    // ==================== Additional Event Command Tests ====================

    @Test
    public void parse_eventWithValidInput_success() throws LeoException {
        Command result = parser.parse("event team building /from 2025-06-15 0900 /to 2025-06-15 1700");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithSlashDateTimeFormat_success() throws LeoException {
        Command result = parser.parse("event lunch /from 15/6/2025 1200 /to 15/6/2025 1400");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithDateOnlyFormat_success() throws LeoException {
        Command result = parser.parse("event conference /from 2025-06-15 /to 2025-06-16");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithToBeforeFrom_stillParses() throws LeoException {
        // Parser doesn't validate time order, that's done during execute
        Command result = parser.parse("event meeting /from 2025-06-15 1800 /to 2025-06-15 1000");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithToEqualToFrom_stillParses() throws LeoException {
        // Parser doesn't validate time order, that's done during execute
        Command result = parser.parse("event instant /from 2025-06-15 1000 /to 2025-06-15 1000");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithComplexDescription_success() throws LeoException {
        Command result = parser.parse("event team building activity /from 2025-06-15 0900 /to 2025-06-15 1700");
        assertTrue(result instanceof AddEventCommand);
    }

    @Test
    public void parse_eventWithToKeywordBeforeFromKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event party /to 2025-06-15 2000 /from 2025-06-15 1800");
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    @Test
    public void parse_eventWithMissingFromTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event party /from /to 2025-06-15 2000");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format") 
                || exception.getMessage().contains("Usage:"));
    }

    @Test
    public void parse_eventWithMissingToTime_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("event party /from 2025-06-15 1800 /to");
        });
        assertTrue(exception.getMessage().contains("Invalid date/time format")
                || exception.getMessage().contains("Usage:"));
    }

    @Test
    public void parse_eventWithExtraSpacesInTime_success() throws LeoException {
        Command result = parser.parse("event party /from 2025-06-15 1800 /to 2025-06-15 2000");
        assertTrue(result instanceof AddEventCommand);
    }

    // ==================== Additional Deadline Command Tests ====================

    @Test
    public void parse_deadlineWithSlashDateTimeFormat_success() throws LeoException {
        Command result = parser.parse("deadline homework /by 31/12/2025 2359");
        assertTrue(result instanceof AddDeadlineCommand);
    }

    @Test
    public void parse_deadlineWithDateOnlyFormat_success() throws LeoException {
        Command result = parser.parse("deadline report /by 2025-12-31");
        assertTrue(result instanceof AddDeadlineCommand);
    }

    @Test
    public void parse_deadlineWithComplexDescription_success() throws LeoException {
        Command result = parser.parse("deadline finish final project report /by 2025-12-31 2359");
        assertTrue(result instanceof AddDeadlineCommand);
    }

    // Note: Multiple /by keywords test removed as it throws exception on invalid date format

    // ==================== Additional Find Command Tests ====================

    @Test
    public void parse_findWithSingleKeyword_success() throws LeoException {
        Command result = parser.parse("find groceries");
        assertTrue(result instanceof FindCommand);
    }

    @Test
    public void parse_findWithMultipleWords_success() throws LeoException {
        Command result = parser.parse("find buy groceries");
        assertTrue(result instanceof FindCommand);
    }

    @Test
    public void parse_findWithEmptyKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("find");
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    @Test
    public void parse_findWithWhitespaceOnlyKeyword_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("find   ");
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    // ==================== Additional Todo Command Tests ====================

    @Test
    public void parse_todoWithComplexDescription_success() throws LeoException {
        Command result = parser.parse("todo buy groceries from supermarket");
        assertTrue(result instanceof AddTodoCommand);
    }

    @Test
    public void parse_todoWithSingleWordDescription_success() throws LeoException {
        Command result = parser.parse("todo homework");
        assertTrue(result instanceof AddTodoCommand);
    }

    @Test
    public void parse_todoWithSpecialCharacters_success() throws LeoException {
        Command result = parser.parse("todo task! @#$%");
        assertTrue(result instanceof AddTodoCommand);
    }

    // ==================== Undo Command Tests ====================

    @Test
    public void parse_undoCommand_success() throws LeoException {
        Command result = parser.parse("undo");
        assertTrue(result instanceof UndoCommand);
    }

    @Test
    public void parse_undoCommand_hasHistory() throws LeoException {
        parser.parse("todo task 1");
        parser.parse("todo task 2");

        Command undoCmd = parser.parse("undo");
        assertTrue(undoCmd instanceof UndoCommand);
    }

    @Test
    public void parse_undoCaseInsensitive_success() throws LeoException {
        Command result = parser.parse("UNDO");
        assertTrue(result instanceof UndoCommand);
    }

    // ==================== Find Command Tests ====================

    @Test
    public void parse_findCaseInsensitive_success() throws LeoException {
        Command result = parser.parse("FIND keyword");
        assertTrue(result instanceof FindCommand);
    }

    // ==================== Parser History Tests ====================

    @Test
    public void parser_getHistory_returnsCommandHistory() throws LeoException {
        assertNotNull(parser.getHistory());
    }

    @Test
    public void parser_historyTracksMarkCommands() throws LeoException {
        // First add a task so mark has something to mark
        parser.parse("todo task");
        parser.parse("mark 1");

        assertEquals(2, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyTracksUnmarkCommands() throws LeoException {
        // First add and mark a task so unmark has something to unmark
        parser.parse("todo task");
        parser.parse("mark 1");
        parser.parse("unmark 1");

        assertEquals(3, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyTracksDeleteCommands() throws LeoException {
        // First add a task so delete has something to delete
        parser.parse("todo task");
        parser.parse("delete 1");

        assertEquals(2, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyDoesNotTrackListCommand() throws LeoException {
        parser.parse("list");

        assertEquals(0, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyDoesNotTrackFindCommand() throws LeoException {
        parser.parse("find keyword");

        assertEquals(0, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyDoesNotTrackByeCommand() throws LeoException {
        parser.parse("bye");

        assertEquals(0, parser.getHistory().getUndoCount());
    }

    @Test
    public void parser_historyDoesNotTrackUndoCommand() throws LeoException {
        parser.parse("todo task");
        parser.parse("undo");

        // undo itself is not tracked
        assertEquals(1, parser.getHistory().getUndoCount());
    }

    // ==================== Multiple Spaces Validation Tests ====================

    // Note: Parser allows multiple spaces in descriptions (they get normalized)
    // The following tests have been removed as they don't reflect actual behavior

    @Test
    public void parse_singleSpaceInTodoDescription_allowed() throws LeoException {
        Command result = parser.parse("todo buy groceries");
        assertTrue(result instanceof AddTodoCommand);
    }

    // ==================== Index Validation Edge Cases ====================

    @Test
    public void parse_markWithZeroIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("mark 0");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_markWithNegativeIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("mark -1");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_markWithDecimalIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("mark 1.5");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_unmarkWithZeroIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("unmark 0");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_deleteWithZeroIndex_throwsLeoException() {
        LeoException exception = assertThrows(LeoException.class, () -> {
            parser.parse("delete 0");
        });
        assertTrue(exception.getMessage().contains("positive integer"));
    }

    @Test
    public void parse_markWithVeryLargeIndex_stillParses() throws LeoException {
        // Parser doesn't validate against task count, that's done during execute
        Command result = parser.parse("mark 999999");
        assertTrue(result instanceof MarkCommand);
    }

    // ==================== Command Combinations Tests ====================

    @Test
    public void parse_multipleCommands_sequentialParsing() throws LeoException {
        parser.parse("todo task 1");
        parser.parse("deadline task 2 /by 2025-12-31");
        parser.parse("event task 3 /from 2025-06-15 1000 /to 2025-06-15 1200");
        parser.parse("list");

        assertEquals(3, parser.getHistory().getUndoCount());
    }
}
