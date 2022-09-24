package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.applications.base.AbstractApplication;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Lists the content of a directory. It prints a list of files and directories separated by tabs and followed by a
 * newline. Ignores files and directories whose names start with . . PATH is the directory. If not specified, list the
 * current directory.
 */
public class Ls extends AbstractApplication {
    /**
     * Constructor that creates a Ls application object using the super constructor InputtedApplication
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
    public Ls(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
              String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        File currDir;
        if (appArgs.isEmpty()) {
            currDir = new File(currentDirectory);
        } else if (appArgs.size() == 1) {
            currDir = new File(currentDirectory + File.separator + appArgs.get(0));
        } else {
            throw new ApplicationException("too many arguments", this);
        }
        writeResults(currDir);
    }

    /**
     * This method will loop through all files and directories in the current directory as specified by the user and
     * write to the terminal those that don't start with a '.'.
     *
     * @param currDir
     *         The current directory to search for folders and directories in.
     *
     * @throws ApplicationException
     *         This error is thrown if the current directory specified is not valid or accessible.
     */
    private void writeResults(File currDir) throws ApplicationException {
        if (invalidDir(currDir)) {
            throw new ApplicationException();
        }
        File[] listOfFiles = currDir.listFiles();
        boolean atLeastOnePrinted = false;
        if (listOfFiles == null || listOfFiles.length == 0) {
            throw new ApplicationException();
        } else {
            StringBuilder FilesAndFolders = new StringBuilder(listOfFiles.length);
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    FilesAndFolders.append(file.getName()).append("\t");
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                write(FilesAndFolders.toString().strip());
            }
        }
    }
}