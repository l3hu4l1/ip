public class Parser {
    public static String parseTodoDescription(String input) throws PixelException {
        if (input.length() <= 4) {
            throw new PixelException("OOPS!!! The description of a todo cannot be empty.");
        }
        String description = input.substring(4).trim();
        if (description.isEmpty()) {
            throw new PixelException("OOPS!!! The description of a todo cannot be empty.");
        }
        return description;
    }

    public static String parseDeadlineDescription(String input) throws PixelException {
        String rest = input.length() > 8 ? input.substring(8) : "";
        int byIndex = rest.indexOf("/by ");
        if (byIndex == -1) {
            throw new PixelException("OOPS!!! A deadline must include /by.");
        }
        String description = rest.substring(0, byIndex).trim();
        if (description.isEmpty()) {
            throw new PixelException("OOPS!!! The description of a deadline cannot be empty.");
        }
        return description;
    }

    public static String parseDeadlineBy(String input) throws PixelException {
        String rest = input.length() > 8 ? input.substring(8) : "";
        int byIndex = rest.indexOf("/by ");
        if (byIndex == -1) {
            throw new PixelException("OOPS!!! A deadline must include /by.");
        }
        String by = rest.substring(byIndex + 4).trim();
        if (by.isEmpty()) {
            throw new PixelException("OOPS!!! The deadline time cannot be empty.");
        }
        return by;
    }

    public static String parseEventDescription(String input) throws PixelException {
        String rest = input.length() > 5 ? input.substring(5) : "";
        int fromIndex = rest.indexOf("/from ");
        if (fromIndex == -1) {
            throw new PixelException("OOPS!!! An event must include /from and /to.");
        }
        String description = rest.substring(0, fromIndex).trim();
        if (description.isEmpty()) {
            throw new PixelException("OOPS!!! The description of an event cannot be empty.");
        }
        return description;
    }

    public static String parseEventFrom(String input) throws PixelException {
        String rest = input.length() > 5 ? input.substring(5) : "";
        int fromIndex = rest.indexOf("/from ");
        int toIndex = rest.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            throw new PixelException("OOPS!!! An event must include /from and /to.");
        }
        String from = rest.substring(fromIndex + 6, toIndex).trim();
        if (from.isEmpty()) {
            throw new PixelException("OOPS!!! The start time of an event cannot be empty.");
        }
        return from;
    }

    public static String parseEventTo(String input) throws PixelException {
        String rest = input.length() > 5 ? input.substring(5) : "";
        int toIndex = rest.indexOf("/to ");
        if (toIndex == -1) {
            throw new PixelException("OOPS!!! An event must include /from and /to.");
        }
        String to = rest.substring(toIndex + 4).trim();
        if (to.isEmpty()) {
            throw new PixelException("OOPS!!! The end time of an event cannot be empty.");
        }
        return to;
    }

    public static int parseMarkIndex(String input) throws PixelException {
        if (input.length() <= 5) {
            throw new PixelException("OOPS!!! Please provide a valid task number to mark.");
        }
        try {
            return Integer.parseInt(input.substring(5).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PixelException("OOPS!!! Please provide a valid task number to mark.");
        }
    }

    public static int parseUnmarkIndex(String input) throws PixelException {
        if (input.length() <= 7) {
            throw new PixelException("OOPS!!! Please provide a valid task number to unmark.");
        }
        try {
            return Integer.parseInt(input.substring(7).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PixelException("OOPS!!! Please provide a valid task number to unmark.");
        }
    }
}
