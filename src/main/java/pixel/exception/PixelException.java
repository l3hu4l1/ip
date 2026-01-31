package pixel.exception;

/**
 * Custom exception class for Pixel application errors.
 * Used to handle application-specific error conditions such as
 * invalid input, missing parameters, or invalid task indices.
 */
public class PixelException extends Exception {
    /**
     * Creates a new PixelException with the specified error message.
     *
     * @param message The error message describing what went wrong
     */
    public PixelException(String message) {
        super(message);
    }
}
