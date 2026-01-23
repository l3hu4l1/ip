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
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println(" added: " + input);
            }

            System.out.println(line);
        }

        scanner.close();
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
