package uk.ac.ucl.shell.applications.base;

import uk.ac.ucl.shell.exceptions.ApplicationException;
import uk.ac.ucl.shell.exceptions.UnsafeApplicationException;

/**
 * Decorator to convert exceptions thrown by applications to unsafe form
 *
 * @see ApplicationException
 * @see UnsafeApplicationException
 */
public class UnsafeApplicationDecorator implements ApplicationInterface {
    /**
     * Safe version of command
     */
    private final AbstractApplication command;

    /**
     * Constructor that creates an unsafe
     *
     * @param setCommand
     *         Safe form of application
     */
    UnsafeApplicationDecorator(AbstractApplication setCommand) {
        command = setCommand;
    }

    @Override public String run() throws UnsafeApplicationException {
        try {
            return command.run();
        } catch (ApplicationException e) {
            throw new UnsafeApplicationException(e);
        }
    }

    @Override public String getName() {
        return command.getName();
    }
}