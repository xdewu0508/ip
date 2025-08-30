public class Parser {
    private final Ui ui;
    private final TaskList tasks;

    public Parser(Ui ui, TaskList tasks) {
        this.ui = ui;
        this.tasks = tasks;
    }

    public void handle(String line) {
        if (line.isEmpty()) {
            return;
        }

        if (line.equals("list")) {
            if (tasks.size() == 0) {
                ui.showBox("You have no tasks in your list.");
            } else if( tasks.size() == 1) {
                ui.showBoxLine("Here is the task in your list:\n 1." + tasks.get(1));
            } else {
                ui.showBox(tasks.formatListForBox().split("\\R"));
            }
            return;
        }

        if (line.startsWith("mark ")) {
            Integer idx = parseIndex(line.substring(5));
            if (!isValidIndex(idx)) {
                ui.showBoxLine("Please give a valid task number to mark.");
                return;
            }
            Task t = tasks.get(idx);
            t.mark();
            ui.showBox(
                    "Nice! I've marked this task as done:",
                    "  " + t
            );
            return;
        }

        if (line.startsWith("unmark ")) {
            Integer idx = parseIndex(line.substring(7));
            if (!isValidIndex(idx)) {
                ui.showBoxLine("Please give a valid task number to unmark.");
                return;
            }
            Task t = tasks.get(idx);
            t.unmark();
            ui.showBox(
                    "OK, I've marked this task as not done yet:",
                    "  " + t
            );
            return;
        }

        if (line.startsWith("todo")) {
            String desc = line.length() > 4 ? line.substring(4).trim() : "";
            if (desc.isEmpty()) {
                ui.showBoxLine("The description of a todo cannot be empty.");
                return;
            }
            Todo t = new Todo(desc);
            tasks.add(t);
            showAddedTaskBox(t);
            return;
        }

        if (line.startsWith("deadline")) {
            String payload = line.length() > 8 ? line.substring(8).trim() : "";
            int pos = lastKeyword(payload, "/by");
            if (payload.isEmpty() || pos <= 0 || pos + 3 >= payload.length()) {
                ui.showBoxLine("Usage: deadline <description> /by <time>");
                return;
            }
            String desc = payload.substring(0, pos).trim();
            String by = payload.substring(pos + 3).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                ui.showBoxLine("Usage: deadline <description> /by <time>");
                return;
            }
            Deadline t = new Deadline(desc, by);
            tasks.add(t);
            showAddedTaskBox(t);
            return;
        }

        if (line.startsWith("event")) {
            String payload = line.length() > 5 ? line.substring(5).trim() : "";
            if (payload.isEmpty()) {
                ui.showBoxLine("Usage: event <description> /from <start> /to <end>");
                return;
            }
            String desc, from, to;
            int posFrom = lastKeyword(payload, "/from");
            int posTo = lastKeyword(payload, "/to");
            int posAt = lastKeyword(payload, "/at");

            if (posFrom > 0 && posTo > posFrom) {
                desc = payload.substring(0, posFrom).trim();
                from = payload.substring(posFrom + 5, posTo).trim();
                to   = payload.substring(posTo + 3).trim();
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    ui.showBoxLine("Usage: event <description> /from <start> /to <end>");
                    return;
                }
                Event t = new Event(desc, from, to);
                tasks.add(t);
                showAddedTaskBox(t);
                return;
            } else if (posAt > 0) {
                desc = payload.substring(0, posAt).trim();
                from = payload.substring(posAt + 3).trim();
                if (desc.isEmpty() || from.isEmpty()) {
                    ui.showBoxLine("Usage: event <description> /from <start> /to <end>");
                    return;
                }
                Event t = new Event(desc, from, null);
                tasks.add(t);
                showAddedTaskBox(t);
                return;
            } else {
                ui.showBoxLine("Usage: event <description> /from <start> /to <end>");
                return;
            }
        }

        Todo t = new Todo(line);
        tasks.add(t);
        showAddedTaskBox(t);
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

    private boolean isValidIndex(Integer idx) {
        return idx != null && idx >= 1 && idx <= tasks.size();
    }

    private int lastKeyword(String s, String kw) {
        return s.lastIndexOf(kw);
    }
}
