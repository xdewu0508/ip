package leo.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import leo.exception.LeoException;
import leo.task.Deadline;
import leo.task.Event;
import leo.task.Task;
import leo.task.TaskList;
import leo.task.Todo;
import leo.util.DateTimeUtil;

/**
 * Storage handles persistence of tasks to and from a file.
 * It supports saving and loading Todo, Deadline, and Event tasks.
 * Tasks are stored in a pipe-delimited format with type, done status, description, and optional time fields.
 */
public class Storage {
    private static final String TODO_CODE = "T";
    private static final String DEADLINE_CODE = "D";
    private static final String EVENT_CODE = "E";
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";
    private static final String DELIMITER = " | ";

    private final Path filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param relativePath the relative path to the storage file
     */
    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    /**
     * Loads tasks from the storage file.
     * If the file doesn't exist, returns an empty TaskList.
     * If the file is corrupted, skips invalid lines and continues loading.
     *
     * @return a TaskList containing all loaded tasks
     * @throws LeoException if the file cannot be read due to permission issues
     */
    public TaskList load() throws LeoException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return new TaskList(tasks);
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (java.nio.file.AccessDeniedException e) {
            throw new LeoException("Access denied to save file: " + filePath 
                    + ". Please check file permissions.");
        } catch (IOException e) {
            throw new LeoException("Cannot read save file: " + filePath 
                    + ". The file may be in use or corrupted.");
        }

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            Task parsed = parseLine(line);
            if (parsed != null) {
                tasks.add(parsed);
            }
        }
        return new TaskList(tasks);
    }

    /**
     * Saves all tasks in the TaskList to the storage file.
     * Creates parent directories if they don't exist.
     *
     * @param tasks the TaskList to save
     * @throws LeoException if the file cannot be written due to permission or disk issues
     */
    public void save(TaskList tasks) throws LeoException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            ArrayList<Task> taskList = tasks.getAll();
            for (Task t : taskList) {
                lines.add(serializeTask(t));
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (java.nio.file.AccessDeniedException e) {
            throw new LeoException("Access denied to save file: " + filePath 
                    + ". Please check file permissions.");
        } catch (java.nio.file.FileSystemException e) {
            throw new LeoException("Cannot write to save file: " + filePath 
                    + ". The disk may be full or the file is locked.");
        } catch (IOException e) {
            throw new LeoException("Cannot write save file: " + filePath 
                    + ". Please check if the location is writable.");
        }
    }

    /**
     * Serializes a Task to its string representation for storage.
     * Format: TYPE | DONE | DESCRIPTION [| TIME_FIELDS]
     *
     * @param t the task to serialize
     * @return the serialized task string
     */
    private String serializeTask(Task t) {
        String doneMarker = t.isDone() ? DONE_MARKER : NOT_DONE_MARKER;
        switch (t.getType()) {
        case TODO:
            return TODO_CODE + DELIMITER + doneMarker + DELIMITER + t.getDescription();

        case DEADLINE:
            return serializeDeadline((Deadline) t, doneMarker);

        case EVENT:
            return serializeEvent((Event) t, doneMarker);

        default:
            return "";
        }
    }

    /**
     * Serializes a Deadline task to its string representation.
     *
     * @param deadline the deadline task to serialize
     * @param doneMarker the done status marker ("1" or "0")
     * @return the serialized deadline string
     */
    private String serializeDeadline(Deadline deadline, String doneMarker) {
        String byTime = DateTimeUtil.toStoredString(deadline.getBy());
        return DEADLINE_CODE + DELIMITER + doneMarker + DELIMITER
                + deadline.getDescription() + DELIMITER + byTime;
    }

    /**
     * Serializes an Event task to its string representation.
     *
     * @param event the event task to serialize
     * @param doneMarker the done status marker ("1" or "0")
     * @return the serialized event string
     */
    private String serializeEvent(Event event, String doneMarker) {
        String fromTime = DateTimeUtil.toStoredString(event.getFrom());
        String toTime = DateTimeUtil.toStoredString(event.getTo());
        return EVENT_CODE + DELIMITER + doneMarker + DELIMITER
                + event.getDescription() + DELIMITER + fromTime + DELIMITER + toTime;
    }


    /**
     * Parses a single line from the storage file into a Task object.
     * Handles Todo, Deadline, and Event task types.
     *
     * @param line the line to parse
     * @return the parsed Task, or null if the line is invalid
     * @throws LeoException if the line format is corrupted
     */
    private Task parseLine(String line) throws LeoException {
        String[] parts = line.split("\\s*\\|\\s*");

        // Guard clause: validate minimum parts
        if (parts.length < 3) {
            return null;
        }

        String typeCode = parts[0];
        boolean isDone = parts[1].equals(DONE_MARKER);
        String desc = parts[2];

        Task task = parseTaskByType(typeCode, parts, desc);

        if (task != null && isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Parses a task based on its type code.
     *
     * @param typeCode the type code (T, D, or E)
     * @param parts the split line parts
     * @param desc the task description
     * @return the parsed task, or null if invalid
     * @throws LeoException if parsing fails
     */
    private Task parseTaskByType(String typeCode, String[] parts, String desc) throws LeoException {
        switch (typeCode) {
        case TODO_CODE:
            return new Todo(desc);

        case DEADLINE_CODE:
            return parseDeadlineTask(parts, desc);

        case EVENT_CODE:
            return parseEventTask(parts, desc);

        default:
            return null;
        }
    }

    /**
     * Parses a Deadline task from stored parts.
     *
     * @param parts the split line parts
     * @param desc the task description
     * @return the parsed Deadline task, or null if invalid
     * @throws LeoException if parsing fails
     */
    private Task parseDeadlineTask(String[] parts, String desc) throws LeoException {
        boolean hasCorrectParts = parts.length == 4;
        if (!hasCorrectParts) {
            return null;
        }
        return new Deadline(desc, DateTimeUtil.parseStored(parts[3]));
    }

    /**
     * Parses an Event task from stored parts.
     *
     * @param parts the split line parts
     * @param desc the task description
     * @return the parsed Event task, or null if invalid
     * @throws LeoException if parsing fails
     */
    private Task parseEventTask(String[] parts, String desc) throws LeoException {
        boolean hasCorrectParts = parts.length == 5;
        if (!hasCorrectParts) {
            return null;
        }
        return new Event(desc,
                DateTimeUtil.parseStored(parts[3]),
                DateTimeUtil.parseStored(parts[4]));
    }
}
