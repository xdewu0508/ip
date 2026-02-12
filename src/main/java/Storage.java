import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    public ArrayList<Task> load() throws LeoException {
        ArrayList<Task> tasks = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return tasks; // no file yet
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
            // corrupted lines are ignored (stretch: tolerant load)
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws LeoException {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent); // create ./data if missing
            }

            List<String> lines = new ArrayList<>();
            for (Task t : tasks) {
                lines.add(serializeTask(t));
            }

            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new LeoException("Cannot write save file: " + filePath);
        }
    }

    private String serializeTask(Task t) {
        String done = t.isDone ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.description;
        }
        if (t instanceof Deadline d) {
            return "D | " + done + " | " + t.description + " | " + d.by;
        }
        if (t instanceof Event e) {
            return "E | " + done + " | " + t.description + " | " + e.from + " | " + e.to;
        }

        // fallback (should not happen in your current app)
        return "T | " + done + " | " + t.description;
    }

    private Task parseLine(String line) {
        // split around | with optional spaces
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        String doneStr = parts[1];
        String desc = parts[2];

        boolean isDone;
        if (doneStr.equals("1")) {
            isDone = true;
        } else if (doneStr.equals("0")) {
            isDone = false;
        } else {
            return null;
        }

        Task task;
        switch (type) {
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                task = new Todo(desc);
                break;
            case "D":
                if (parts.length != 4) {
                    return null;
                }
                task = new Deadline(desc, parts[3]);
                break;
            case "E":
                if (parts.length != 5) {
                    return null;
                }
                task = new Event(desc, parts[3], parts[4]);
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
