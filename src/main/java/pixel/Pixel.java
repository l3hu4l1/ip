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
    private Task pendingTask = null;
    private boolean isAwaitingConfirmation = false;

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
            // Handle confirmation responses
            if (isAwaitingConfirmation) {
                return handleConfirmation(input);
            }

            String command = input.split("\\s+", 2)[0];
            return switch (command) {
            case "bye" -> handleBye();
            case "list" -> responseFormatter.getListMessage(tasks);
            case "mark" -> handleMark(input);
            case "unmark" -> handleUnmark(input);
            case "todo" -> handleTodo(input);
            case "deadline" -> handleDeadline(input);
            case "event" -> handleEvent(input);
            case "delete" -> handleDelete(input);
            case "find" -> handleFind(input);
            default -> throw new PixelException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            };
        } catch (PixelException e) {
            return e.getMessage();
        }
    }

    private String handleBye() {
        toExit = true;
        return responseFormatter.getFarewellMessage();
    }

    private String handleMark(String input) throws PixelException {
        int taskIndex = Parser.parseMarkIndex(input);
        validateTaskIndex(taskIndex, tasks.size());
        tasks.get(taskIndex).markAsDone();
        storage.save(tasks);
        return responseFormatter.getTaskMarkedMessage(tasks.get(taskIndex));
    }

    private String handleUnmark(String input) throws PixelException {
        int taskIndex = Parser.parseUnmarkIndex(input);
        validateTaskIndex(taskIndex, tasks.size());
        tasks.get(taskIndex).markAsNotDone();
        storage.save(tasks);
        return responseFormatter.getTaskUnmarkedMessage(tasks.get(taskIndex));
    }

    private String handleTodo(String input) throws PixelException {
        String description = Parser.parseTodoDescription(input);
        Task task = new Todo(description);

        if (isDuplicated(task)) {
            return getDuplicateMessage(task);
        }

        tasks.add(task);
        storage.save(tasks);
        return responseFormatter.getTaskAddedMessage(task, tasks.size());
    }

    private String handleDeadline(String input) throws PixelException {
        String description = Parser.parseDeadlineDescription(input);
        LocalDateTime by = Parser.parseDeadlineBy(input);
        Task task = new Deadline(description, by);

        if (isDuplicated(task)) {
            return getDuplicateMessage(task);
        }

        tasks.add(task);
        storage.save(tasks);
        return responseFormatter.getTaskAddedMessage(task, tasks.size());
    }

    private String handleEvent(String input) throws PixelException {
        String description = Parser.parseEventDescription(input);
        LocalDateTime from = Parser.parseEventFrom(input);
        LocalDateTime to = Parser.parseEventTo(input);
        Task task = new Event(description, from, to);

        if (isDuplicated(task)) {
            return getDuplicateMessage(task);
        }

        tasks.add(task);
        storage.save(tasks);
        return responseFormatter.getTaskAddedMessage(task, tasks.size());
    }

    private String handleDelete(String input) throws PixelException {
        int taskIndex = Parser.parseDeleteIndex(input);
        validateTaskIndex(taskIndex, tasks.size());
        Task task = tasks.remove(taskIndex);
        storage.save(tasks);
        return responseFormatter.getTaskDeletedMessage(task, tasks.size());
    }

    private String handleFind(String input) throws PixelException {
        String searchCriteria = Parser.parseFindKeyword(input);
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (Parser.matchesSearchCriteria(task.getDescription(), searchCriteria)) {
                matchingTasks.add(task);
            }
        }
        return responseFormatter.getSearchResultsMessage(matchingTasks);
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

    private String handleConfirmation(String input) throws PixelException {
        String response = input.trim().toUpperCase();
        isAwaitingConfirmation = false;

        if (response.equals("Y")) {
            tasks.add(pendingTask);
            storage.save(tasks);
            String result = responseFormatter.getTaskAddedMessage(pendingTask, tasks.size());
            pendingTask = null;
            return result;
        } else if (response.equals("N")) {
            pendingTask = null;
            return "Okay, I won't add the duplicate task.";
        } else {
            isAwaitingConfirmation = true;
            return "Please respond with Y (yes) or N (no).";
        }
    }

    private boolean isDuplicated(Task newTask) {
        return findDuplicate(newTask) != null;
    }

    private Task findDuplicate(Task newTask) {
        for (Task task : tasks) {
            if (areDuplicates(task, newTask)) {
                return task;
            }
        }
        return null;
    }

    private boolean areDuplicates(Task task1, Task task2) {
        // Different types are not duplicates
        if (task1.getTaskType() != task2.getTaskType()) {
            return false;
        }

        // Check description (case-insensitive)
        if (!task1.getDescription().equalsIgnoreCase(task2.getDescription())) {
            return false;
        }

        // For Deadline, check if dates match
        if (task1 instanceof Deadline && task2 instanceof Deadline) {
            Deadline d1 = (Deadline) task1;
            Deadline d2 = (Deadline) task2;
            return d1.getBy().equals(d2.getBy());
        }

        // For Event, check if dates match
        if (task1 instanceof Event && task2 instanceof Event) {
            Event e1 = (Event) task1;
            Event e2 = (Event) task2;
            return e1.getFrom().equals(e2.getFrom()) && e1.getTo().equals(e2.getTo());
        }

        // For Todo, just description match is enough
        return true;
    }

    private String getDuplicateMessage(Task task) {
        pendingTask = task;
        isAwaitingConfirmation = true;
        return "This task already exists in your list:\n  " + findDuplicate(task)
                + "\n\nDo you still want to add it? (Y/N)";
    }

    public boolean toExit() {
        return toExit;
    }
}
