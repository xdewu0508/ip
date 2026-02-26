package leo.history;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leo.command.AddTodoCommand;
import leo.command.Command;
import leo.command.MarkCommand;
import leo.command.UnmarkCommand;

/**
 * Tests for the CommandHistory class.
 * Tests cover all public methods: addCommand, getLastCommand, undo, canUndo, getUndoCount.
 */
public class CommandHistoryTest {

    private CommandHistory history;

    @BeforeEach
    public void setUp() {
        history = new CommandHistory();
    }

    // ==================== Constructor Tests ====================

    @Test
    public void constructor_newHistory_isEmpty() {
        assertFalse(history.canUndo());
        assertEquals(0, history.getUndoCount());
        assertNull(history.getLastCommand());
    }

    // ==================== AddCommand Tests ====================

    @Test
    public void addCommand_singleCommand_canUndo() {
        Command cmd = new AddTodoCommand("Test task");
        history.addCommand(cmd);

        assertTrue(history.canUndo());
        assertEquals(1, history.getUndoCount());
        assertEquals(cmd, history.getLastCommand());
    }

    @Test
    public void addCommand_multipleCommands_allAccessible() {
        Command cmd1 = new AddTodoCommand("Task 1");
        Command cmd2 = new MarkCommand(1);
        Command cmd3 = new UnmarkCommand(2);

        history.addCommand(cmd1);
        history.addCommand(cmd2);
        history.addCommand(cmd3);

        assertEquals(3, history.getUndoCount());
        assertEquals(cmd3, history.getLastCommand());
        assertTrue(history.canUndo());
    }

    @Test
    public void addCommand_nullCommand_added() {
        // Note: This tests that null can be added (may be a design choice)
        history.addCommand(null);
        assertEquals(1, history.getUndoCount());
        assertNull(history.getLastCommand());
    }

    // ==================== CanUndo Tests ====================

    @Test
    public void canUndo_emptyHistory_returnsFalse() {
        assertFalse(history.canUndo());
    }

    @Test
    public void canUndo_afterAddingCommand_returnsTrue() {
        history.addCommand(new AddTodoCommand("Test"));
        assertTrue(history.canUndo());
    }

    @Test
    public void canUndo_afterAllUndone_returnsFalse() {
        history.addCommand(new AddTodoCommand("Test"));
        history.undo();
        assertFalse(history.canUndo());
    }

    // ==================== GetUndoCount Tests ====================

    @Test
    public void getUndoCount_emptyHistory_returnsZero() {
        assertEquals(0, history.getUndoCount());
    }

    @Test
    public void getUndoCount_afterAddingCommands_returnsCorrectCount() {
        history.addCommand(new AddTodoCommand("Task 1"));
        assertEquals(1, history.getUndoCount());

        history.addCommand(new MarkCommand(1));
        assertEquals(2, history.getUndoCount());

        history.addCommand(new UnmarkCommand(1));
        assertEquals(3, history.getUndoCount());
    }

    @Test
    public void getUndoCount_afterUndo_decrements() {
        history.addCommand(new AddTodoCommand("Task 1"));
        history.addCommand(new MarkCommand(1));

        history.undo();
        assertEquals(1, history.getUndoCount());

        history.undo();
        assertEquals(0, history.getUndoCount());
    }

    // ==================== GetLastCommand Tests ====================

    @Test
    public void getLastCommand_emptyHistory_returnsNull() {
        assertNull(history.getLastCommand());
    }

    @Test
    public void getLastCommand_withCommands_returnsMostRecent() {
        Command cmd1 = new AddTodoCommand("Task 1");
        Command cmd2 = new MarkCommand(1);

        history.addCommand(cmd1);
        assertEquals(cmd1, history.getLastCommand());

        history.addCommand(cmd2);
        assertEquals(cmd2, history.getLastCommand());
    }

    @Test
    public void getLastCommand_afterUndo_returnsPreviousCommand() {
        Command cmd1 = new AddTodoCommand("Task 1");
        Command cmd2 = new MarkCommand(1);

        history.addCommand(cmd1);
        history.addCommand(cmd2);

        history.undo();
        assertEquals(cmd1, history.getLastCommand());
    }

    // ==================== Undo Tests ====================

    @Test
    public void undo_emptyHistory_returnsFalse() {
        assertFalse(history.undo());
    }

    @Test
    public void undo_withCommands_returnsTrue() {
        history.addCommand(new AddTodoCommand("Test"));
        assertTrue(history.undo());
    }

    @Test
    public void undo_multipleUndos_success() {
        history.addCommand(new AddTodoCommand("Task 1"));
        history.addCommand(new MarkCommand(1));
        history.addCommand(new UnmarkCommand(1));

        assertTrue(history.undo());
        assertEquals(2, history.getUndoCount());

        assertTrue(history.undo());
        assertEquals(1, history.getUndoCount());

        assertTrue(history.undo());
        assertEquals(0, history.getUndoCount());

        assertFalse(history.undo()); // No more to undo
    }

    @Test
    public void undo_exhaustedHistory_returnsFalse() {
        history.addCommand(new AddTodoCommand("Test"));
        history.undo();
        assertFalse(history.undo());
    }

    // ==================== Undo with Redo Scenario Tests ====================

    @Test
    public void addCommand_afterUndo_truncatesFutureHistory() {
        Command cmd1 = new AddTodoCommand("Task 1");
        Command cmd2 = new MarkCommand(1);
        Command cmd3 = new UnmarkCommand(1);

        history.addCommand(cmd1);
        history.addCommand(cmd2);
        history.addCommand(cmd3);

        // Undo twice
        history.undo();
        history.undo();

        // Now current index is at cmd1, cmd2 and cmd3 are in "future"
        assertEquals(1, history.getUndoCount());
        assertEquals(cmd1, history.getLastCommand());

        // Add new command - should truncate cmd2 and cmd3
        Command cmd4 = new AddTodoCommand("Task 2");
        history.addCommand(cmd4);

        assertEquals(2, history.getUndoCount());
        assertEquals(cmd4, history.getLastCommand());

        // Verify cmd2 and cmd3 are no longer accessible
        history.undo();
        assertEquals(cmd1, history.getLastCommand());
        history.undo();
        assertNull(history.getLastCommand());
    }

    @Test
    public void addCommand_afterPartialUndo_correctHistory() {
        Command cmd1 = new AddTodoCommand("Task 1");
        Command cmd2 = new MarkCommand(1);
        Command cmd3 = new UnmarkCommand(1);
        Command cmd4 = new AddTodoCommand("Task 2");

        history.addCommand(cmd1);
        history.addCommand(cmd2);
        history.addCommand(cmd3);

        history.undo(); // Undo cmd3
        history.addCommand(cmd4); // Add cmd4

        assertEquals(3, history.getUndoCount());
        assertEquals(cmd4, history.getLastCommand());

        history.undo();
        assertEquals(cmd2, history.getLastCommand());

        history.undo();
        assertEquals(cmd1, history.getLastCommand());

        history.undo();
        assertFalse(history.canUndo());
    }

    // ==================== Complex Scenario Tests ====================

    @Test
    public void complexScenario_multipleAddUndoCycles() {
        // First cycle
        history.addCommand(new AddTodoCommand("Task 1"));
        history.addCommand(new MarkCommand(1));
        history.undo();
        history.undo();

        assertEquals(0, history.getUndoCount());

        // Second cycle
        history.addCommand(new AddTodoCommand("Task 2"));
        history.addCommand(new UnmarkCommand(1));
        history.addCommand(new MarkCommand(1));

        assertEquals(3, history.getUndoCount());

        // Undo all
        history.undo();
        history.undo();
        history.undo();

        assertEquals(0, history.getUndoCount());
    }

    @Test
    public void complexScenario_interleavedAddUndo() {
        history.addCommand(new AddTodoCommand("Task 1")); // 1
        history.addCommand(new MarkCommand(1)); // 2
        history.undo(); // back to 1
        history.addCommand(new UnmarkCommand(1)); // replaces mark, now 2
        history.addCommand(new MarkCommand(1)); // 3

        assertEquals(3, history.getUndoCount());

        // Verify the sequence using instanceof since each command is a new instance
        Command lastCmd = history.getLastCommand();
        assertTrue(lastCmd instanceof MarkCommand);
        
        history.undo();
        Command prevCmd = history.getLastCommand();
        assertTrue(prevCmd instanceof UnmarkCommand);
        
        history.undo();
        Command firstCmd = history.getLastCommand();
        assertTrue(firstCmd instanceof AddTodoCommand);
    }
}
