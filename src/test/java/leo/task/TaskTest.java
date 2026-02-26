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

    // ==================== Additional Task Edge Cases ====================

    @Test
    public void constructor_taskWithSpecialCharacters_success() {
        Task task = new Task("Task with !@#$%^&*() symbols", TaskType.TODO);
        assertEquals("Task with !@#$%^&*() symbols", task.getDescription());
    }

    @Test
    public void constructor_taskWithUnicodeCharacters_success() {
        Task task = new Task("任务管理 📝 émojis", TaskType.TODO);
        assertEquals("任务管理 📝 émojis", task.getDescription());
    }

    @Test
    public void constructor_taskWithLongDescription_success() {
        String longDesc = "This is a very long description that goes on and on to test how the Task class handles lengthy input";
        Task task = new Task(longDesc, TaskType.TODO);
        assertEquals(longDesc, task.getDescription());
    }

    @Test
    public void constructor_taskWithWhitespaceInDescription_preserved() {
        Task task = new Task("Task with   multiple   spaces", TaskType.TODO);
        assertEquals("Task with   multiple   spaces", task.getDescription());
    }

    @Test
    public void markAsDone_markMultipleTimes_stillDone() {
        Task task = new Task("Test", TaskType.TODO);
        task.markAsDone();
        task.markAsDone(); // Mark again
        task.markAsDone(); // And again
        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());
    }

    @Test
    public void markAsNotDone_markMultipleTimes_stillNotDone() {
        Task task = new Task("Test", TaskType.TODO);
        task.markAsDone();
        task.markAsNotDone();
        task.markAsNotDone(); // Mark again
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void markToggle_toggleMultipleTimes_correctState() {
        Task task = new Task("Test", TaskType.TODO);

        task.markAsDone();
        assertTrue(task.isDone());

        task.markAsNotDone();
        assertFalse(task.isDone());

        task.markAsDone();
        assertTrue(task.isDone());

        task.markAsNotDone();
        assertFalse(task.isDone());
    }

    @Test
    public void setDescription_setToSameValue_success() {
        Task task = new Task("Original", TaskType.TODO);
        task.setDescription("Original");
        assertEquals("Original", task.getDescription());
    }

    // Note: setDescription with empty string throws AssertionError (by design)
    // Test removed as it violates the assertion

    @Test
    public void toString_multipleTasks_correctFormat() {
        Task task1 = new Task("Task 1", TaskType.TODO);
        Task task2 = new Task("Task 2", TaskType.TODO);

        assertEquals("[T][ ] Task 1", task1.toString());
        assertEquals("[T][ ] Task 2", task2.toString());
    }

    // ==================== Deadline Additional Tests ====================

    @Test
    public void deadline_toString_midnightTime_dateOnlyFormat() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 0, 0);
        Deadline deadline = new Deadline("Finish report", by);
        String result = deadline.toString();

        assertTrue(result.contains("by: Dec 31 2025"));
        assertFalse(result.contains("AM") || result.contains("PM"));
    }

    @Test
    public void deadline_toString_withTime_dateTimeFormat() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 6, 15, 14, 30);
        Deadline deadline = new Deadline("Finish report", by);
        String result = deadline.toString();

        assertTrue(result.contains("by: Jun 15 2025 2:30PM"));
    }

    @Test
    public void deadline_toString_markedDone_correctFormat() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        deadline.markAsDone();
        String result = deadline.toString();

        assertTrue(result.startsWith("[D][X]"));
        assertTrue(result.contains("Finish report"));
    }

    @Test
    public void deadline_getBy_afterModification_unchanged() {
        java.time.LocalDateTime originalBy = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", originalBy);

        deadline.markAsDone();
        deadline.setDescription("Updated report");

        assertEquals(originalBy, deadline.getBy());
    }

    @Test
    public void deadline_setDescription_updatesDescription() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Original", by);
        deadline.setDescription("Updated");

        assertEquals("Updated", deadline.getDescription());
    }

    // ==================== Event Additional Tests ====================

    @Test
    public void event_toString_midnightTimes_dateOnlyFormat() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 0, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 0, 0);
        Event event = new Event("Midnight event", from, to);
        String result = event.toString();

        assertTrue(result.contains("from: Jun 15 2025"));
        assertTrue(result.contains("to: Jun 15 2025"));
    }

    @Test
    public void event_toString_withTimes_dateTimeFormat() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 9, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 17, 30);
        Event event = new Event("Conference", from, to);
        String result = event.toString();

        assertTrue(result.contains("from: Jun 15 2025 9:00AM"));
        assertTrue(result.contains("to: Jun 15 2025 5:30PM"));
    }

    @Test
    public void event_toString_markedDone_correctFormat() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);
        event.markAsDone();
        String result = event.toString();

        assertTrue(result.startsWith("[E][X]"));
        assertTrue(result.contains("Team meeting"));
    }

    @Test
    public void event_getFromAndTo_afterModification_unchanged() {
        java.time.LocalDateTime originalFrom = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime originalTo = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", originalFrom, originalTo);

        event.markAsDone();
        event.setDescription("Updated meeting");

        assertEquals(originalFrom, event.getFrom());
        assertEquals(originalTo, event.getTo());
    }

    @Test
    public void event_setDescription_updatesDescription() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Original", from, to);
        event.setDescription("Updated");

        assertEquals("Updated", event.getDescription());
    }

    @Test
    public void event_sameStartAndEndTime_success() {
        java.time.LocalDateTime time = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        Event event = new Event("Instant event", time, time);

        assertEquals(time, event.getFrom());
        assertEquals(time, event.getTo());
    }

    // ==================== Todo Additional Tests ====================

    @Test
    public void todo_setDescription_updatesDescription() {
        Todo todo = new Todo("Original");
        todo.setDescription("Updated");
        assertEquals("Updated", todo.getDescription());
    }

    @Test
    public void todo_markDoneAndGetStatusIcon_correct() {
        Todo todo = new Todo("Test");
        assertEquals(" ", todo.getStatusIcon());

        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());

        todo.markAsNotDone();
        assertEquals(" ", todo.getStatusIcon());
    }

    // ==================== TaskType Additional Tests ====================

    @Test
    public void taskType_fromTodoTask_returnsTodo() {
        Todo todo = new Todo("Test");
        assertEquals(TaskType.TODO, todo.getType());
    }

    @Test
    public void taskType_fromDeadlineTask_returnsDeadline() {
        Deadline deadline = new Deadline("Test",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        assertEquals(TaskType.DEADLINE, deadline.getType());
    }

    @Test
    public void taskType_fromEventTask_returnsEvent() {
        Event event = new Event("Test",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 12, 0));
        assertEquals(TaskType.EVENT, event.getType());
    }

    // ==================== Task Identity and Equality Tests ====================

    @Test
    public void task_sameDescriptionDifferentInstances_differentObjects() {
        Task task1 = new Task("Test", TaskType.TODO);
        Task task2 = new Task("Test", TaskType.TODO);

        // They are different objects in memory
        assert task1 != task2;
    }

    @Test
    public void task_sameObject_sameReference() {
        Task task1 = new Task("Test", TaskType.TODO);
        Task task2 = task1;

        assert task1 == task2;
    }

    @Test
    public void deadline_sameParametersDifferentInstances_differentObjects() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline d1 = new Deadline("Test", by);
        Deadline d2 = new Deadline("Test", by);

        assert d1 != d2;
    }

    @Test
    public void event_sameParametersDifferentInstances_differentObjects() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event e1 = new Event("Test", from, to);
        Event e2 = new Event("Test", from, to);

        assert e1 != e2;
    }

    // ==================== Task State Transition Tests ====================

    @Test
    public void task_stateTransitions_newToDoneToNotDone() {
        Task task = new Task("Test", TaskType.TODO);

        // Initial state
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());

        // Mark as done
        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("X", task.getStatusIcon());

        // Mark as not done
        task.markAsNotDone();
        assertFalse(task.isDone());
        assertEquals(" ", task.getStatusIcon());
    }

    @Test
    public void deadline_stateTransitions_complete() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Report", by);

        assertFalse(deadline.isDone());
        deadline.markAsDone();
        assertTrue(deadline.isDone());
        deadline.markAsNotDone();
        assertFalse(deadline.isDone());
    }

    @Test
    public void event_stateTransitions_complete() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Meeting", from, to);

        assertFalse(event.isDone());
        event.markAsDone();
        assertTrue(event.isDone());
        event.markAsNotDone();
        assertFalse(event.isDone());
    }

    // ==================== Task Description Edge Cases ====================

    @Test
    public void task_descriptionWithNewlines_preserved() {
        Task task = new Task("Line1\nLine2", TaskType.TODO);
        assertEquals("Line1\nLine2", task.getDescription());
    }

    @Test
    public void task_descriptionWithTabs_preserved() {
        Task task = new Task("Tab\there", TaskType.TODO);
        assertEquals("Tab\there", task.getDescription());
    }

    @Test
    public void task_descriptionWithLeadingTrailingSpaces_preserved() {
        Task task = new Task("  spaces  ", TaskType.TODO);
        assertEquals("  spaces  ", task.getDescription());
    }

    // ==================== Task toString Consistency Tests ====================

    @Test
    public void task_toString_consistentAfterMarkDone() {
        Task task = new Task("Test", TaskType.TODO);
        String beforeMark = task.toString();

        task.markAsDone();
        String afterMark = task.toString();

        // Only the status icon should change
        assertTrue(beforeMark.contains("[T][ ]"));
        assertTrue(afterMark.contains("[T][X]"));
    }

    @Test
    public void deadline_toString_consistentAfterMarkDone() {
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Report", by);
        String beforeMark = deadline.toString();

        deadline.markAsDone();
        String afterMark = deadline.toString();

        assertTrue(beforeMark.contains("[D][ ]"));
        assertTrue(afterMark.contains("[D][X]"));
        // Time should remain the same
        assertTrue(beforeMark.substring(beforeMark.indexOf("by:"))
                .equals(afterMark.substring(afterMark.indexOf("by:"))));
    }

    @Test
    public void event_toString_consistentAfterMarkDone() {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Meeting", from, to);
        String beforeMark = event.toString();

        event.markAsDone();
        String afterMark = event.toString();

        assertTrue(beforeMark.contains("[E][ ]"));
        assertTrue(afterMark.contains("[E][X]"));
        // Times should remain the same
        assertTrue(beforeMark.contains("from:"));
        assertTrue(afterMark.contains("from:"));
    }
}
