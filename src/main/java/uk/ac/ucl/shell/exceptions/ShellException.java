package uk.ac.ucl.shell.exceptions;

/**
 * Exception to be thrown by shell with a correctly formatted error message according to shell specification
 */
public class ShellException extends Exception {
    /**
     * error message with prefix removed
     */
    private final String subMessage;

    /**
     * Throws error silently
     */
    public ShellException() {
        super("");
        this.subMessage = "";
    }

    /**
     * Throws error and formats error message to shell specification
     *
     * @param errorMessage
     *         Message explaining error reason
     */
    public ShellException(String errorMessage) {
        super("COMP0010 shell: " + errorMessage);
        this.subMessage = errorMessage;
    }

    /**
     * Returns error message with prefix "COMP0010 shell:" removed
     *
     * @return error message with prefix "COMP0010 shell:" removed
     */
    public String getSubMessage() {
        return this.subMessage;
    }
}