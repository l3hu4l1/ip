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

public class Storage {
    private final Path filePath;
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public ArrayList<Task> load() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Path directory = filePath.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // If file doesn't exist, return empty list
            if (!Files.exists(filePath)) {
                return tasks;
            }

            File file = filePath.toFile();
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }

            scanner.close();
        } catch (IOException e) {
            throw new PixelException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws PixelException {
        try {
            Path directory = filePath.getParent();
            if (directory != null && !Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            FileWriter writer = new FileWriter(filePath.toFile());

            for (Task task : tasks) {
                writer.write(formatTask(task) + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            throw new PixelException("Error saving tasks to file: " + e.getMessage());
        }
    }

    private String formatTask(Task task) {
        String type = task.getTaskType().getCode();
        String done = task.getStatusIcon().equals("X") ? "1" : "0";

        if (task instanceof Todo) {
            return type + " | " + done + " | " + ((Todo) task).description;
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return type + " | " + done + " | " + deadline.description + " | " + deadline.by;
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return type + " | " + done + " | " + event.description + " | " + event.from + " | " + event.to;
        }

        return "";
    }

    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length >= 4) {
                try {
                    LocalDateTime by = LocalDateTime.parse(parts[3], STORAGE_FORMATTER);
                    task = new Deadline(description, by);
                } catch (Exception e) {
                    // If parsing fails, skip this task
                    return null;
                }
            }
            break;
        case "E":
            if (parts.length >= 5) {
                task = new Event(description, parts[3], parts[4]);
            }
            break;
        }

        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }
}
