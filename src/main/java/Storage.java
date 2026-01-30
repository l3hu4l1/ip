import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final Path filePath;

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
                task = new Deadline(description, parts[3]);
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
