package pixel.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import pixel.exception.PixelException;
import pixel.task.Deadline;
import pixel.task.Event;
import pixel.task.Task;
import pixel.task.Todo;

/**
 * Class responsible for loading and saving tasks to a file.
 */
public class Storage {
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final Path filePath;

    /**
     * Creates a new Storage instance with the specified file path.
     *
     * @param filePath The path to the file where tasks will be saved/loaded
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file. Creates the directory structure if it
     * doesn't exist.
     *
     * @return An ArrayList of tasks loaded from the file, or an empty list if the file doesn't exist
     * @throws PixelException If there's an error reading the file
     */
    public ArrayList<Task> load() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            ensureDirectoryExists();

            if (!Files.exists(filePath)) {
                assert tasks != null : "Task list should never be null";
                return tasks;
            }

            File file = filePath.toFile();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new PixelException("Error loading tasks from file: " + e.getMessage());
        }

        assert tasks != null : "Task list should never be null after load()";
        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file. Creates the directory
     * structure if it doesn't exist.
     *
     * @throws PixelException If there's an error writing to the file
     */
    public void save(ArrayList<Task> tasks) throws PixelException {
        try {
            ensureDirectoryExists();

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                for (Task task : tasks) {
                    writer.write(formatTask(task) + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new PixelException("Error saving tasks to file: " + e.getMessage());
        }
    }

    private void ensureDirectoryExists() throws IOException {
        Path directory = filePath.getParent();
        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    /**
     * Formats a task into a pipe-delimited string for storage. Format: TYPE |
     * STATUS | DESCRIPTION | [EXTRA_FIELDS]
     */
    private String formatTask(Task task) {
        String type = task.getTaskType().getCode();
        String done = task.getStatusIcon().equals("X") ? "1" : "0";

        if (task instanceof Todo) {
            return type + " | " + done + " | " + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return type + " | " + done + " | " + deadline.getDescription() + " | "
                                            + deadline.getBy().format(STORAGE_FORMATTER);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return type + " | " + done + " | " + event.getDescription() + " | "
                                            + event.getFrom().format(STORAGE_FORMATTER) + " | "
                                            + event.getTo().format(STORAGE_FORMATTER);
        }

        return "";
    }

    /**
     * Parses a line from the storage file into a Task object. Handles each task
     * type by its specific format.
     *
     * @return The parsed Task object, or null if parsing fails
     */
    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        assert type != null : "Task type should not be null";

        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        assert description != null : "Task description should not be null";

        Task task = null;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length >= 4) {
                try {
                    LocalDateTime by = LocalDateTime.parse(parts[3], STORAGE_FORMATTER);
                    assert by != null : "Parsed deadline date should not be null";
                    task = new Deadline(description, by);
                } catch (Exception e) {
                    return null; // If parsing fails, skip this task
                }
            }
            break;
        case "E":
            if (parts.length >= 5) {
                try {
                    LocalDateTime from = LocalDateTime.parse(parts[3], STORAGE_FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(parts[4], STORAGE_FORMATTER);
                    assert from != null : "Parsed event start date should not be null";
                    assert to != null : "Parsed event end date should not be null";
                    task = new Event(description, from, to);
                } catch (Exception e) {
                    return null;
                }
            }
            break;

        default:
            return null;
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}
