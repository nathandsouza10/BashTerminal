package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Changes the current working directory. PATH is a relative path to the target directory.
 */
public class Cd extends AbstractApplication {
    /**
     * Constructor that creates a Cd application object using the super constructor InputtedApplication
     *
     * @param setName
     *         Name of the Application
     * @param output
     *         Application output stream
     * @param input
     *         Application input stream
     * @param appArgs
     *         Arguments provided to application
     * @param currentDirectory
     *         Directory application executed in
     */
    public Cd(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
              String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        if (appArgs.isEmpty()) {
            throw new ApplicationException("missing argument", this);
        } else if (appArgs.size() > 1) {
            throw new ApplicationException("too many arguments", this);
        }
    }

    /**
     * This method checks if the directory that has been cd into is valid. If so then the current directory path is
     * updated and if not then an appropriate error message is thrown to the terminal.
     *
     * @throws ApplicationException
     *         The error that is thrown to the terminal when the results of this application cannot be printed
     */
    @Override protected String getUpdatedDirectory() throws ApplicationException {
        String dirString = appArgs.get(0);
        File dir = new File(currentDirectory, dirString);
        if (invalidDir(dir)) {
            throw new ApplicationException(dirString + " is not an existing directory", this);
        }
        try {
            return dir.getCanonicalPath();
        } catch (IOException e) {
            throw new ApplicationException("cannot resolve path for directory due to system permissions", this);
        }
    }
}