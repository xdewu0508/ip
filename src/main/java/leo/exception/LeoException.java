package leo.exception;

/**
 * LeoException is the custom exception class for the Leo chatbot.
 * It is thrown when user input is invalid or when operations fail.
 */
public class LeoException extends Exception {
    /**
     * Constructs a LeoException with the specified error message.
     *
     * @param message the error message describing the exception
     */
    public LeoException(String message) {
        super(message);
    }
}
