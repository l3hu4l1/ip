public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String toString() {
        return "[" + getTaskType().getCode() + "][" + getStatusIcon() + "] " + description + " (by: " + by + ")";
    }
}
