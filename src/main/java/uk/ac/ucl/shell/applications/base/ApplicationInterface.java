package uk.ac.ucl.shell.applications.base;

import uk.ac.ucl.shell.exceptions.ApplicationException;
import uk.ac.ucl.shell.exceptions.UnsafeApplicationException;

/**
 * Interface defining properties of an application
 */
public interface ApplicationInterface {
    /**
     * Run application and update working directory
     *
     * @return Returns current working directory after application completes execution
     *
     * @throws ApplicationException
     *         Thrown if error occurs while running application
     * @throws UnsafeApplicationException
     *         Thrown if error occurs while running unsafe application
     */
    String run() throws ApplicationException, UnsafeApplicationException;

    /**
     * Returns application name
     *
     * @return Application name
     */
    String getName();
}