package pixel.task;

/**
 * Represents a todo task without any specific time constraints.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo task with the specified description.
     *
     * @param description The description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }
}
