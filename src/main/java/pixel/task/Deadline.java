package pixel.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline (a specific date and time by which it must be completed).
 */
public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task with the specified description and deadline.
     *
     * @param description The description of the task
     * @param by The deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Gets the deadline date and time.
     *
     * @return The deadline as a LocalDateTime
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        return "[" + getTaskType().getCode() + "][" + getStatusIcon() + "] " + description + " (by: "
                + by.format(OUTPUT_FORMATTER) + ")";
    }
}
