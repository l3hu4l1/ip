package pixel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import pixel.exception.PixelException;
import pixel.storage.Storage;
import pixel.task.Deadline;
import pixel.task.Event;
import pixel.task.Task;
import pixel.task.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private Path testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_pixel.txt");
        storage = new Storage(testFilePath.toString());
    }

    @Test
    public void storage_saveAndLoad_emptyList_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(0, loadedTasks.size());
    }

    @Test
    public void storage_saveAndLoad_singleTodo_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("buy milk"));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertEquals("[T][ ] buy milk", loadedTasks.get(0).toString());
    }

    @Test
    public void storage_saveAndLoad_singleDeadline_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        LocalDateTime deadline = LocalDateTime.of(2026, 6, 15, 18, 0);
        tasks.add(new Deadline("submit assignment", deadline));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.get(0).toString().contains("submit assignment"));
        assertTrue(loadedTasks.get(0).toString().contains("Jun 15 2026 18:00"));
    }

    @Test
    public void storage_saveAndLoad_singleEvent_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        LocalDateTime from = LocalDateTime.of(2026, 8, 20, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 8, 20, 16, 0);
        tasks.add(new Event("project meeting", from, to));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.get(0).toString().contains("project meeting"));
        assertTrue(loadedTasks.get(0).toString().contains("Aug 20 2026"));
    }

    @Test
    public void storage_saveAndLoad_multipleTasks_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("task 1"));
        tasks.add(new Todo("task 2"));
        LocalDateTime deadline = LocalDateTime.of(2026, 12, 31, 23, 59);
        tasks.add(new Deadline("task 3", deadline));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(3, loadedTasks.size());
    }

    @Test
    public void storage_saveAndLoad_markedTask_preservesStatus() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Todo("completed task");
        task.markAsDone();
        tasks.add(task);
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertEquals("X", loadedTasks.get(0).getStatusIcon());
    }

    @Test
    public void storage_saveAndLoad_unmarkedTask_preservesStatus() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        Task task = new Todo("incomplete task");
        tasks.add(task);
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertEquals(" ", loadedTasks.get(0).getStatusIcon());
    }

    @Test
    public void storage_load_nonExistentFile_returnsEmptyList() throws PixelException {
        Path nonExistentPath = tempDir.resolve("nonexistent.txt");
        Storage nonExistentStorage = new Storage(nonExistentPath.toString());

        ArrayList<Task> loadedTasks = nonExistentStorage.load();
        assertNotNull(loadedTasks);
        assertEquals(0, loadedTasks.size());
    }

    @Test
    public void storage_saveAndLoad_taskDescriptionWithSpecialChars_success() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("buy milk & bread @ 5pm!"));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.get(0).toString().contains("buy milk & bread @ 5pm!"));
    }

    @Test
    public void storage_save_overwritesExistingFile() throws PixelException {
        ArrayList<Task> tasks1 = new ArrayList<>();
        tasks1.add(new Todo("task 1"));
        tasks1.add(new Todo("task 2"));
        storage.save(tasks1);

        ArrayList<Task> tasks2 = new ArrayList<>();
        tasks2.add(new Todo("new task"));
        storage.save(tasks2);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(1, loadedTasks.size());
        assertTrue(loadedTasks.get(0).toString().contains("new task"));
    }

    @Test
    public void storage_load_corruptedFile_handlesGracefully() throws IOException, PixelException {
        Files.writeString(testFilePath, "corrupted data\nmore corrupted data\n");

        ArrayList<Task> loadedTasks = storage.load();
        // Should return empty list or skip corrupted entries
        assertNotNull(loadedTasks);
    }

    @Test
    public void storage_saveAndLoad_mixedTaskTypes_preservesOrder() throws PixelException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("first"));
        LocalDateTime deadline = LocalDateTime.of(2026, 3, 15, 10, 30);
        tasks.add(new Deadline("second", deadline));
        LocalDateTime from = LocalDateTime.of(2026, 4, 1, 9, 0);
        LocalDateTime to = LocalDateTime.of(2026, 4, 1, 17, 0);
        tasks.add(new Event("third", from, to));
        storage.save(tasks);

        ArrayList<Task> loadedTasks = storage.load();
        assertEquals(3, loadedTasks.size());
        assertTrue(loadedTasks.get(0).toString().contains("first"));
        assertTrue(loadedTasks.get(1).toString().contains("second"));
        assertTrue(loadedTasks.get(2).toString().contains("third"));
    }
}
