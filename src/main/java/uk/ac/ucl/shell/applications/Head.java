package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Prints the first N lines of a given file or stdin. If there are less than N lines, prints only the existing lines
 * without raising an exception. OPTIONS, e.g. -n 15 means printing the first 15 lines. If not specified, prints the
 * first 10 lines. FILE is the name of the file. If not specified, uses stdin.
 */
public class Head extends AbstractHeadTail {
    /**
     * Constructor that creates a Head application object using the super constructor InputtedApplication
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
    public Head(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    /**
     * This method reads and returns the first n lines of a file as specified by the user.
     *
     * @param inputStream
     *         Stream being read from
     * @param lines
     *         The number of lines from the head of the file to extract.
     *
     * @return A list where each element is a line of the file.
     *
     * @throws ApplicationException
     *         This error is thrown if the file specified by the user cannot be opened or read from.
     */
    @Override protected ArrayList<String> getResultsToWrite(InputStream inputStream, int lines)
            throws ApplicationException {
        ArrayList<String> linesToWrite = new ArrayList<>();
        for (int i = 0; i < lines; ++i) {
            String line = readLineFromInput(inputStream);
            if (line == null) {
                break;
            }
            linesToWrite.add(line);
        }
        return linesToWrite;
    }
}