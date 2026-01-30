package pixel.ui;

import pixel.task.Task;

import java.util.ArrayList;

public class Ui {
    private static final String LOGO = "  ____  _          _  \n"
            + "|  _ \\(_)_  _____| | \n"
            + "| |_) | \\ \\/ / _ \\ | \n"
            + "|  __/| |>  <  __/ | \n"
            + "|_|   |_/_/\\_\\___|_|";
    private static final String LINE = "____________________________________________________________";

    public void printWelcome() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm\n" + LOGO);
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public void printFarewell() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public void printError(String message) {
        System.out.println(" " + message);
    }

    public void printLine() {
        System.out.println(LINE);
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
