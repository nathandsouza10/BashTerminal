package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.*;
import java.util.ArrayList;

/**
 * Concatenates the content of given files and prints it to stdout. FILE(s) is the name(s) of the file(s) to
 * concatenate. If no files are specified, uses stdin.
 */
public class Cat extends AbstractApplication {
    /**
     * Constructor that creates a Cut application object
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
    public Cat(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
               String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        if (appArgs.isEmpty() && input == null) {
            throw new ApplicationException("missing arguments", this);
        }
        writeToTerminal(appArgs, currentDirectory);
    }

    /**
     * This method will read the contents of the given file and write it to the terminal. If the file does not exist
     * then an error message is thrown appropriately.
     *
     * @param appArgs
     *         A List of all arguments required for the application
     * @param currentDirectory
     *         The current directory the shell is in
     *
     * @throws ApplicationException
     *         The error that is thrown to the terminal when the results of this application cannot be printed
     */
    private void writeToTerminal(ArrayList<String> appArgs, String currentDirectory) throws ApplicationException {
        for (String arg : appArgs) {
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(currentDirectory + File.separator + arg);
            } catch (FileNotFoundException e) {
                throw new ApplicationException("does not exist", this, arg);
            }
            write(fileInputStream);
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("could not close file", this);
            }
        }
        if (input != null) {
            write(input);
        }
    }
}