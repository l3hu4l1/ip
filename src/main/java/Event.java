import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
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
