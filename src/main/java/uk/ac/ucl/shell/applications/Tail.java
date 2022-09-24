package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Prints the last N lines of a given file or stdin. If there are less than N lines, prints only the existing lines
 * without raising an exception. OPTIONS, e.g. -n 15 means printing the last 15 lines. If not specified, prints the last
 * 10 lines. FILE is the name of the file. If not specified, uses stdin.
 */
public class Tail extends AbstractHeadTail {
    /**
     * Constructor that creates a Tail application object using the super constructor InputtedApplication
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
    public Tail(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    /**
     * This method reads and returns the last n lines of a file as specified by the user.
     *
     * @param inputStream
     *         Stream being read from
     * @param lines
     *         The number of lines from the tail of the file to extract.
     *
     * @return A list where each element is a line of the file.
     *
     * @throws ApplicationException
     *         This error is thrown if the file specified by the user cannot be opened or read from.
     */
    @Override protected ArrayList<String> getResultsToWrite(InputStream inputStream, int lines)
            throws ApplicationException {
        ArrayList<String> storage = new ArrayList<>();
        ArrayList<String> linesToWrite = new ArrayList<>();
        String line;
        int index;
        while ((line = readLineFromInput(inputStream)) != null) {
            storage.add(line);
        }
        if (lines > storage.size()) {
            index = 0;
        } else {
            index = storage.size() - lines;
        }
        for (int i = index; i < storage.size(); ++i) {
            linesToWrite.add(storage.get(i));
        }
        return linesToWrite;
    }
}