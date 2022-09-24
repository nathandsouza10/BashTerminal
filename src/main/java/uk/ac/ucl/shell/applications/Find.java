package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Recursively searches for files with matching names. Outputs the list of relative paths, each followed by a newline.
 * find [PATH] -name PATTERN. PATTERN is a file name with some parts replaced with * (asterisk). PATH is the root
 * directory for search. If not specified, uses the current directory.
 */
public class Find extends AbstractApplication {
    /**
     * Holds whether any matching files have been found, true if not and false otherwise
     */
    private boolean isEmpty = true;

    /**
     * Constructor that creates a Find application object using the super constructor InputtedApplication
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
    public Find(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        File currDir;
        Pattern namePattern;
        if (appArgs.size() <= 1) {
            throw new ApplicationException("missing arguments", this);
        } else if (appArgs.size() == 2) {
            if (appArgs.get(0).compareTo("-name") != 0) {
                throw new ApplicationException("-name missing", this);
            }
            currDir = new File(currentDirectory);
            namePattern = Pattern.compile(appArgs.get(1).replaceAll("\\*", ".*"));
        } else if (appArgs.size() == 3) {
            if (appArgs.get(1).compareTo("-name") != 0) {
                throw new ApplicationException("-name missing", this);
            }
            currDir = new File(appArgs.get(0));
            namePattern = Pattern.compile(appArgs.get(2).replaceAll("\\*", ".*"));
        } else {
            throw new ApplicationException("many arguments", this);
        }
        writeResults(currDir, namePattern);
        if (isEmpty) {
            throw new ApplicationException("cannot find any files with matching pattern", this);
        }
    }

    /**
     * This is a recursive method that will find files and directories given a regex pattern to match.
     *
     * @param currDir
     *         The current directory the shell is in
     * @param namePattern
     *         The regex for file or directory name that is being searched
     *
     * @throws ApplicationException
     *         Thrown if results cannot be printed
     */
    private void writeResults(File currDir, Pattern namePattern) throws ApplicationException {
        File[] listOfFiles = currDir.listFiles();
        if (listOfFiles == null) {
            return;
        }
        StringBuilder stringBuilder = searchFile(listOfFiles, new StringBuilder(), namePattern);
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        if (!stringBuilder.toString().isEmpty()) {
            isEmpty = false;
            write(stringBuilder.toString());
        }
    }

    /**
     * Searches the directory for matching files
     *
     * @param listOfFiles
     *         List of files in the directory
     * @param stringBuilder
     *         The file paths of matching files are stored here
     * @param namePattern
     *         The pattern to find matching file names for
     *
     * @return File paths of matching files
     *
     * @throws ApplicationException
     *         Thrown if results cannot be printed
     */
    private StringBuilder searchFile(File[] listOfFiles, StringBuilder stringBuilder, Pattern namePattern)
            throws ApplicationException {
        boolean isMatchingFile;
        String temporary;
        for (File file : listOfFiles) {
            Matcher nameMatch = namePattern.matcher(file.getName());
            isMatchingFile = nameMatch.matches();
            if (file.isDirectory()) {
                writeResults(file, namePattern);
            } else if (isMatchingFile) {
                temporary = file.getPath().replaceFirst("^/[^/]*/", "./");
                stringBuilder.append(temporary.strip()).append("\n");
            }
        }
        return stringBuilder;
    }
}