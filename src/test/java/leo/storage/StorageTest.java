package leo.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leo.task.Deadline;
import leo.task.Event;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Todo;

/**
 * Tests for the Storage class.
 * Tests cover saving and loading tasks, handling missing files, and data integrity.
 */
public class StorageTest {

    private static final String TEST_FILE_PATH = "data/test_leo.txt";
    private Storage storage;

    @BeforeEach
    public void setUp() throws IOException {
        // Clean up any existing test file
        Path testFile = Paths.get(TEST_FILE_PATH);
        if (Files.exists(testFile)) {
            Files.delete(testFile);
        }
        storage = new Storage(TEST_FILE_PATH);
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up test file after each test
        Path testFile = Paths.get(TEST_FILE_PATH);
        if (Files.exists(testFile)) {
            Files.delete(testFile);
        }
    }

    // ==================== Constructor Tests ====================

    @Test
    public void constructor_createsStorageWithCorrectPath() {
        Storage testStorage = new Storage("data/another_test.txt");
        assertNotNull(testStorage);
    }

    // ==================== Load Method Tests ====================

    @Test
    public void load_nonExistentFile_returnsEmptyTaskList() throws Exception {
        TaskList tasks = storage.load();
        assertEquals(0, tasks.size());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void load_emptyFile_returnsEmptyTaskList() throws Exception {
        // Create empty file
        Path testFile = Paths.get(TEST_FILE_PATH);
        Files.createFile(testFile);

        TaskList tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_fileWithTodoTasks_success() throws Exception {
        // Manually create a save file with todo tasks
        String content = "T | 0 | Buy groceries\n" +
                         "T | 1 | Read book\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), content);

        TaskList tasks = storage.load();
        assertEquals(2, tasks.size());

        Task task1 = tasks.get(0);
        assertTrue(task1 instanceof Todo);
        assertEquals("Buy groceries", task1.getDescription());
        assertFalse(task1.isDone());

        Task task2 = tasks.get(1);
        assertTrue(task2 instanceof Todo);
        assertEquals("Read book", task2.getDescription());
        assertTrue(task2.isDone());
    }

    @Test
    public void load_fileWithDeadlineTasks_success() throws Exception {
        String content = "D | 0 | Finish report | 2025-12-31T23:59\n" +
                         "D | 1 | Submit assignment | 2025-06-15T18:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), content);

        TaskList tasks = storage.load();
        assertEquals(2, tasks.size());

        Task task1 = tasks.get(0);
        assertTrue(task1 instanceof Deadline);
        assertEquals("Finish report", task1.getDescription());
        assertFalse(task1.isDone());

        Task task2 = tasks.get(1);
        assertTrue(task2 instanceof Deadline);
        assertEquals("Submit assignment", task2.getDescription());
        assertTrue(task2.isDone());
    }

    @Test
    public void load_fileWithEventTasks_success() throws Exception {
        String content = "E | 0 | Team meeting | 2025-06-15T10:00 | 2025-06-15T12:00\n" +
                         "E | 1 | Conference | 2025-07-20T09:00 | 2025-07-20T17:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), content);

        TaskList tasks = storage.load();
        assertEquals(2, tasks.size());

        Task task1 = tasks.get(0);
        assertTrue(task1 instanceof Event);
        assertEquals("Team meeting", task1.getDescription());

        Task task2 = tasks.get(1);
        assertTrue(task2 instanceof Event);
        assertEquals("Conference", task2.getDescription());
    }

    @Test
    public void load_fileWithMixedTasks_success() throws Exception {
        String content = "T | 0 | Simple task\n" +
                         "D | 1 | Deadline task | 2025-12-31T23:59\n" +
                         "E | 0 | Event task | 2025-06-15T10:00 | 2025-06-15T12:00\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), content);

        TaskList tasks = storage.load();
        assertEquals(3, tasks.size());

        assertTrue(tasks.get(0) instanceof Todo);
        assertTrue(tasks.get(1) instanceof Deadline);
        assertTrue(tasks.get(2) instanceof Event);
    }

    @Test
    public void load_fileWithEmptyLines_skipsEmptyLines() throws Exception {
        String content = "T | 0 | Task 1\n" +
                         "\n" +
                         "   \n" +
                         "T | 1 | Task 2\n";
        Files.writeString(Paths.get(TEST_FILE_PATH), content);

        TaskList tasks = storage.load();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getDescription());
        assertEquals("Task 2", tasks.get(1).getDescription());
    }

    // ==================== Save Method Tests ====================

    @Test
    public void save_emptyTaskList_createsFile() throws Exception {
        TaskList tasks = new TaskList();
        storage.save(tasks);

        Path testFile = Paths.get(TEST_FILE_PATH);
        assertTrue(Files.exists(testFile));
        assertEquals(0, Files.size(testFile));
    }

    @Test
    public void save_todoTasks_correctFormat() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("Buy groceries"));
        tasks.add(new Todo("Read book"));

        storage.save(tasks);

        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(content.contains("T | 0 | Buy groceries"));
        assertTrue(content.contains("T | 0 | Read book"));
    }

    @Test
    public void save_doneTodoTasks_correctFormat() throws Exception {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("Completed task");
        todo.markAsDone();
        tasks.add(todo);

        storage.save(tasks);

        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(content.contains("T | 1 | Completed task"));
    }

    @Test
    public void save_deadlineTasks_correctFormat() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Deadline("Finish report",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59)));

        storage.save(tasks);

        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(content.contains("D | 0 | Finish report"));
        assertTrue(content.contains("2025-12-31T23:59"));
    }

    @Test
    public void save_eventTasks_correctFormat() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new Event("Team meeting",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 12, 0)));

        storage.save(tasks);

        String content = Files.readString(Paths.get(TEST_FILE_PATH));
        assertTrue(content.contains("E | 0 | Team meeting"));
        assertTrue(content.contains("2025-06-15T10:00"));
        assertTrue(content.contains("2025-06-15T12:00"));
    }

    // ==================== Save and Load Integration Tests ====================

    @Test
    public void saveAndLoad_roundTrip_todoTasks() throws Exception {
        TaskList original = new TaskList();
        original.add(new Todo("Buy groceries"));
        Todo doneTodo = new Todo("Read book");
        doneTodo.markAsDone();
        original.add(doneTodo);

        storage.save(original);
        TaskList loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals("Buy groceries", loaded.get(0).getDescription());
        assertFalse(loaded.get(0).isDone());
        assertEquals("Read book", loaded.get(1).getDescription());
        assertTrue(loaded.get(1).isDone());
    }

    @Test
    public void saveAndLoad_roundTrip_deadlineTasks() throws Exception {
        TaskList original = new TaskList();
        java.time.LocalDateTime by = java.time.LocalDateTime.of(2025, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Finish report", by);
        deadline.markAsDone();
        original.add(deadline);

        storage.save(original);
        TaskList loaded = storage.load();

        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Deadline);
        assertEquals("Finish report", loaded.get(0).getDescription());
        assertTrue(loaded.get(0).isDone());
    }

    @Test
    public void saveAndLoad_roundTrip_eventTasks() throws Exception {
        TaskList original = new TaskList();
        java.time.LocalDateTime from = java.time.LocalDateTime.of(2025, 6, 15, 10, 0);
        java.time.LocalDateTime to = java.time.LocalDateTime.of(2025, 6, 15, 12, 0);
        Event event = new Event("Team meeting", from, to);
        original.add(event);

        storage.save(original);
        TaskList loaded = storage.load();

        assertEquals(1, loaded.size());
        assertTrue(loaded.get(0) instanceof Event);
        assertEquals("Team meeting", loaded.get(0).getDescription());
    }

    @Test
    public void saveAndLoad_roundTrip_mixedTasks() throws Exception {
        TaskList original = new TaskList();
        original.add(new Todo("Simple task"));

        Deadline deadline = new Deadline("Deadline task",
                java.time.LocalDateTime.of(2025, 12, 31, 23, 59));
        deadline.markAsDone();
        original.add(deadline);

        Event event = new Event("Event task",
                java.time.LocalDateTime.of(2025, 6, 15, 10, 0),
                java.time.LocalDateTime.of(2025, 6, 15, 12, 0));
        original.add(event);

        storage.save(original);
        TaskList loaded = storage.load();

        assertEquals(3, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertTrue(loaded.get(2) instanceof Event);
        assertTrue(loaded.get(1).isDone());
    }
}
