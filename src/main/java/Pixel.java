import java.util.Scanner;

public class Pixel {
    public static void main(String[] args) {
        String logo = " ____  _          _  \n"
                + "|  _ \\(_)_  _____| | \n"
                + "| |_) | \\ \\/ / _ \\ | \n"
                + "|  __/| |>  <  __/ | \n"
                + "|_|   |_/_/\\_\\___|_| \n";
        String line = "____________________________________________________________";

        System.out.println(line);
        System.out.println(" Hello! I'm\n" + logo);
        System.out.println(" What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();
            System.out.println(line);
            System.out.println(" " + input);
            System.out.println(line);

            if (input.equals("bye")) {
                break;
            }
        }

        scanner.close();
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(line);
    }
}
