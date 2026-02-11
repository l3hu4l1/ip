package pixel.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start time and end time.
 */
public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates a new Event task with the specified description, start time, and end time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        assert from != null : "Event start date/time cannot be null";
        assert to != null : "Event end date/time cannot be null";
        assert from.isBefore(to) || from.isEqual(to) : "Event start time should not be after end time";
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the start date and time of the event.
     */
    public LocalDateTime getFrom() {
        assert from != null : "Event start date/time should never be null after construction";
        return from;
    }

    /**
     * Gets the end date and time of the event.
     */
    public LocalDateTime getTo() {
        assert to != null : "Event end date/time should never be null after construction";
        return to;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        return "[" + getTaskType().getCode() + "][" + getStatusIcon() + "] " + description + " (from: "
                + from.format(OUTPUT_FORMATTER) + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}
