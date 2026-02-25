package leo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the TaskList class.
 * Tests cover all public methods including add, remove, get, mark, and utility methods.
 */
public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    // ==================== Constructor Tests ====================

    @Test
    public void constructor_emptyTaskList_success() {
        TaskList emptyList = new TaskList();
        assertEquals(0, emptyList.size());
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void constructor_withExistingTasks_success() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("Task 1"));
        tasks.add(new Todo("Task 2"));

        TaskList list = new TaskList(tasks);
        assertEquals(2, list.size());
        assertEquals("Task 1", list.get(0).getDescription());
        assertEquals("Task 2", list.get(1).getDescription());
    }

    // ==================== Add Method Tests ====================

    @Test
    public void add_todoTask_success() {
        Todo todo = new Todo("Buy groceries");
        taskList.add(todo);

        assertEquals(1, taskList.size());
        assertEquals("Buy groceries", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Todo);
    }

    @Test
    public void add_deadlineTask_success() {
        Deadline deadline = new Deadline("Finish report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        taskList.add(deadline);

        assertEquals(1, taskList.size());
        assertEquals("Finish report", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Deadline);
    }

    @Test
    public void add_eventTask_success() {
        Event event = new Event("Team meeting",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 11, 0));
        taskList.add(event);

        assertEquals(1, taskList.size());
        assertEquals("Team meeting", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Event);
    }

    @Test
    public void add_multipleTasks_success() {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));
        taskList.add(new Todo("Task 3"));

        assertEquals(3, taskList.size());
        assertEquals("Task 1", taskList.get(0).getDescription());
        assertEquals("Task 2", taskList.get(1).getDescription());
        assertEquals("Task 3", taskList.get(2).getDescription());
    }

    // ==================== Get Method Tests ====================

    @Test
    public void get_firstTask_success() {
        taskList.add(new Todo("First"));
        taskList.add(new Todo("Second"));

        Task first = taskList.get(0);
        assertEquals("First", first.getDescription());
    }

    @Test
    public void get_lastTask_success() {
        taskList.add(new Todo("First"));
        taskList.add(new Todo("Last"));

        Task last = taskList.get(1);
        assertEquals("Last", last.getDescription());
    }

    @Test
    public void get_invalidIndex_throwsIndexOutOfBoundsException() {
        taskList.add(new Todo("Only task"));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(5);
        });
    }

    // ==================== Remove Method Tests ====================

    @Test
    public void remove_firstTask_success() {
        Todo todo1 = new Todo("Task 1");
        Todo todo2 = new Todo("Task 2");
        taskList.add(todo1);
        taskList.add(todo2);

        Task removed = taskList.remove(0);

        assertEquals("Task 1", removed.getDescription());
        assertEquals(1, taskList.size());
        assertEquals("Task 2", taskList.get(0).getDescription());
    }

    @Test
    public void remove_lastTask_success() {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));
        taskList.add(new Todo("Task 3"));

        Task removed = taskList.remove(2);

        assertEquals("Task 3", removed.getDescription());
        assertEquals(2, taskList.size());
    }

    @Test
    public void remove_onlyTask_listBecomesEmpty() {
        taskList.add(new Todo("Only task"));

        taskList.remove(0);

        assertEquals(0, taskList.size());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void remove_invalidIndex_throwsIndexOutOfBoundsException() {
        taskList.add(new Todo("Only task"));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.remove(5);
        });
    }

    // ==================== Mark/Unmark Method Tests ====================

    @Test
    public void markAsDone_taskBecomesDone() {
        Todo todo = new Todo("Task to complete");
        taskList.add(todo);

        assertFalse(taskList.get(0).isDone());

        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());
        assertEquals("X", taskList.get(0).getStatusIcon());
    }

    @Test
    public void markAsNotDone_taskBecomesNotDone() {
        Todo todo = new Todo("Task to undo");
        taskList.add(todo);
        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());

        taskList.markAsNotDone(0);

        assertFalse(taskList.get(0).isDone());
        assertEquals(" ", taskList.get(0).getStatusIcon());
    }

    @Test
    public void markAsDone_deadline_success() {
        Deadline deadline = new Deadline("Submit report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        taskList.add(deadline);

        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());
    }

    @Test
    public void markAsNotDone_event_success() {
        Event event = new Event("Conference",
                java.time.LocalDateTime.of(2025, 6, 15, 9, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 17, 0));
        taskList.add(event);
        taskList.markAsDone(0);

        taskList.markAsNotDone(0);

        assertFalse(taskList.get(0).isDone());
    }

    // ==================== Size Method Tests ====================

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    public void size_afterAddingTasks_returnsCorrectCount() {
        assertEquals(0, taskList.size());

        taskList.add(new Todo("Task 1"));
        assertEquals(1, taskList.size());

        taskList.add(new Todo("Task 2"));
        assertEquals(2, taskList.size());

        taskList.add(new Todo("Task 3"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void size_afterRemovingTasks_returnsCorrectCount() {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));
        taskList.add(new Todo("Task 3"));

        taskList.remove(1);
        assertEquals(2, taskList.size());

        taskList.remove(0);
        assertEquals(1, taskList.size());
    }

    // ==================== IsEmpty Method Tests ====================

    @Test
    public void isEmpty_newList_returnsTrue() {
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void isEmpty_afterAdd_returnsFalse() {
        taskList.add(new Todo("Task"));
        assertFalse(taskList.isEmpty());
    }

    @Test
    public void isEmpty_afterRemoveAll_returnsTrue() {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));
        taskList.remove(0);
        taskList.remove(0);

        assertTrue(taskList.isEmpty());
    }

    // ==================== GetAll Method Tests ====================

    @Test
    public void getAll_emptyList_returnsEmptyArrayList() {
        ArrayList<Task> all = taskList.getAll();
        assertEquals(0, all.size());
        assertTrue(all instanceof ArrayList);
    }

    @Test
    public void getAll_withTasks_returnsAllTasks() {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Deadline("Task 2",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));
        taskList.add(new Event("Task 3",
                java.time.LocalDateTime.of(2025, 6, 15, 9, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 17, 0)));

        ArrayList<Task> all = taskList.getAll();
        assertEquals(3, all.size());
        assertEquals("Task 1", all.get(0).getDescription());
        assertEquals("Task 2", all.get(1).getDescription());
        assertEquals("Task 3", all.get(2).getDescription());
    }

    @Test
    public void getAll_returnsTheInternalList_reference() {
        taskList.add(new Todo("Task 1"));

        ArrayList<Task> all = taskList.getAll();
        
        // Note: getAll() returns the internal list reference (by design)
        // Modifying it will affect the original taskList
        assertEquals(1, all.size());
    }

    // ==================== Mixed Operations Tests ====================

    @Test
    public void mixedOperations_complexScenario_success() {
        // Add tasks
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Deadline("Finish report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));
        taskList.add(new Event("Team meeting",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 11, 0)));

        assertEquals(3, taskList.size());

        // Mark second task as done
        taskList.markAsDone(1);
        assertTrue(taskList.get(1).isDone());

        // Remove first task
        taskList.remove(0);
        assertEquals(2, taskList.size());

        // Verify remaining tasks
        assertEquals("Finish report", taskList.get(0).getDescription());
        assertTrue(taskList.get(0).isDone());
        assertEquals("Team meeting", taskList.get(1).getDescription());

        // Unmark the remaining deadline
        taskList.markAsNotDone(0);
        assertFalse(taskList.get(0).isDone());
    }
}
