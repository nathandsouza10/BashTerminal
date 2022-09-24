package uk.ac.ucl.shell.exceptions;

/**
 * Exception to be thrown if writing to output fails
 */
public class OutputWriteException extends ShellException {
    /**
     * Error thrown when terminal write fails
     */
    public OutputWriteException() {
        super("Cannot write to output");
    }
}