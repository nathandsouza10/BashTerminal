package uk.ac.ucl.shell.exceptions;

/**
 * Exception to be thrown to trigger shell exit
 */
public class ExitException extends Exception {
    /**
     * Throws error silently
     */
    public ExitException() {
    }
}