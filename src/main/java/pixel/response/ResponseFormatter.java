package pixel.response;

import java.util.ArrayList;

import pixel.task.Task;

/**
 * Provides methods to generate formatted response messages.
 */
public class ResponseFormatter {
    private String formatTaskList(ArrayList<Task> tasks, String header) {
        StringBuilder sb = new StringBuilder(header);
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String getFarewellMessage() {
        return "Bye. Hope to see you again soon!";
    }

    public String getTaskAddedMessage(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }

    public String getListMessage(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "Your task list is empty.";
        }
        return formatTaskList(tasks, "Here are the tasks in your list:\n");
    }

    public String getTaskMarkedMessage(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    public String getTaskUnmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns a message when a task is deleted.
     *
     * @param task The task that was deleted
     * @param taskCount The remaining number of tasks
     * @return The formatted message
     */
    public String getTaskDeletedMessage(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Returns a message for search results.
     *
     * @param tasks The list of matching tasks
     */
    public String getSearchResultsMessage(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return "No matching tasks found.";
        }
        return formatTaskList(tasks, "Here are the matching tasks in your list:\n");
    }
}
