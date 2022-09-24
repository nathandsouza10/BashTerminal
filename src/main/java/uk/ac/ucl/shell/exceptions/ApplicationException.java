package uk.ac.ucl.shell.exceptions;

import uk.ac.ucl.shell.applications.base.ApplicationInterface;

/**
 * Exception to be thrown by applications with a correctly formatted error message according to shell specification
 */
public class ApplicationException extends ShellException {
    /**
     * Throws error silently
     */
    public ApplicationException() {
        super();
    }

    /**
     * Throws error and formats error message to shell specification
     *
     * @param errorMessage
     *         Message explaining error reason
     * @param application
     *         The application throwing the error
     */
    public ApplicationException(String errorMessage, ApplicationInterface application) {
        super(application.getName() + ": " + errorMessage);
    }

    /**
     * Throws error and formats error message to shell specification
     *
     * @param errorMessage
     *         Message explaining error reason
     * @param application
     *         The application throwing the error
     * @param fileName
     *         Name of a file which was being interacted with when error thrown
     */
    public ApplicationException(String errorMessage, ApplicationInterface application, String fileName) {
        super(application.getName() + ": " + fileName + ": " + errorMessage);
    }

    /**
     * Throws error and formats error message to shell specification
     *
     * @param errorMessage
     *         Message explaining error reason
     * @param applicationName
     *         Name of application throwing error (intended for use when application name is invalid)
     */
    public ApplicationException(String errorMessage, String applicationName) {
        super(applicationName + ": " + errorMessage);
    }
}