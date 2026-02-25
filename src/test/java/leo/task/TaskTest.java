package leo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Task class and its subclasses (Todo, Deadline, Event).
 * Tests cover task state management, string representation, and getters/setters.
 */
public class TaskTest {

    // ==================== Task Base Class Tests ====================

    @Test
    public void constructor_newTask_isNotDone() {
        Task task = new Task("Test task", TaskType.TODO);
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void constructor_newTask_hasCorrectDescription() {
        Task task = new Task("Buy groceries", TaskType.TODO);
        assertEquals("Buy groceries", task.getDescription());
    }

    @Test
    public void constructor_newTask_hasCorrectType() {
        Task task = new Task("Test", TaskType.DEADLINE);
        assertEquals(TaskType.DEADLINE, task.getType());
    }

    @Test
    public void markAsDone_taskBecomesDone() {
        Task task = new Task("Test", TaskType.TODO);
        task.markAsDone();

        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markAsNotDone_taskBecomesNotDone() {
        Task task = new Task("Test", TaskType.TODO);
        task.markAsDone();
        task.markAsNotDone();

        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void setDescription_updatesDescription() {
        Task task = new Task("Original", TaskType.TODO);
        task.setDescription("Updated");

        assertEquals("Updated", task.getDescription());
    }

    @Test
    public void toString_todoFormat_correct() {
        Task task = new Task("Buy milk", TaskType.TODO);
        String expected = "[T][ ] Buy milk";
        assertEquals(expected, task.toString());
    }

    @Test
    public void toString_doneTask_correct() {
        Task task = new Task("Buy milk", TaskType.TODO);
        task.markAsDone();
        String expected = "[T][X] Buy milk";
        assertEquals(expected, task.toString());
    }

    // ==================== Todo Class Tests ====================

    @Test
    public void todo_constructor_hasCorrectType() {
        Todo todo = new Todo("Read book");
        assertEquals(TaskType.TODO, todo.getType());
    }

    @Test
    public void todo_toString_correctFormat() {
        Todo todo = new Todo("Read book");
        assertEquals("[T][ ] Read book", todo.toString());
    }

    @Test
    public void todo_markDone_toStringCorrect() {
        Todo todo = new Todo("Read book");
        todo.markAsDone();
        assertEquals("[T][X] Read book", todo.toString());
    }

    // ==================== Deadline Class Tests ====================

    @Test
    public void deadline_constructor_hasCorrectType() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        assertEquals(TaskType.DEADLINE, deadline.getType());
    }

    @Test
    public void deadline_getBy_returnsCorrectTime() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        assertEquals(by, deadline.getBy());
    }

    @Test
    public void deadline_toString_correctFormat() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        String result = deadline.toString();

        assertTrue(result.startsWith("[D][ ]"));
        assertTrue(result.contains("Finish report"));
        assertTrue(result.contains("by:"));
    }

    @Test
    public void deadline_markDone_statusIconCorrect() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        deadline.markAsDone();

        assertEquals("X", deadline.getStatusIcon());
        assertTrue(deadline.toString().contains("[D][X]"));
    }

    // ==================== Event Class Tests ====================

    @Test
    public void event_constructor_hasCorrectType() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);
        assertEquals(TaskType.EVENT, event.getType());
    }

    @Test
    public void event_getFromAndTo_returnsCorrectTimes() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);

        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
    }

    @Test
    public void event_toString_correctFormat() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);
        String result = event.toString();

        assertTrue(result.startsWith("[E][ ]"));
        assertTrue(result.contains("Team meeting"));
        assertTrue(result.contains("from:"));
        assertTrue(result.contains("to:"));
    }

    @Test
    public void event_markDone_statusIconCorrect() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);
        event.markAsDone();

        assertEquals("X", event.getStatusIcon());
        assertTrue(event.toString().contains("[E][X]"));
    }

    // ==================== TaskType Enum Tests ====================

    @Test
    public void taskType_todo_getSymbol() {
        assertEquals("T", TaskType.TODO.getSymbol());
    }

    @Test
    public void taskType_deadline_getSymbol() {
        assertEquals("D", TaskType.DEADLINE.getSymbol());
    }

    @Test
    public void taskType_event_getSymbol() {
        assertEquals("E", TaskType.EVENT.getSymbol());
    }
}
