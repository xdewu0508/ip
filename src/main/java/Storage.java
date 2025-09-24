import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dataDir = Path.of("data");
    private final Path dataFile = dataDir.resolve("leo.txt");

    public List<Task> load() {
        List<Task> list = new ArrayList<>();
        try {
            if (!Files.exists(dataFile)) {
                return list; // first run
            }
            try (BufferedReader br = new BufferedReader(new FileReader(dataFile.toFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Task t = deserialize(line);
                    if (t != null) {
                        list.add(t);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return list;
    }

    public void save(TaskList tasks) {
        try {
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
            File f = dataFile.toFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                for (Task t : tasks.asList()) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private String serialize(Task t) {
        String done = t.isDone ? "1" : "0";
        if (t instanceof Todo) {
            return String.join(" | ", "T", done, t.description);
        } else if (t instanceof Deadline d) {
            return String.join(" | ", "D", done, t.description, d.getBy());
        } else if (t instanceof Event e) {
            // store from/to; if single-time (/at), 'to' is blank
            String from = e.getFrom() == null ? "" : e.getFrom();
            String to = e.getTo() == null ? "" : e.getTo();
            return String.join(" | ", "E", done, t.description, from, to);
        }
        return "";
    }

    private Task deserialize(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) return null;
        String type = parts[0].trim();
        boolean done = "1".equals(parts[1].trim());
        String desc = parts[2];

        Task t = null;
        switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length >= 4) t = new Deadline(desc, parts[3]);
                break;
            case "E":
                String from = parts.length >= 4 ? parts[3] : null;
                String to = parts.length >= 5 ? parts[4] : null;
                t = new Event(desc, from, to);
                break;
            default:
                return null;
        }
        if (t != null && done) t.mark();
        return t;
    }
}
