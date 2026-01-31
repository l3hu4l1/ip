package pixel.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pixel.exception.PixelException;

public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    // Command prefix lengths
    private static final int TODO_PREFIX_LENGTH = 4;
    private static final int MARK_PREFIX_LENGTH = 5;
    private static final int UNMARK_PREFIX_LENGTH = 7;
    private static final int DELETE_PREFIX_LENGTH = 6;
    private static final int FIND_PREFIX_LENGTH = 4;
    private static final int DEADLINE_PREFIX_LENGTH = 8;
    private static final int EVENT_PREFIX_LENGTH = 5;

    // Parameter keyword lengths (including leading slash and trailing space)
    private static final int BY_PREFIX_LENGTH = 4; // "/by "
    private static final int FROM_PREFIX_LENGTH = 6; // "/from "
    private static final int TO_PREFIX_LENGTH = 4; // "/to "

    /**
     * Parses the description from a todo command.
     *
     * @param input The full user input command (e.g., "todo buy milk")
     * @return The description of the todo task
     * @throws PixelException If the description is empty
     */
    public static String parseTodoDescription(String input) throws PixelException {
        if (input.length() <= TODO_PREFIX_LENGTH) {
            throw new PixelException("OOPS!!! The description of a todo cannot be empty.");
        }
        String description = input.substring(TODO_PREFIX_LENGTH).trim();
        if (description.isEmpty()) {
            throw new PixelException("OOPS!!! The description of a todo cannot be empty.");
        }
        return description;
    }

    /**
     * Parses the description from a deadline command.
     *
     * @param input The full user input command (e.g., "deadline return book /by
     *              2026-01-15")
     * @return The description of the deadline task
     * @throws PixelException If the description is empty
     */
    public static String parseDeadlineDescription(String input) throws PixelException {
        String rest = input.substring(DEADLINE_PREFIX_LENGTH);
        int byIndex = rest.indexOf("/by ");
        String description = rest.substring(0, byIndex).trim();
        if (description.isEmpty()) {
            throw new PixelException("OOPS!!! The description of a deadline cannot be empty.");
        }
        return description;
    }

    /**
     * Parses the deadline date/time from a deadline command.
     *
     * @param input The full user input command
     * @return The parsed deadline as a LocalDateTime
     * @throws PixelException If the /by parameter is missing, empty, or has an
     *                        invalid date format
     */
    public static LocalDateTime parseDeadlineBy(String input) throws PixelException {
        String rest = input.length() > DEADLINE_PREFIX_LENGTH ? input.substring(DEADLINE_PREFIX_LENGTH) : "";
        int byIndex = rest.indexOf("/by ");
        if (byIndex == -1) {
            throw new PixelException("OOPS!!! A deadline must include /by.");
        }
        String by = rest.substring(byIndex + BY_PREFIX_LENGTH).trim();
        if (by.isEmpty()) {
            throw new PixelException("OOPS!!! The deadline time cannot be empty.");
        }
        return parseDateTime(by);
    }

    /**
     * Parses a date/time string into a LocalDateTime object.
     * Accepts formats: "yyyy-MM-dd" (defaults to 23:59) or "yyyy-MM-dd HHmm".
     *
     * @param dateTimeStr The date/time string to parse (e.g., "2026-01-15" or
     *                    "2026-01-15 1430")
     * @return The parsed LocalDateTime
     * @throws PixelException If the date format is invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws PixelException {
        try {
            String[] parts = dateTimeStr.split(" ");
            LocalDate date = LocalDate.parse(parts[0], INPUT_DATE_FORMATTER);

            if (parts.length > 1) {
                LocalTime time = LocalTime.parse(parts[1], INPUT_TIME_FORMATTER);
                return LocalDateTime.of(date, time);
            } else {
                // No time provided, default to 23:59
                return LocalDateTime.of(date, LocalTime.of(23, 59));
            }
        } catch (DateTimeParseException e) {
            throw new PixelException("OOPS!!! Invalid date format. Use yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }

    /**
     * Parses the description from an event command.
     *
     * @param input The full user input command (e.g., "event meeting /from ... /to
     *              ...")
     * @return The description of the event task
     * @throws PixelException If the description is empty or /from and /to are
     *                        missing
     */
    public static String parseEventDescription(String input) throws PixelException {
        String rest = input.length() > EVENT_PREFIX_LENGTH ? input.substring(EVENT_PREFIX_LENGTH) : "";
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

    /**
     * Parses the start date/time from an event command.
     *
     * @param input The full user input command
     * @return The parsed start time as a LocalDateTime
     * @throws PixelException If /from or /to is missing, empty, or has an invalid
     *                        date format
     */
    public static LocalDateTime parseEventFrom(String input) throws PixelException {
        String rest = input.length() > EVENT_PREFIX_LENGTH ? input.substring(EVENT_PREFIX_LENGTH) : "";
        int fromIndex = rest.indexOf("/from ");
        int toIndex = rest.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || fromIndex > toIndex) {
            throw new PixelException("OOPS!!! An event must include /from and /to.");
        }
        String from = rest.substring(fromIndex + FROM_PREFIX_LENGTH, toIndex).trim();
        if (from.isEmpty()) {
            throw new PixelException("OOPS!!! The start time of an event cannot be empty.");
        }
        return parseDateTime(from);
    }

    /**
     * Parses the end date/time from an event command.
     *
     * @param input The full user input command
     * @return The parsed end time as a LocalDateTime
     * @throws PixelException If /to is missing, empty, or has an invalid date
     *                        format
     */
    public static LocalDateTime parseEventTo(String input) throws PixelException {
        String rest = input.length() > EVENT_PREFIX_LENGTH ? input.substring(EVENT_PREFIX_LENGTH) : "";
        int toIndex = rest.indexOf("/to ");
        if (toIndex == -1) {
            throw new PixelException("OOPS!!! An event must include /from and /to.");
        }
        String to = rest.substring(toIndex + TO_PREFIX_LENGTH).trim();
        if (to.isEmpty()) {
            throw new PixelException("OOPS!!! The end time of an event cannot be empty.");
        }
        return parseDateTime(to);
    }

    /**
     * Parses the task index from a mark command.
     *
     * @param input The full user input command (e.g., "mark 2")
     * @return The zero-based task index
     * @throws PixelException If the index is missing or not a valid number
     */
    public static int parseMarkIndex(String input) throws PixelException {
        if (input.length() <= MARK_PREFIX_LENGTH) {
            throw new PixelException("OOPS!!! Please provide a valid task number to mark.");
        }
        try {
            return Integer.parseInt(input.substring(MARK_PREFIX_LENGTH).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PixelException("OOPS!!! Please provide a valid task number to mark.");
        }
    }

    /**
     * Parses the task index from an unmark command.
     *
     * @param input The full user input command (e.g., "unmark 2")
     * @return The zero-based task index
     * @throws PixelException If the index is missing or not a valid number
     */
    public static int parseUnmarkIndex(String input) throws PixelException {
        if (input.length() <= UNMARK_PREFIX_LENGTH) {
            throw new PixelException("OOPS!!! Please provide a valid task number to unmark.");
        }
        try {
            return Integer.parseInt(input.substring(UNMARK_PREFIX_LENGTH).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PixelException("OOPS!!! Please provide a valid task number to unmark.");
        }
    }

    /**
     * Parses the task index from a delete command.
     *
     * @param input The full user input command (e.g., "delete 2")
     * @return The zero-based task index
     * @throws PixelException If the index is missing or not a valid number
     */
    public static int parseDeleteIndex(String input) throws PixelException {
        if (input.length() <= DELETE_PREFIX_LENGTH) {
            throw new PixelException("OOPS!!! Please provide a valid task number to delete.");
        }
        try {
            return Integer.parseInt(input.substring(DELETE_PREFIX_LENGTH).trim()) - 1;
        } catch (NumberFormatException e) {
            throw new PixelException("OOPS!!! Please provide a valid task number to delete.");
        }
    }

    /**
     * Parses the keyword from a find command.
     *
     * @param input The full user input command (e.g., "find book")
     * @return The search keyword
     * @throws PixelException If the keyword is empty
     */
    public static String parseFindKeyword(String input) throws PixelException {
        if (input.length() <= FIND_PREFIX_LENGTH) {
            throw new PixelException("OOPS!!! The search keyword cannot be empty.");
        }
        String keyword = input.substring(FIND_PREFIX_LENGTH).trim();
        if (keyword.isEmpty()) {
            throw new PixelException("OOPS!!! The search keyword cannot be empty.");
        }
        return keyword;
    }
}
