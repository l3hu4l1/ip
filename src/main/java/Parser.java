public class Parser {
    public static String parseTodoDescription(String input) {
        return input.substring(5);
    }

    public static String parseDeadlineDescription(String input) {
        String rest = input.substring(9);
        int byIndex = rest.indexOf("/by ");
        return rest.substring(0, byIndex);
    }

    public static String parseDeadlineBy(String input) {
        String rest = input.substring(9);
        int byIndex = rest.indexOf("/by ");
        return rest.substring(byIndex + 4);
    }

    public static String parseEventDescription(String input) {
        String rest = input.substring(6);
        int fromIndex = rest.indexOf("/from ");
        return rest.substring(0, fromIndex);
    }

    public static String parseEventFrom(String input) {
        String rest = input.substring(6);
        int fromIndex = rest.indexOf("/from ");
        int toIndex = rest.indexOf("/to ");
        return rest.substring(fromIndex + 6, toIndex);
    }

    public static String parseEventTo(String input) {
        String rest = input.substring(6);
        int toIndex = rest.indexOf("/to ");
        return rest.substring(toIndex + 4);
    }

    public static int parseMarkIndex(String input) {
        return Integer.parseInt(input.substring(5)) - 1;
    }

    public static int parseUnmarkIndex(String input) {
        return Integer.parseInt(input.substring(7)) - 1;
    }
}
