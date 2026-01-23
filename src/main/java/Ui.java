public class Ui {
    private static final String logo = "  ____  _          _  \n"
            + "|  _ \\(_)_  _____| | \n"
            + "| |_) | \\ \\/ / _ \\ | \n"
            + "|  __/| |>  <  __/ | \n"
            + "|_|   |_/_/\\_\\___|_|";
    private static final String line = "____________________________________________________________";

    public void printWelcome() {
        System.out.println(line);
        System.out.println(" Hello! I'm\n" + logo);
        System.out.println(" What can I do for you?");
        System.out.println(line);
    }

    public void printFarewell() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }

    public void printError(String message) {
        System.out.println(" " + message);
    }

    public String getLine() {
        return line;
    }

    public void printTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    public void printList(java.util.ArrayList<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    public void printTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    public void printTaskUnmarked(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    public void printTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }
}
