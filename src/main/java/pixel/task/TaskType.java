package pixel.task;

/**
 * Enumeration representing the different types of tasks.
 * Each type has a single-letter code used for storage and display.
 */
public enum TaskType {
    TODO("T"), DEADLINE("D"), EVENT("E");

    private final String code;

    TaskType(String code) {
        this.code = code;
    }

    /**
     * Gets the single-letter code for this task type.
     *
     * @return The task type code ("T", "D", or "E")
     */
    public String getCode() {
        return code;
    }
}
