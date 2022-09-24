package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Prints its arguments separated by spaces and followed by a newline to stdout.
 */
public class Echo extends AbstractApplication {
    /**
     * Constructor that creates an Echo application object using the super constructor InputtedApplication
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
    public Echo(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        boolean atLeastOnePrinted = false;
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : appArgs) {
            stringBuilder.append(arg).append(" ");
            atLeastOnePrinted = true;
        }
        if (atLeastOnePrinted) {
            write(stringBuilder.toString().strip());
        }
    }
}