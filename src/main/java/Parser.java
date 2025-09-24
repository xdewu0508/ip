public class Parser {
    private final Ui ui;
    private final TaskList tasks;

    public Parser(Ui ui, TaskList tasks) {
        this.ui = ui;
        this.tasks = tasks;
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
        ui.showBox(
                "Nice! I've marked this task as done:",
                "  " + t
        );
    }

    private void handleUnmark(String arg) throws LeoException {
        Integer idx = parseIndex(arg);
        if (!validIndex(idx)) {
            throw new LeoException("Please give a valid task number to unmark.");
        }
        Task t = tasks.get(idx);
        t.unmark();
        ui.showBox(
                "OK, I've marked this task as not done yet:",
                "  " + t
        );
    }

    private void handleDelete(String arg) throws LeoException {
        Integer idx = parseIndex(arg);
        if (!validIndex(idx)) throw new LeoException("Please give a valid task number to delete.");
        Task removed = tasks.remove(idx);
        ui.showBox(
                "Noted. I've removed this task:",
                "  " + removed.toString(),
                "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    private void handleTodo(String remainder) throws LeoException {
        String desc = remainder == null ? "" : remainder.trim();
        if (desc.isEmpty()) {
            throw new LeoException("The description of a todo cannot be empty.");
        }
        Todo t = new Todo(desc);
        tasks.add(t);
        showAddedTaskBox(t);
    }

    private void handleDeadline(String remainder) throws LeoException {
        String payload = remainder == null ? "" : remainder.trim(); // "<desc> /by <time>"
        int pos = lastKeyword(payload, "/by");
        if (payload.isEmpty() || pos <= 0 || pos + 3 >= payload.length()) {
            throw new LeoException("Usage: deadline <description> /by <time>");
        }
        String desc = payload.substring(0, pos).trim();
        String by = payload.substring(pos + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new LeoException("Usage: deadline <description> /by <time>");
        }
        Deadline t = new Deadline(desc, by);
        tasks.add(t);
        showAddedTaskBox(t);
    }

    private void handleEvent(String remainder) throws LeoException {
        String payload = remainder == null ? "" : remainder.trim();
        if (payload.isEmpty()) {
            throw new LeoException("Usage: event <description> /from <start> /to <end>");
        }
        int posFrom = lastKeyword(payload, "/from");
        int posTo   = lastKeyword(payload, "/to");
        int posAt   = lastKeyword(payload, "/at");

        if (posFrom > 0 && posTo > posFrom) {
            String desc = payload.substring(0, posFrom).trim();
            String from = payload.substring(posFrom + 5, posTo).trim();
            String to   = payload.substring(posTo + 3).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new LeoException("Usage: event <description> /from <start> /to <end>");
            }
            Event t = new Event(desc, from, to);
            tasks.add(t);
            showAddedTaskBox(t);
            return;
        }
        if (posAt > 0) {
            String desc = payload.substring(0, posAt).trim();
            String at   = payload.substring(posAt + 3).trim();
            if (desc.isEmpty() || at.isEmpty()) {
                throw new LeoException("Usage: event <description> /from <start> /to <end>");
            }
            Event t = new Event(desc, at, null);
            tasks.add(t);
            showAddedTaskBox(t);
            return;
        }
        throw new LeoException("Usage: event <description> /from <start> /to <end>");
    }

    private void showAddedTaskBox(Task t) {
        ui.showBox(
                "Got it. I've added this task:",
                "  " + t.toString(),
                "Now you have " + tasks.size() + " tasks in the list."
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
