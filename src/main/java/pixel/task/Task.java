package pixel.task;

/**
 * Abstract base class representing a task.
 */
public abstract class Task {
    protected String description;
    protected TaskStatus status;

    /**
     * Creates a new Task with the specified description.
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Gets the status icon indicating whether the task is done.
     *
     * @return "X" if task is done, " " otherwise
     */
    public String getStatusIcon() {
        return status.getIcon();
    }

    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    public void markAsNotDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    public abstract TaskType getTaskType();

    @Override
    public String toString() {
        return "[" + getTaskType().getCode() + "][" + getStatusIcon() + "] " + description;
    }
}
