import java.util.Scanner;

public class Pixel {
    public static void main(String[] args) {
        String logo = " ____  _          _  \n"
                + "|  _ \\(_)_  _____| | \n"
                + "| |_) | \\ \\/ / _ \\ | \n"
                + "|  __/| |>  <  __/ | \n"
                + "|_|   |_/_/\\_\\___|_| \n";
        String line = "____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;
        Ui ui = new Ui();

        System.out.println(line);
        System.out.println(" Hello! I'm\n" + logo);
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            System.out.println(line);

            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Parser.parseMarkIndex(input);
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Parser.parseUnmarkIndex(input);
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (input.startsWith("todo ")) {
                String description = Parser.parseTodoDescription(input);
                tasks[taskCount] = new Todo(description);
                taskCount++;
                ui.printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                String description = Parser.parseDeadlineDescription(input);
                String by = Parser.parseDeadlineBy(input);
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                ui.printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("event ")) {
                String description = Parser.parseEventDescription(input);
                String from = Parser.parseEventFrom(input);
                String to = Parser.parseEventTo(input);
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                ui.printTaskAdded(tasks[taskCount - 1], taskCount);
            }

            System.out.println(line);
        }

        scanner.close();
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
