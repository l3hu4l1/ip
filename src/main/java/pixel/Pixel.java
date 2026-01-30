package pixel;

import pixel.exception.PixelException;
import pixel.parser.Parser;
import pixel.storage.Storage;
import pixel.task.Deadline;
import pixel.task.Event;
import pixel.task.Task;
import pixel.task.Todo;
import pixel.ui.Ui;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Pixel {
    private static final String FILE_PATH = "./data/pixel.txt";

    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();
        Ui ui = new Ui();
        Storage storage = new Storage(FILE_PATH);

        ui.printWelcome();

        try {
            tasks = storage.load();
        } catch (PixelException e) {
            ui.printError(e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            ui.printLine();

            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ui.printList(tasks);
                } else if (input.startsWith("mark")) {
                    int taskIndex = Parser.parseMarkIndex(input);
                    validateTaskIndex(taskIndex, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    ui.printTaskMarked(tasks.get(taskIndex));
                    storage.save(tasks);
                } else if (input.startsWith("unmark")) {
                    int taskIndex = Parser.parseUnmarkIndex(input);
                    validateTaskIndex(taskIndex, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    ui.printTaskUnmarked(tasks.get(taskIndex));
                    storage.save(tasks);
                } else if (input.startsWith("todo")) {
                    String description = Parser.parseTodoDescription(input);
                    Task task = new Todo(description);
                    tasks.add(task);
                    ui.printTaskAdded(task, tasks.size());
                    storage.save(tasks);
                } else if (input.startsWith("deadline")) {
                    String description = Parser.parseDeadlineDescription(input);
                    LocalDateTime by = Parser.parseDeadlineBy(input);
                    Task task = new Deadline(description, by);
                    tasks.add(task);
                    ui.printTaskAdded(task, tasks.size());
                    storage.save(tasks);
                } else if (input.startsWith("event")) {
                    String description = Parser.parseEventDescription(input);
                    LocalDateTime from = Parser.parseEventFrom(input);
                    LocalDateTime to = Parser.parseEventTo(input);
                    Task task = new Event(description, from, to);
                    tasks.add(task);
                    ui.printTaskAdded(task, tasks.size());
                    storage.save(tasks);
                } else if (input.startsWith("delete")) {
                    int taskIndex = Parser.parseDeleteIndex(input);
                    validateTaskIndex(taskIndex, tasks.size());
                    Task task = tasks.remove(taskIndex);
                    ui.printTaskDeleted(task, tasks.size());
                    storage.save(tasks);
                } else {
                    throw new PixelException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (PixelException e) {
                ui.printError(e.getMessage());
            }

            ui.printLine();
        }

        scanner.close();
        ui.printFarewell();
    }

    private static void validateTaskIndex(int taskIndex, int taskCount) throws PixelException {
        if (taskIndex < 0 || taskIndex >= taskCount) {
            throw new PixelException("OOPS!!! The task index is invalid.");
        }
    }
}
