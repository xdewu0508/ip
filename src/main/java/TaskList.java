import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public void add(Task t) {
        tasks.add(t);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int oneBasedIndex) {
        return tasks.get(oneBasedIndex - 1);
    }

    public Task remove(int oneBasedIndex) {
        return tasks.remove(oneBasedIndex - 1);
    }

    public String formatListForBox() {
        StringBuilder sb = new StringBuilder();
        if (tasks.isEmpty()) {
            sb.append("You have no tasks in your list.").append(System.lineSeparator());
        } else if (tasks.size() == 1) {
            sb.append("Here is the task in your list:").append(System.lineSeparator());
        } else {
            sb.append("Here are the tasks in your list:").append(System.lineSeparator());
        }
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(".").append(tasks.get(i).toString());
            if (i < tasks.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
