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
     * @throws LeoException if the file cannot be read
     */
    public TaskList load() throws LeoException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return new TaskList(tasks);
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new LeoException("Cannot read save file: " + filePath);
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
     * @throws LeoException if the file cannot be written
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
        } catch (IOException e) {
            throw new LeoException("Cannot write save file: " + filePath);
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
        String done = t.isDone() ? "1" : "0";
        switch (t.getType()) {
        case TODO:
            return "T | " + done + " | " + t.getDescription();

        case DEADLINE:
            Deadline d = (Deadline) t;
            return "D | " + done + " | "
                    + d.getDescription() + " | "
                    + DateTimeUtil.toStoredString(d.getBy());

        case EVENT:
            Event e = (Event) t;
            return "E | " + done + " | "
                    + e.getDescription() + " | "
                    + DateTimeUtil.toStoredString(e.getFrom())
                    + " | "
                    + DateTimeUtil.toStoredString(e.getTo());

        default:
            return "";
        }
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
        if (parts.length < 3) {
            return null;
        }
        String typeCode = parts[0];
        boolean isDone = parts[1].equals("1");
        String desc = parts[2];
        Task task;
        switch (typeCode) {
        case "T":
            task = new Todo(desc);
            break;
        case "D":
            if (parts.length != 4) {
                return null;
            }
            task = new Deadline(desc,
                    DateTimeUtil.parseStored(parts[3]));
            break;
        case "E":
            if (parts.length != 5) {
                return null;
            }
            task = new Event(desc,
                    DateTimeUtil.parseStored(parts[3]),
                    DateTimeUtil.parseStored(parts[4]));
            break;
        default:
            return null;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
