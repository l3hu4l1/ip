package pixel;

import java.time.LocalDateTime;
import java.util.ArrayList;

import pixel.exception.PixelException;
import pixel.parser.Parser;
import pixel.response.ResponseFormatter;
import pixel.storage.Storage;
import pixel.task.Deadline;
import pixel.task.Event;
import pixel.task.Task;
import pixel.task.Todo;

/**
 * Represents top-level logic.
 */
public class Pixel {
    private static final String FILE_PATH = "./data/pixel.txt";
    private static final String welcomeMessage = "Hello! I'm Pixel.\nWhat can I do for you?";
    private final ResponseFormatter responseFormatter;
    private final Storage storage;
    private ArrayList<Task> tasks;
    private boolean toExit = false;

    /**
     * Creates a new Pixel instance and loads existing tasks from storage.
     */
    public Pixel() {
        this.tasks = new ArrayList<>();
        this.responseFormatter = new ResponseFormatter();
        this.storage = new Storage(FILE_PATH);

        try {
            this.tasks = storage.load();
        } catch (PixelException e) {
            // Start with empty task list if loading fails
        }
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    /**
     * Generates a response to the user's input.
     */
    public String getResponse(String input) {
        try {
            String response = null;
            if (input.equals("bye")) {
                toExit = true;
                response = responseFormatter.getFarewellMessage();
            } else if (input.equals("list")) {
                response = responseFormatter.getListMessage(tasks);
            } else if (input.startsWith("mark")) {
                int taskIndex = Parser.parseMarkIndex(input);
                validateTaskIndex(taskIndex, tasks.size());
                tasks.get(taskIndex).markAsDone();
                storage.save(tasks);
                response = responseFormatter.getTaskMarkedMessage(tasks.get(taskIndex));
            } else if (input.startsWith("unmark")) {
                int taskIndex = Parser.parseUnmarkIndex(input);
                validateTaskIndex(taskIndex, tasks.size());
                tasks.get(taskIndex).markAsNotDone();
                storage.save(tasks);
                response = responseFormatter.getTaskUnmarkedMessage(tasks.get(taskIndex));
            } else if (input.startsWith("todo")) {
                String description = Parser.parseTodoDescription(input);
                Task task = new Todo(description);
                tasks.add(task);
                storage.save(tasks);
                response = responseFormatter.getTaskAddedMessage(task, tasks.size());
            } else if (input.startsWith("deadline")) {
                String description = Parser.parseDeadlineDescription(input);
                LocalDateTime by = Parser.parseDeadlineBy(input);
                Task task = new Deadline(description, by);
                tasks.add(task);
                storage.save(tasks);
                response = responseFormatter.getTaskAddedMessage(task, tasks.size());
            } else if (input.startsWith("event")) {
                String description = Parser.parseEventDescription(input);
                LocalDateTime from = Parser.parseEventFrom(input);
                LocalDateTime to = Parser.parseEventTo(input);
                Task task = new Event(description, from, to);
                tasks.add(task);
                storage.save(tasks);
                response = responseFormatter.getTaskAddedMessage(task, tasks.size());
            } else if (input.startsWith("delete")) {
                int taskIndex = Parser.parseDeleteIndex(input);
                validateTaskIndex(taskIndex, tasks.size());
                Task task = tasks.remove(taskIndex);
                storage.save(tasks);
                response = responseFormatter.getTaskDeletedMessage(task, tasks.size());
            } else if (input.startsWith("find")) {
                String keyword = Parser.parseFindKeyword(input);
                ArrayList<Task> matchingTasks = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.getDescription().contains(keyword)) {
                        matchingTasks.add(task);
                    }
                }
                response = responseFormatter.getSearchResultsMessage(matchingTasks);
            } else {
                throw new PixelException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            assert response != null : "Response should never be null";
            return response;
        } catch (PixelException e) {
            String errorMessage = e.getMessage();
            assert errorMessage != null : "Error message should not be null";
            return errorMessage;
        }
    }

    /**
     * Validates that a task index is within bounds.
     *
     * @param taskIndex The zero-based index to validate
     * @param taskCount The total number of tasks in the list
     * @throws PixelException If the index is out of bounds
     */
    private void validateTaskIndex(int taskIndex, int taskCount) throws PixelException {
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new PixelException("OOPS!!! The task index is invalid.");
        }
    }

    public boolean toExit() {
        return toExit;
    }
}
