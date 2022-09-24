package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Searches for lines containing a match to the specified pattern. The output of the command is the list of lines. Each
 * line is printed followed by a newline. PATTERN is a regular expression in PCRE format. FILE(s) is the name(s) of the
 * file(s). When multiple files are provided, the found lines should be prefixed with the corresponding file paths and
 * colon symbols. If no file is specified, uses stdin.
 */
public class Grep extends AbstractApplication {
    /**
     * Constructor that creates a grep application object
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
    public Grep(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        if (input == null && appArgs.size() < 2 || input != null && appArgs.size() < 1) {
            throw new ApplicationException("wrong number of arguments", this);
        }
        int numOfFiles = appArgs.size() - 1;
        InputStream[] inputStreams = getInputStreams(numOfFiles, currentDirectory, appArgs);
        writeToTerminal(inputStreams, appArgs, numOfFiles);
    }

    /**
     * This method returns an array of all the file paths for the file names provided by the user to search for a
     * pattern in. If a file is not found, non-existent or not readable then an error is thrown.
     *
     * @param numOfFiles
     *         The number of files the user has wished for the pattern to be searched in.
     * @param currentDirectory
     *         The current directory the system is in
     * @param appArgs
     *         A list of all the file names to be searched for, the first element is the PCRE pattern to be matched.
     *
     * @return A list of the file paths for all the files specified by the user
     *
     * @throws ApplicationException
     *         error in processing of application within command
     */
    private InputStream[] getInputStreams(int numOfFiles, String currentDirectory, ArrayList<String> appArgs)
            throws ApplicationException {
        InputStream[] inputStreams;
        if (input == null) {
            inputStreams = new FileInputStream[numOfFiles];
        } else {
            inputStreams = new FileInputStream[numOfFiles + 1];
            inputStreams[numOfFiles] = input;
        }
        Path currentDir = Paths.get(currentDirectory);
        Path filePath;
        for (int i = 0; i < numOfFiles; ++i) {
            filePath = currentDir.resolve(appArgs.get(i + 1));
            try {
                inputStreams[i] = new FileInputStream(filePath.toFile());
            } catch (IOException e) {
                throw new ApplicationException("does not exist", this, appArgs.get(i + 1));
            }
        }
        return inputStreams;
    }

    /**
     * This method loops through the file trying to match lines that have the same pattern. If the file cannot be opened
     * then an error message is thrown accordingly.
     *
     * @param inputStreams
     *         An array containing input streams for the files to match the PCRE regex in.
     * @param appArgs
     *         A list of all the filenames and the pattern that is to be matched.
     * @param numOfFiles
     *         The number of files the user has wished for the pattern to be searched in.
     *
     * @throws ApplicationException
     *         If a specified file cannot be opened then this error is thrown.
     */
    private void writeToTerminal(InputStream[] inputStreams, ArrayList<String> appArgs, int numOfFiles)
            throws ApplicationException {
        Pattern grepPattern = Pattern.compile(appArgs.get(0));
        for (int stream = 0; stream < inputStreams.length; ++stream) {
            String line;
            while ((line = readLineFromInput(inputStreams[stream])) != null) {
                Matcher matcher = grepPattern.matcher(line);
                if (matcher.find()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (numOfFiles > 1) {
                        stringBuilder.append(appArgs.get(stream + 1)).append(":");
                    }
                    write(stringBuilder.append(line).toString());
                }
            }
        }
    }
}