package leo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leo.exception.LeoException;

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
    public void constructor_withExistingTasks_success() throws LeoException {
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
    public void add_todoTask_success() throws LeoException {
        Todo todo = new Todo("Buy groceries");
        taskList.add(todo);

        assertEquals(1, taskList.size());
        assertEquals("Buy groceries", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Todo);
    }

    @Test
    public void add_deadlineTask_success() throws LeoException {
        Deadline deadline = new Deadline("Finish report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        taskList.add(deadline);

        assertEquals(1, taskList.size());
        assertEquals("Finish report", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Deadline);
    }

    @Test
    public void add_eventTask_success() throws LeoException {
        Event event = new Event("Team meeting",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 11, 0));
        taskList.add(event);

        assertEquals(1, taskList.size());
        assertEquals("Team meeting", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Event);
    }

    @Test
    public void add_multipleTasks_success() throws LeoException {
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
    public void get_firstTask_success() throws LeoException {
        taskList.add(new Todo("First"));
        taskList.add(new Todo("Second"));

        Task first = taskList.get(0);
        assertEquals("First", first.getDescription());
    }

    @Test
    public void get_lastTask_success() throws LeoException {
        taskList.add(new Todo("First"));
        taskList.add(new Todo("Last"));

        Task last = taskList.get(1);
        assertEquals("Last", last.getDescription());
    }

    @Test
    public void get_invalidIndex_throwsIndexOutOfBoundsException() throws LeoException {
        taskList.add(new Todo("Only task"));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.get(5);
        });
    }

    // ==================== Remove Method Tests ====================

    @Test
    public void remove_firstTask_success() throws LeoException {
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
    public void remove_lastTask_success() throws LeoException {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));
        taskList.add(new Todo("Task 3"));

        Task removed = taskList.remove(2);

        assertEquals("Task 3", removed.getDescription());
        assertEquals(2, taskList.size());
    }

    @Test
    public void remove_onlyTask_listBecomesEmpty() throws LeoException {
        taskList.add(new Todo("Only task"));

        taskList.remove(0);

        assertEquals(0, taskList.size());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void remove_invalidIndex_throwsIndexOutOfBoundsException() throws LeoException {
        taskList.add(new Todo("Only task"));

        assertThrows(IndexOutOfBoundsException.class, () -> {
            taskList.remove(5);
        });
    }

    // ==================== Mark/Unmark Method Tests ====================

    @Test
    public void markAsDone_taskBecomesDone() throws LeoException {
        Todo todo = new Todo("Task to complete");
        taskList.add(todo);

        assertFalse(taskList.get(0).isDone());

        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());
        assertEquals("X", taskList.get(0).getStatusIcon());
    }

    @Test
    public void markAsNotDone_taskBecomesNotDone() throws LeoException {
        Todo todo = new Todo("Task to undo");
        taskList.add(todo);
        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());

        taskList.markAsNotDone(0);

        assertFalse(taskList.get(0).isDone());
        assertEquals(" ", taskList.get(0).getStatusIcon());
    }

    @Test
    public void markAsDone_deadline_success() throws LeoException {
        Deadline deadline = new Deadline("Submit report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        taskList.add(deadline);

        taskList.markAsDone(0);

        assertTrue(taskList.get(0).isDone());
    }

    @Test
    public void markAsNotDone_event_success() throws LeoException {
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
    public void size_afterAddingTasks_returnsCorrectCount() throws LeoException {
        assertEquals(0, taskList.size());

        taskList.add(new Todo("Task 1"));
        assertEquals(1, taskList.size());

        taskList.add(new Todo("Task 2"));
        assertEquals(2, taskList.size());

        taskList.add(new Todo("Task 3"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void size_afterRemovingTasks_returnsCorrectCount() throws LeoException {
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
    public void isEmpty_afterAdd_returnsFalse() throws LeoException {
        taskList.add(new Todo("Task"));
        assertFalse(taskList.isEmpty());
    }

    @Test
    public void isEmpty_afterRemoveAll_returnsTrue() throws LeoException {
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
    public void getAll_withTasks_returnsAllTasks() throws LeoException {
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
    public void getAll_returnsTheInternalList_reference() throws LeoException {
        taskList.add(new Todo("Task 1"));

        ArrayList<Task> all = taskList.getAll();

        // Note: getAll() returns the internal list reference (by design)
        // Modifying it will affect the original taskList
        assertEquals(1, all.size());
    }

    // ==================== Mixed Operations Tests ====================

    @Test
    public void mixedOperations_complexScenario_success() throws LeoException {
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

    // ==================== Duplicate Detection Tests ====================

    @Test
    public void add_duplicateTodo_throwsLeoException() throws LeoException {
        taskList.add(new Todo("Buy groceries"));

        assertThrows(LeoException.class, () -> {
            taskList.add(new Todo("Buy groceries"));
        });
    }

    @Test
    public void add_duplicateTodoCaseInsensitive_throwsLeoException() throws LeoException {
        taskList.add(new Todo("Buy groceries"));

        assertThrows(LeoException.class, () -> {
            taskList.add(new Todo("BUY GROCERIES"));
        });
    }

    @Test
    public void add_duplicateDeadline_throwsLeoException() throws LeoException {
        java.time.LocalDateTime deadline = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        taskList.add(new Deadline("Finish report", deadline));

        assertThrows(LeoException.class, () -> {
            taskList.add(new Deadline("Finish report", deadline));
        });
    }

    @Test
    public void add_duplicateEvent_throwsLeoException() throws LeoException {
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 11, 0);
        taskList.add(new Event("Team meeting", from, to));

        assertThrows(LeoException.class, () -> {
            taskList.add(new Event("Team meeting", from, to));
        });
    }

    @Test
    public void add_differentTodo_sameDescription_allowed() throws LeoException {
        // Different types with same description should be allowed
        taskList.add(new Todo("Task"));
        taskList.add(new Deadline("Task", java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));

        assertEquals(2, taskList.size());
    }

    @Test
    public void add_sameDescriptionDifferentTime_allowed() throws LeoException {
        // Same description but different times should be allowed
        taskList.add(new Deadline("Report", java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));
        taskList.add(new Deadline("Report", java.time.LocalDateTime.of(2026, 1, 15, 23, 59)));

        assertEquals(2, taskList.size());
    }

    // ==================== GetDoneTasks Method Tests ====================

    @Test
    public void getDoneTasks_emptyList_returnsEmptyList() throws LeoException {
        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        assertEquals(0, doneTasks.size());
    }

    @Test
    public void getDoneTasks_noDoneTasks_returnsEmptyList() throws LeoException {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));

        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        assertEquals(0, doneTasks.size());
    }

    @Test
    public void getDoneTasks_allDoneTasks_returnsAllTasks() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        task1.markAsDone();
        task2.markAsDone();
        taskList.add(task1);
        taskList.add(task2);

        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        assertEquals(2, doneTasks.size());
        assertTrue(doneTasks.get(0).isDone());
        assertTrue(doneTasks.get(1).isDone());
    }

    @Test
    public void getDoneTasks_mixedTasks_returnsOnlyDoneTasks() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        Todo task3 = new Todo("Task 3");
        task1.markAsDone();
        task3.markAsDone();
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        assertEquals(2, doneTasks.size());
        assertEquals("Task 1", doneTasks.get(0).getDescription());
        assertEquals("Task 3", doneTasks.get(1).getDescription());
    }

    @Test
    public void getDoneTasks_mixedTaskTypes_success() throws LeoException {
        Todo todo = new Todo("Todo task");
        Deadline deadline = new Deadline("Deadline task",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        Event event = new Event("Event task",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 11, 0));

        todo.markAsDone();
        deadline.markAsDone();

        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);

        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        assertEquals(2, doneTasks.size());
        assertTrue(doneTasks.get(0) instanceof Todo);
        assertTrue(doneTasks.get(1) instanceof Deadline);
    }

    // ==================== GetNotDoneTasks Method Tests ====================

    @Test
    public void getNotDoneTasks_emptyList_returnsEmptyList() {
        ArrayList<Task> notDoneTasks = taskList.getNotDoneTasks();
        assertEquals(0, notDoneTasks.size());
    }

    @Test
    public void getNotDoneTasks_allNotDoneTasks_returnsAllTasks() throws LeoException {
        taskList.add(new Todo("Task 1"));
        taskList.add(new Todo("Task 2"));

        ArrayList<Task> notDoneTasks = taskList.getNotDoneTasks();
        assertEquals(2, notDoneTasks.size());
        assertFalse(notDoneTasks.get(0).isDone());
        assertFalse(notDoneTasks.get(1).isDone());
    }

    @Test
    public void getNotDoneTasks_noNotDoneTasks_returnsEmptyList() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        task1.markAsDone();
        task2.markAsDone();
        taskList.add(task1);
        taskList.add(task2);

        ArrayList<Task> notDoneTasks = taskList.getNotDoneTasks();
        assertEquals(0, notDoneTasks.size());
    }

    @Test
    public void getNotDoneTasks_mixedTasks_returnsOnlyNotDoneTasks() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        Todo task3 = new Todo("Task 3");
        task1.markAsDone();
        task3.markAsDone();
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        ArrayList<Task> notDoneTasks = taskList.getNotDoneTasks();
        assertEquals(1, notDoneTasks.size());
        assertEquals("Task 2", notDoneTasks.get(0).getDescription());
        assertFalse(notDoneTasks.get(0).isDone());
    }

    // ==================== FindTasks Method Tests ====================

    @Test
    public void findTasks_emptyList_returnsEmptyList() {
        ArrayList<Task> found = taskList.findTasks("keyword");
        assertEquals(0, found.size());
    }

    @Test
    public void findTasks_noMatchingTasks_returnsEmptyList() throws LeoException {
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Todo("Read book"));

        ArrayList<Task> found = taskList.findTasks("xyz");
        assertEquals(0, found.size());
    }

    @Test
    public void findTasks_singleMatch_success() throws LeoException {
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Todo("Read book"));
        taskList.add(new Todo("Buy milk"));

        ArrayList<Task> found = taskList.findTasks("groceries");
        assertEquals(1, found.size());
        assertEquals("Buy groceries", found.get(0).getDescription());
    }

    @Test
    public void findTasks_multipleMatches_success() throws LeoException {
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Todo("Read book"));
        taskList.add(new Todo("Buy milk"));

        ArrayList<Task> found = taskList.findTasks("buy");
        assertEquals(2, found.size());
        assertEquals("Buy groceries", found.get(0).getDescription());
        assertEquals("Buy milk", found.get(1).getDescription());
    }

    @Test
    public void findTasks_caseInsensitive_success() throws LeoException {
        taskList.add(new Todo("BUY GROCERIES"));
        taskList.add(new Todo("buy milk"));
        taskList.add(new Todo("Read Book"));

        ArrayList<Task> found = taskList.findTasks("BUY");
        assertEquals(2, found.size());

        found = taskList.findTasks("book");
        assertEquals(1, found.size());
    }

    @Test
    public void findTasks_partialMatch_success() throws LeoException {
        taskList.add(new Todo("Buy groceries from supermarket"));
        taskList.add(new Todo("Read"));
        taskList.add(new Todo("Reading book"));

        ArrayList<Task> found = taskList.findTasks("read");
        assertEquals(2, found.size());
        assertEquals("Read", found.get(0).getDescription());
        assertEquals("Reading book", found.get(1).getDescription());
    }

    @Test
    public void findTasks_mixedTaskTypes_success() throws LeoException {
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Deadline("Finish report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));
        taskList.add(new Event("Team meeting",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 11, 0)));

        ArrayList<Task> found = taskList.findTasks("team");
        assertEquals(1, found.size());
        assertTrue(found.get(0) instanceof Event);
    }

    // ==================== Stream Methods Independence Tests ====================

    @Test
    public void getDoneTasks_returnsNewArrayList_independentFromOriginal() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        task1.markAsDone();
        taskList.add(task1);
        taskList.add(task2);

        ArrayList<Task> doneTasks = taskList.getDoneTasks();
        taskList.remove(0); // Modify original list

        assertEquals(1, doneTasks.size()); // Should not be affected
    }

    @Test
    public void getNotDoneTasks_returnsNewArrayList_independentFromOriginal() throws LeoException {
        Todo task1 = new Todo("Task 1");
        Todo task2 = new Todo("Task 2");
        taskList.add(task1);
        taskList.add(task2);

        ArrayList<Task> notDoneTasks = taskList.getNotDoneTasks();
        taskList.remove(0); // Modify original list

        assertEquals(2, notDoneTasks.size()); // Should not be affected
    }

    @Test
    public void findTasks_returnsNewArrayList_independentFromOriginal() throws LeoException {
        taskList.add(new Todo("Buy groceries"));
        taskList.add(new Todo("Read book"));

        ArrayList<Task> found = taskList.findTasks("buy");
        taskList.remove(0); // Modify original list

        assertEquals(1, found.size()); // Should not be affected
    }
}
