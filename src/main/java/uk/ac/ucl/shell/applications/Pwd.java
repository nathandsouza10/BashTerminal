package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Outputs the current working directory followed by a newline.
 */
public class Pwd extends AbstractApplication {
    /**
     * Constructor that creates a Pwd application object using the super constructor InputtedApplication
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
    public Pwd(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
               String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        write(currentDirectory);
    }
}