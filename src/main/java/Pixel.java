import java.util.Scanner;

public class Pixel {
    public static void main(String[] args) {
        Task[] tasks = new Task[100];
        int taskCount = 0;
        Ui ui = new Ui();

        ui.printWelcome();

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            System.out.println(ui.getLine());

            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    ui.printList(tasks, taskCount);
                } else if (input.startsWith("mark")) {
                    int taskIndex = Parser.parseMarkIndex(input);
                    validateTaskIndex(taskIndex, taskCount);
                    tasks[taskIndex].markAsDone();
                    ui.printTaskMarked(tasks[taskIndex]);
                } else if (input.startsWith("unmark")) {
                    int taskIndex = Parser.parseUnmarkIndex(input);
                    validateTaskIndex(taskIndex, taskCount);
                    tasks[taskIndex].markAsNotDone();
                    ui.printTaskUnmarked(tasks[taskIndex]);
                } else if (input.startsWith("todo")) {
                    String description = Parser.parseTodoDescription(input);
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    ui.printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (input.startsWith("deadline")) {
                    String description = Parser.parseDeadlineDescription(input);
                    String by = Parser.parseDeadlineBy(input);
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    ui.printTaskAdded(tasks[taskCount - 1], taskCount);
                } else if (input.startsWith("event")) {
                    String description = Parser.parseEventDescription(input);
                    String from = Parser.parseEventFrom(input);
                    String to = Parser.parseEventTo(input);
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount++;
                    ui.printTaskAdded(tasks[taskCount - 1], taskCount);
                } else {
                    throw new PixelException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (PixelException e) {
                ui.printError(e.getMessage());
            }

            System.out.println(ui.getLine());
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
