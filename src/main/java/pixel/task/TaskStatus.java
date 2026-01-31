package pixel.task;

/**
 * Enumeration representing the completion status of a task.
 */
public enum TaskStatus {
    DONE("X"), NOT_DONE(" ");

    private final String icon;

    TaskStatus(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
