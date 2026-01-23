import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Parser {
    private final Ui ui;
    private final TaskList tasks;
    private final Storage storage;

    public Parser(Ui ui, TaskList tasks, Storage storage) {
        this.ui = ui;
        this.tasks = tasks;
        this.storage = storage;
    }

    public void handle(String line) throws LeoException {
        if (line.isEmpty()) {
            return;
        }
        if (line.equals("list")) {
            handleList();
            return;
        }
        if (line.startsWith("mark ")) {
            handleMark(line.substring(5));
            return;
        }
        if (line.startsWith("unmark ")) {
            handleUnmark(line.substring(7));
            return;
        }
        if (line.startsWith("todo")) {
            handleTodo(line.substring(4));
            return;
        }
        if (line.startsWith("deadline")) {
            handleDeadline(line.substring(8));
            return;
        }
        if (line.startsWith("event")) {
            handleEvent(line.substring(5));
            return;
        }
        if (line.startsWith("delete ")) {
            handleDelete(line.substring(7));
            return;
        }
        throw new LeoException("I'm sorry, but I don't know what that means :-(");
    }

    private void handleList() {
        ui.showBox(tasks.formatListForBox().split("\\R"));
    }

    private void handleMark(String arg) throws LeoException {
        Integer idx = parseIndex(arg);
        if (!validIndex(idx)) {
            throw new LeoException("Please give a valid task number to mark.");
        }
        Task t = tasks.get(idx);
        t.mark();
        storage.save(tasks); // Level-7: persist
        ui.showBox("Nice! I've marked this task as done:", "  " + t);
    }

    private void handleUnmark(String arg) throws LeoException {
        Integer idx = parseIndex(arg);
        if (!validIndex(idx)) {
            throw new LeoException("Please give a valid task number to unmark.");
        }
        Task t = tasks.get(idx);
        t.unmark();
        storage.save(tasks);
        ui.showBox("OK, I've marked this task as not done yet:", "  " + t);
    }

    private void handleDelete(String arg) throws LeoException {
        Integer idx = parseIndex(arg);
        if (!validIndex(idx)) {
            throw new LeoException("Please give a valid task number to delete.");
        }
        Task removed = tasks.remove(idx);
        storage.save(tasks);
        ui.showBox(
                "Noted. I've removed this task:",
                "  " + removed.toString(),
                "Now you have " + tasks.size() + " task(s) in the list."
        );
    }

    private void handleTodo(String remainder) throws LeoException {
        String desc = remainder == null ? "" : remainder.trim();
        if (desc.isEmpty()) {
            throw new LeoException("The description of a todo cannot be empty.");
        }
        Todo t = new Todo(desc);
        tasks.add(t);
        storage.save(tasks);
        showAddedTaskBox(t);
    }

    private void handleDeadline(String remainder) throws LeoException {
        int pos = lastKeyword(remainder, "/by");
        if (pos <= 0) {
            throw new LeoException("Usage: deadline <desc> /by <date-time>");
        }
        String desc = remainder.substring(0, pos).trim();
        String byStr = remainder.substring(pos + 3).trim();
        try {
            LocalDateTime by = DateTimeUtil.parse(byStr);
            Deadline d = new Deadline(desc, by);
            tasks.add(d);
            storage.save(tasks);
            showAddedTaskBox(d);
        } catch (DateTimeParseException e) {
            throw new LeoException("Invalid date format. Use d/M/yyyy HHmm or yyyy-MM-dd HHmm.");
        }
    }

    private void handleEvent(String remainder) throws LeoException {
        int posFrom = lastKeyword(remainder, "/from");
        int posTo = lastKeyword(remainder, "/to");
        int posAt = lastKeyword(remainder, "/at");
        try {
            if (posFrom > 0 && posTo > posFrom) {
                String desc = remainder.substring(0, posFrom).trim();
                LocalDateTime from = DateTimeUtil.parse(remainder.substring(posFrom + 5, posTo).trim());
                LocalDateTime to = DateTimeUtil.parse(remainder.substring(posTo + 3).trim());
                Event e = new Event(desc, from, to);
                tasks.add(e);
                storage.save(tasks);
                showAddedTaskBox(e);
                return;
            }
            if (posAt > 0) {
                String desc = remainder.substring(0, posAt).trim();
                LocalDateTime at = DateTimeUtil.parse(remainder.substring(posAt + 3).trim());
                Event e = new Event(desc, at, null);
                tasks.add(e);
                storage.save(tasks);
                showAddedTaskBox(e);
                return;
            }
        } catch (DateTimeParseException ex) {
            throw new LeoException("Invalid date format. Use d/M/yyyy HHmm or yyyy-MM-dd HHmm.");
        }
        throw new LeoException("Usage: event <desc> /from <date-time> /to <date-time>");
    }

    private void showAddedTaskBox(Task t) {
        ui.showBox(
                "Got it. I've added this task:",
                "  " + t.toString(),
                "Now you have " + tasks.size() + " task(s) in the list."
        );
    }

    private Integer parseIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean validIndex(Integer idx) {
        return idx != null && idx >= 1 && idx <= tasks.size();
    }

    private int lastKeyword(String s, String kw) {
        return s.lastIndexOf(kw);
    }
}
