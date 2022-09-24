package uk.ac.ucl.shell.applications.base;

import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Defines structure and common methods to enhance ApplicationInterface and redundant code in child Applications
 */
public abstract class AbstractApplication implements ApplicationInterface {
    /**
     * Arguments provided to application
     */
    protected final ArrayList<String> appArgs;
    /**
     * Directory application executed in
     */
    protected final String currentDirectory;
    /**
     * Application input stream
     */
    protected final InputStream input;
    /**
     * Application Name
     */
    private final String name;
    /**
     * Application output stream
     */
    private final OutputStream output;

    /**
     * Constructor that creates an application
     *
     * @param name
     *         Name of the Application
     * @param input
     *         Default input stream
     * @param output
     *         Default stream writer object
     * @param appArgs
     *         A List of all arguments required for the application
     * @param currentDirectory
     *         The current directory the shell is in
     */
    public AbstractApplication(String name, InputStream input, OutputStream output, ArrayList<String> appArgs,
                               String currentDirectory) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.appArgs = appArgs;
        this.currentDirectory = currentDirectory;
    }

    @Override public String run() throws ApplicationException {
        doOperations();
        return getUpdatedDirectory();
    }

    @Override public String getName() {
        return this.name;
    }

    /**
     * Runs application processing (to be overridden by child class)
     *
     * @throws ApplicationException
     *         Thrown if error occurs while running application
     */
    protected abstract void doOperations() throws ApplicationException;

    /**
     * Returns current directory (can be overridden by child class to change shell directory)
     *
     * @return Current working directory after executing application
     *
     * @throws ApplicationException
     *         Thrown if error occurs while running application
     */
    protected String getUpdatedDirectory() throws ApplicationException {
        return currentDirectory;
    }

    /**
     * Writes inputs to output stream on new lines
     *
     * @param stringsToWrite
     *         Array of strings to be written
     *
     * @throws ApplicationException
     *         Thrown if writing to output stream fails
     */
    protected void write(ArrayList<String> stringsToWrite) throws ApplicationException {
        for (String line : stringsToWrite) {
            write(line);
        }
    }

    /**
     * Writes input to output stream
     *
     * @param stringToWrite
     *         String to be written
     *
     * @throws ApplicationException
     *         Thrown if writing to output stream fails
     */
    protected void write(String stringToWrite) throws ApplicationException {
        try {
            for (int characterIndex = 0; characterIndex < stringToWrite.length(); ++characterIndex) {
                output.write(stringToWrite.charAt(characterIndex));
            }
            String lineSeparator = System.lineSeparator();
            for (int lineSeparatorCharacterIndex = 0;
                 lineSeparatorCharacterIndex < lineSeparator.length();
                 ++lineSeparatorCharacterIndex) {
                output.write(lineSeparator.charAt(lineSeparatorCharacterIndex));
            }
            output.flush();
        } catch (IOException e) {
            throw new ApplicationException("cannot write to terminal", this);
        }
    }

    /**
     * Writes from input stream to output stream
     *
     * @param inputStream
     *         Input stream to be read from
     *
     * @throws ApplicationException
     *         Thrown if writing to output stream fails
     */
    protected void write(InputStream inputStream) throws ApplicationException {
        try {
            inputStream.transferTo(output);
        } catch (IOException e) {
            throw new ApplicationException("cannot write to terminal", this);
        }
    }

    /**
     * Checks provided path points to an existing file
     *
     * @param filePath
     *         Path to file
     *
     * @return If file is invalid
     */
    protected boolean invalidFile(Path filePath) {
        return !Files.exists(filePath) ||
               Files.isDirectory(filePath) ||
               !Files.isReadable(filePath) ||
               !Files.isWritable(filePath);
    }

    /**
     * Checks provided directory exists and is a directory
     *
     * @param dir
     *         Path to directory
     *
     * @return if directory is invalid
     */
    protected boolean invalidDir(File dir) {
        return !dir.exists() || !dir.isDirectory();
    }

    /**
     * Extracts lines from an input stream
     *
     * @param inputStream
     *         Stream to be read from
     *
     * @return list of words from the file
     *
     * @throws ApplicationException
     *         File read error occurs
     */
    protected ArrayList<String> readLinesFromInput(InputStream inputStream) throws ApplicationException {
        ArrayList<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        int character;
        try {
            while ((character = inputStream.read()) != -1) {
                currentWord.append((char) character);
                String currentWordString = currentWord.toString();
                if (currentWordString.endsWith(System.lineSeparator())) {
                    words.add(currentWordString.substring(0, currentWordString.lastIndexOf(System.lineSeparator())));
                    currentWord = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new ApplicationException("cannot read input", this);
        }
        return words;
    }

    /**
     * Extracts line from an input stream
     *
     * @param inputStream
     *         Stream to be read from
     *
     * @return next line in file
     *
     * @throws ApplicationException
     *         File read error occurs
     */
    protected String readLineFromInput(InputStream inputStream) throws ApplicationException {
        StringBuilder currentWord = new StringBuilder();
        try {
            int firstChar = inputStream.read();
            if (firstChar == -1) {
                return null;
            } else {
                currentWord.append((char) firstChar);
            }
            int character;
            while ((character = inputStream.read()) != -1) {
                currentWord.append((char) character);
                String currentWordString = currentWord.toString();
                if (currentWordString.endsWith(System.lineSeparator())) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new ApplicationException("cannot read input", this);
        }
        return currentWord.toString().strip();
    }
}