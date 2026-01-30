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
        Task task = null;
        return task;
    }
}
