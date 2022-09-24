package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.*;
import java.util.ArrayList;

/**
 * Defines structure and common methods for sort and uniq applications due to shared functionality
 */
public abstract class AbstractSortUniq extends AbstractApplication {
    /**
     * character to be used to check to use special behaviour (reverse for sort, ignore case for uniq)
     */
    private final char specialCaseIndicatorCharacter;

    /**
     * Constructor that creates an application
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
     * @param specialCaseIndicatorCharacter
     *         character to be used to check to use special behaviour (reverse for sort, ignore case for uniq)
     */
    public AbstractSortUniq(String name, InputStream input, OutputStream output, ArrayList<String> appArgs,
                            String currentDirectory, char specialCaseIndicatorCharacter) {
        super(name, input, output, appArgs, currentDirectory);
        this.specialCaseIndicatorCharacter = specialCaseIndicatorCharacter;
    }

    @Override protected void doOperations() throws ApplicationException {
        validateArguments(appArgs);
        if (input == null) {
            File headFile;
            String headArg;
            boolean toBeReversed;
            if (appArgs.size() == 2) {
                headFile = new File(currentDirectory + File.separator + appArgs.get(1));
                headArg = appArgs.get(1);
                toBeReversed = true;
            } else {
                headFile = new File(currentDirectory + File.separator + appArgs.get(0));
                headArg = appArgs.get(0);
                toBeReversed = false;
            }
            if (!headFile.exists()) {
                throw new ApplicationException("does not exist", this, headArg);
            }
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(headFile);
            } catch (FileNotFoundException e) {
                throw new ApplicationException("does not exist", this, headArg);
            }
            handle(fileInputStream, toBeReversed);
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new ApplicationException("could not close file", this);
            }
        } else {
            handle(input, appArgs.size() == 1);
        }
    }

    /**
     * Validates the arguments passed to the application
     *
     * @param appArgs
     *         List of arguments
     *
     * @throws ApplicationException
     *         Thrown if arguments are invalid
     */
    private void validateArguments(ArrayList<String> appArgs) throws ApplicationException {
        if (appArgs.isEmpty() && input == null) {
            throw new ApplicationException("missing arguments", this);
        } else if (input == null && appArgs.size() != 1 && appArgs.size() != 2 ||
                   input != null && appArgs.size() != 0 && appArgs.size() != 1) {
            throw new ApplicationException("wrong number of arguments", this);
        } else if (input == null &&
                   appArgs.size() == 2 &&
                   !appArgs.get(0).equals("-" + specialCaseIndicatorCharacter) ||
                   input != null &&
                   appArgs.size() == 1 &&
                   !appArgs.get(0).equals("-" + specialCaseIndicatorCharacter)) {
            throw new ApplicationException("wrong argument " + appArgs.get(0), this);
        }
    }

    /**
     * Performs application specific processing
     *
     * @param inputStream
     *         Stream being read from
     * @param specialCase
     *         Indicates to perform special version of operation
     *
     * @throws ApplicationException
     *         Thrown if results cannot be printed to terminal
     */
    protected abstract void handle(InputStream inputStream, boolean specialCase) throws ApplicationException;
}