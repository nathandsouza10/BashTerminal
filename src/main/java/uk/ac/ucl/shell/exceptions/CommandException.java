package uk.ac.ucl.shell.exceptions;

/**
 * Exception to be thrown by applications with a correctly formatted error message according to shell specification
 */
public class CommandException extends ShellException {
    /**
     * Throws error and formats error message to shell specification
     *
     * @param errorMessage
     *         Message explaining error reason
     */
    public CommandException(String errorMessage) {
        super(errorMessage);
    }
}