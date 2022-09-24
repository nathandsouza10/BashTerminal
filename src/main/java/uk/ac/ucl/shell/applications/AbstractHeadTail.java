package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Defines structure and common methods for head and tail applications due to shared functionality
 */
public abstract class AbstractHeadTail extends AbstractApplication {
    /**
     * Constructor that creates an AbstractHeadTail application object using the super constructor InputtedApplication
     *
     * @param name
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
    public AbstractHeadTail(String name, InputStream input, OutputStream output, ArrayList<String> appArgs,
                            String currentDirectory) {
        super(name, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        if (appArgs.isEmpty() && input == null) {
            throw new ApplicationException("missing arguments", this);
        } else if (input == null && appArgs.size() != 1 && appArgs.size() != 3 ||
                   input != null && appArgs.size() != 0 && appArgs.size() != 2) {
            throw new ApplicationException("wrong number of arguments", this);
        } else if (input == null && appArgs.size() == 3 && !appArgs.get(0).equals("-n") ||
                   input != null && appArgs.size() == 2 && !appArgs.get(0).equals("-n")) {
            throw new ApplicationException("wrong argument " + appArgs.get(0), this);
        }
        int lines = 10;
        String arg;
        if (input == null && appArgs.size() == 3 || input != null && appArgs.size() == 2) {
            try {
                lines = Integer.parseInt(appArgs.get(1));
            } catch (Exception e) {
                throw new ApplicationException("wrong argument " + appArgs.get(1), this);
            }
            if (input == null) {
                arg = appArgs.get(2);
            } else {
                arg = "";
            }
        } else {
            if (input == null) {
                arg = appArgs.get(0);
            } else {
                arg = "";
            }
        }
        if (input == null) {
            Path filePath = Paths.get(currentDirectory).resolve(arg);
            if (invalidFile(filePath)) {
                throw new ApplicationException("does not exist", this, arg);
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath.toFile());
                write(getResultsToWrite(fileInputStream, lines));
            } catch (FileNotFoundException e) {
                throw new ApplicationException("cannot open", this, arg);
            }
        } else {
            write(getResultsToWrite(input, lines));
        }
    }

    /**
     * Returns application specific processing results
     *
     * @param inputStream
     *         Stream being read from
     * @param lines
     *         Number of lines to be included
     *
     * @return List of results
     *
     * @throws ApplicationException
     *         thrown if error reading stream
     */
    protected abstract ArrayList<String> getResultsToWrite(InputStream inputStream, int lines)
            throws ApplicationException;
}