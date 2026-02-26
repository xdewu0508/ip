package leo.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leo.exception.LeoException;
import leo.history.CommandHistory;
import leo.storage.Storage;
import leo.task.Deadline;
import leo.task.Event;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Todo;
import leo.ui.Ui;

/**
 * Tests for Command execution functionality.
 * Tests cover all command types: ListCommand, ExitCommand, AddTodoCommand,
 * AddDeadlineCommand, AddEventCommand, MarkCommand, UnmarkCommand,
 * DeleteCommand, FindCommand, and UndoCommand.
 */
public class CommandExecutorTest {

    private TaskList tasks;
    private TestStorage storage;
    private TestUi ui;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        storage = new TestStorage();
        ui = new TestUi();
    }

    // ==================== ListCommand Tests ====================

    @Test
    public void execute_listCommand_emptyList_success() throws LeoException {
        Command cmd = new ListCommand();
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("Here are the tasks in your list:"));
        assertFalse(storage.isSaved);
    }

    @Test
    public void execute_listCommand_withTasks_success() throws LeoException {
        tasks.add(new Todo("Task 1"));
        tasks.add(new Todo("Task 2"));

        Command cmd = new ListCommand();
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("Here are the tasks in your list:"));
        assertTrue(ui.output.contains("1.[T][ ] Task 1"));
        assertTrue(ui.output.contains("2.[T][ ] Task 2"));
    }

    // ==================== ExitCommand Tests ====================

    @Test
    public void execute_exitCommand_printsGoodbye() throws LeoException {
        Command cmd = new ExitCommand();
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("Bye. Hope to see you again soon!"));
    }

    @Test
    public void exitCommand_isExit_returnsTrue() {
        Command cmd = new ExitCommand();
        assertTrue(cmd.isExit());
    }

    @Test
    public void otherCommands_isExit_returnsFalse() {
        assertFalse(new ListCommand().isExit());
        assertFalse(new AddTodoCommand("Test").isExit());
        assertFalse(new MarkCommand(1).isExit());
        assertFalse(new UnmarkCommand(1).isExit());
        assertFalse(new DeleteCommand(1).isExit());
        assertFalse(new FindCommand("test").isExit());
    }

    // ==================== AddTodoCommand Tests ====================

    @Test
    public void execute_addTodoCommand_success() throws LeoException {
        Command cmd = new AddTodoCommand("Buy groceries");
        cmd.execute(tasks, storage, ui);

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Todo);
        assertEquals("Buy groceries", tasks.get(0).getDescription());
        assertTrue(storage.isSaved);
        assertTrue(ui.output.contains("Got it. I've added this task:"));
        assertTrue(ui.output.contains("Now you have 1 tasks in the list."));
    }

    @Test
    public void execute_addTodoCommand_duplicate_throwsLeoException() throws LeoException {
        tasks.add(new Todo("Buy groceries"));

        Command cmd = new AddTodoCommand("Buy groceries");
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("already exists"));
    }

    // ==================== AddDeadlineCommand Tests ====================

    @Test
    public void execute_addDeadlineCommand_success() throws LeoException {
        LocalDateTime by = LocalDateTime.of(2025, 12, 31, 23, 59);
        Command cmd = new AddDeadlineCommand("Finish report", by);
        cmd.execute(tasks, storage, ui);

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
        Deadline deadline = (Deadline) tasks.get(0);
        assertEquals("Finish report", deadline.getDescription());
        assertEquals(by, deadline.getBy());
        assertTrue(storage.isSaved);
    }

    @Test
    public void execute_addDeadlineCommand_doneStatus_preserved() throws LeoException {
        LocalDateTime by = LocalDateTime.of(2025, 12, 31, 23, 59);
        Command cmd = new AddDeadlineCommand("Finish report", by);
        cmd.execute(tasks, storage, ui);

        assertFalse(tasks.get(0).isDone());
    }

    // ==================== AddEventCommand Tests ====================

    @Test
    public void execute_addEventCommand_success() throws LeoException {
        LocalDateTime from = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 6, 15, 12, 0);
        Command cmd = new AddEventCommand("Team meeting", from, to);
        cmd.execute(tasks, storage, ui);

        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Event);
        Event event = (Event) tasks.get(0);
        assertEquals("Team meeting", event.getDescription());
        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
        assertTrue(storage.isSaved);
    }

    @Test
    public void execute_addEventCommand_endTimeBeforeStart_throwsLeoException() {
        LocalDateTime from = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 6, 15, 9, 0); // Before start
        Command cmd = new AddEventCommand("Invalid event", from, to);

        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("end time must be after start"));
    }

    @Test
    public void execute_addEventCommand_endTimeEqualToStart_throwsLeoException() {
        LocalDateTime from = LocalDateTime.of(2025, 6, 15, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 6, 15, 10, 0); // Same as start
        Command cmd = new AddEventCommand("Invalid event", from, to);

        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("end time must be after start"));
    }

    // ==================== MarkCommand Tests ====================

    @Test
    public void execute_markCommand_success() throws LeoException {
        tasks.add(new Todo("Task to mark"));

        Command cmd = new MarkCommand(0);
        cmd.execute(tasks, storage, ui);

        assertTrue(tasks.get(0).isDone());
        assertTrue(storage.isSaved);
        assertTrue(ui.output.contains("Nice! I've marked this task as done:"));
    }

    @Test
    public void execute_markCommand_emptyList_throwsLeoException() {
        Command cmd = new MarkCommand(0);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("no tasks"));
    }

    @Test
    public void execute_markCommand_invalidIndex_throwsLeoException() throws LeoException {
        tasks.add(new Todo("Only task"));

        Command cmd = new MarkCommand(5);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("valid task number"));
    }

    @Test
    public void execute_markCommand_negativeIndex_throwsLeoException() throws LeoException {
        tasks.add(new Todo("Task"));

        Command cmd = new MarkCommand(-1);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("valid task number"));
    }

    // ==================== UnmarkCommand Tests ====================

    @Test
    public void execute_unmarkCommand_success() throws LeoException {
        Todo todo = new Todo("Task to unmark");
        todo.markAsDone();
        tasks.add(todo);

        Command cmd = new UnmarkCommand(0);
        cmd.execute(tasks, storage, ui);

        assertFalse(tasks.get(0).isDone());
        assertTrue(storage.isSaved);
        assertTrue(ui.output.contains("OK, I've marked this task as not done"));
    }

    @Test
    public void execute_unmarkCommand_emptyList_throwsLeoException() {
        Command cmd = new UnmarkCommand(0);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("no tasks"));
    }

    @Test
    public void execute_unmarkCommand_invalidIndex_throwsLeoException() throws LeoException {
        tasks.add(new Todo("Only task"));

        Command cmd = new UnmarkCommand(5);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("valid task number"));
    }

    // ==================== DeleteCommand Tests ====================

    @Test
    public void execute_deleteCommand_success() throws LeoException {
        tasks.add(new Todo("Task to delete"));
        tasks.add(new Todo("Task to keep"));

        Command cmd = new DeleteCommand(0);
        cmd.execute(tasks, storage, ui);

        assertEquals(1, tasks.size());
        assertEquals("Task to keep", tasks.get(0).getDescription());
        assertTrue(storage.isSaved);
        assertTrue(ui.output.contains("Noted. I've removed this task:"));
        assertTrue(ui.output.contains("Now you have 1 tasks in the list."));
    }

    @Test
    public void execute_deleteCommand_lastTask_success() throws LeoException {
        tasks.add(new Todo("Only task"));

        Command cmd = new DeleteCommand(0);
        cmd.execute(tasks, storage, ui);

        assertEquals(0, tasks.size());
        assertTrue(tasks.isEmpty());
        assertTrue(storage.isSaved);
    }

    @Test
    public void execute_deleteCommand_emptyList_throwsLeoException() {
        Command cmd = new DeleteCommand(0);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("no tasks"));
    }

    @Test
    public void execute_deleteCommand_invalidIndex_throwsLeoException() throws LeoException {
        tasks.add(new Todo("Only task"));

        Command cmd = new DeleteCommand(5);
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("valid task number"));
    }

    // ==================== FindCommand Tests ====================

    @Test
    public void execute_findCommand_matchingTasks_success() throws LeoException {
        tasks.add(new Todo("Buy groceries"));
        tasks.add(new Todo("Read book"));
        tasks.add(new Todo("Buy milk"));

        Command cmd = new FindCommand("buy");
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("Here are the matching tasks"));
        assertTrue(ui.output.contains("1.[T][ ] Buy groceries"));
        assertTrue(ui.output.contains("2.[T][ ] Buy milk"));
        assertFalse(storage.isSaved);
    }

    @Test
    public void execute_findCommand_noMatchingTasks_success() throws LeoException {
        tasks.add(new Todo("Buy groceries"));
        tasks.add(new Todo("Read book"));

        Command cmd = new FindCommand("xyz");
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("Here are the matching tasks"));
        assertTrue(ui.output.contains("No tasks found containing"));
    }

    @Test
    public void execute_findCommand_emptyList_success() throws LeoException {
        Command cmd = new FindCommand("test");
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("No tasks found containing"));
    }

    @Test
    public void execute_findCommand_caseInsensitive_success() throws LeoException {
        tasks.add(new Todo("BUY GROCERIES"));
        tasks.add(new Todo("buy milk"));

        Command cmd = new FindCommand("Buy");
        cmd.execute(tasks, storage, ui);

        assertTrue(ui.output.contains("1.[T][ ] BUY GROCERIES"));
        assertTrue(ui.output.contains("2.[T][ ] buy milk"));
    }

    // ==================== UndoCommand Tests ====================

    @Test
    public void execute_undoCommand_emptyHistory_throwsLeoException() {
        Command cmd = new UndoCommand(new CommandHistory());
        LeoException exception = assertThrows(LeoException.class, () -> {
            cmd.execute(tasks, storage, ui);
        });
        assertTrue(exception.getMessage().contains("Nothing to undo"));
    }

    @Test
    public void execute_undoCommand_addTodo_success() throws LeoException {
        CommandHistory history = new CommandHistory();
        Command addCmd = new AddTodoCommand("Test task");
        history.addCommand(addCmd);

        // First execute the add
        addCmd.execute(tasks, storage, ui);
        assertEquals(1, tasks.size());

        // Then undo
        Command undoCmd = new UndoCommand(history);
        ui.output = ""; // Clear output
        undoCmd.execute(tasks, storage, ui);

        assertEquals(0, tasks.size());
        assertTrue(ui.output.contains("Undone add command"));
    }

    @Test
    public void execute_undoCommand_mark_success() throws LeoException {
        CommandHistory history = new CommandHistory();
        tasks.add(new Todo("Test task"));

        Command markCmd = new MarkCommand(0);
        history.addCommand(markCmd);
        markCmd.execute(tasks, storage, ui);
        assertTrue(tasks.get(0).isDone());

        Command undoCmd = new UndoCommand(history);
        ui.output = "";
        undoCmd.execute(tasks, storage, ui);

        assertFalse(tasks.get(0).isDone());
        assertTrue(ui.output.contains("Undone mark command"));
    }

    @Test
    public void execute_undoCommand_unmark_success() throws LeoException {
        CommandHistory history = new CommandHistory();
        Todo todo = new Todo("Test task");
        todo.markAsDone();
        tasks.add(todo);

        Command unmarkCmd = new UnmarkCommand(0);
        history.addCommand(unmarkCmd);
        unmarkCmd.execute(tasks, storage, ui);
        assertFalse(tasks.get(0).isDone());

        Command undoCmd = new UndoCommand(history);
        ui.output = "";
        undoCmd.execute(tasks, storage, ui);

        assertTrue(tasks.get(0).isDone());
        assertTrue(ui.output.contains("Undone unmark command"));
    }

    @Test
    public void execute_undoCommand_delete_partialSuccess() throws LeoException {
        CommandHistory history = new CommandHistory();
        tasks.add(new Todo("Task to delete"));

        Command deleteCmd = new DeleteCommand(0);
        history.addCommand(deleteCmd);
        deleteCmd.execute(tasks, storage, ui);
        assertEquals(0, tasks.size());

        Command undoCmd = new UndoCommand(history);
        ui.output = "";
        undoCmd.execute(tasks, storage, ui);

        // Delete cannot fully restore the task
        assertTrue(ui.output.contains("cannot be fully undone"));
    }

    // ==================== Mixed Command Execution Tests ====================

    @Test
    public void mixedCommands_multipleOperations_success() throws LeoException {
        // Add tasks
        new AddTodoCommand("Task 1").execute(tasks, storage, ui);
        new AddTodoCommand("Task 2").execute(tasks, storage, ui);

        assertEquals(2, tasks.size());

        // Mark first task
        new MarkCommand(0).execute(tasks, storage, ui);
        assertTrue(tasks.get(0).isDone());

        // List tasks
        ui.output = "";
        new ListCommand().execute(tasks, storage, ui);
        assertTrue(ui.output.contains("Task 1"));
        assertTrue(ui.output.contains("Task 2"));

        // Delete second task
        new DeleteCommand(1).execute(tasks, storage, ui);
        assertEquals(1, tasks.size());

        // Find tasks
        ui.output = "";
        new FindCommand("Task").execute(tasks, storage, ui);
        assertTrue(ui.output.contains("Task 1"));
    }

    // ==================== Test Helper Classes ====================

    /**
     * TestStorage is a test double for Storage that tracks save calls.
     */
    private static class TestStorage extends Storage {
        boolean isSaved = false;

        public TestStorage() {
            super("data/test.txt");
        }

        @Override
        public void save(TaskList tasks) {
            isSaved = true;
        }
    }

    /**
     * TestUi is a test double for Ui that captures output.
     */
    private static class TestUi extends Ui {
        String output = "";

        @Override
        public void printLine() {
            output += "____________________________________________________________\n";
        }

        @Override
        public void printGreeting() {
            output += "Hello! I'm Leo\n";
        }

        @Override
        public void printGoodbye() {
            output += "Bye. Hope to see you again soon!\n";
        }

        @Override
        public void printList(java.util.ArrayList<Task> tasks) {
            output += "Here are the tasks in your list:\n";
            for (int i = 0; i < tasks.size(); i++) {
                output += (i + 1) + "." + tasks.get(i) + "\n";
            }
        }

        @Override
        public void printAddedTask(Task task, int taskCount) {
            output += "Got it. I've added this task:\n";
            output += "  " + task + "\n";
            output += "Now you have " + taskCount + " tasks in the list.\n";
        }

        @Override
        public void printDeletedTask(Task task, int taskCount) {
            output += "Noted. I've removed this task:\n";
            output += "  " + task + "\n";
            output += "Now you have " + taskCount + " tasks in the list.\n";
        }

        @Override
        public void printMarkedTask(Task task) {
            output += "Nice! I've marked this task as done:\n";
            output += "  " + task + "\n";
        }

        @Override
        public void printUnmarkedTask(Task task) {
            output += "OK, I've marked this task as not done yet:\n";
            output += "  " + task + "\n";
        }

        @Override
        public void printError(String message) {
            output += message + "\n";
        }

        @Override
        public void printFindResults(String keyword, TaskList tasks, String searchKeyword) {
            output += "Here are the matching tasks in your list:\n";
            java.util.ArrayList<Task> allTasks = tasks.getAll();
            int matchCount = 0;
            for (Task task : allTasks) {
                if (task.getDescription().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    matchCount++;
                    output += matchCount + "." + task + "\n";
                }
            }
            if (matchCount == 0) {
                output += "No tasks found containing \"" + keyword + "\".\n";
            }
        }

        @Override
        public void printUndo(String message) {
            output += message + "\n";
        }
    }
}
