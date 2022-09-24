package uk.ac.ucl.shell.exceptions;

/**
 * Exception to be thrown by unsafe applications with a correctly formatted error message according to shell
 * specification
 *
 * @see ApplicationException
 */
public class UnsafeApplicationException extends ShellException {
    /**
     * Throws error and gets appropriate error message from ApplicationException
     *
     * @param e
     *         Regular ApplicationException to be decorated
     *
     * @throws UnsafeApplicationException
     *         error in processing of application
     */
    public UnsafeApplicationException(ApplicationException e) throws UnsafeApplicationException {
        String message = e.getSubMessage();
        if (message.equals("")) {
            throw new UnsafeApplicationException();
        } else {
            throw new UnsafeApplicationException(message);
        }
    }

    /**
     * Throws error using message provided
     *
     * @param message
     *         String to be shown as error message
     */
    private UnsafeApplicationException(String message) {
        super(message);
    }

    /**
     * Throws error silently
     */
    private UnsafeApplicationException() {
        super();
    }
}