package uk.ac.ucl.shell.applications.base;

import uk.ac.ucl.shell.applications.*;
import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Factory to produce Application child class instances
 *
 * @see ApplicationInterface
 * @see UnsafeApplicationDecorator
 */
public class ApplicationFactory {
    /**
     * Returns application corresponding to provided name
     *
     * @param appName
     *         Name of the Application
     * @param output
     *         Application output stream
     * @param input
     *         Application input stream
     * @param appArgs
     *         Arguments provided to application
     * @param currentDirectory
     *         Directory application executed in
     *
     * @return Returns an application
     *
     * @throws ApplicationException
     *         Thrown if application constructor fails
     */
    public ApplicationInterface buildCommand(String appName, InputStream input, OutputStream output,
                                             ArrayList<String> appArgs, String currentDirectory)
            throws ApplicationException {
        boolean unsafe = appName.charAt(0) == '_';
        String trimmedAppName;
        if (unsafe) {
            trimmedAppName = appName.substring(1);
        } else {
            trimmedAppName = appName;
        }
        AbstractApplication command;
        switch (trimmedAppName) {
            case "cd":
                command = new Cd(appName, input, output, appArgs, currentDirectory);
                break;
            case "pwd":
                command = new Pwd(appName, input, output, appArgs, currentDirectory);
                break;
            case "ls":
                command = new Ls(appName, input, output, appArgs, currentDirectory);
                break;
            case "cat":
                command = new Cat(appName, input, output, appArgs, currentDirectory);
                break;
            case "echo":
                command = new Echo(appName, input, output, appArgs, currentDirectory);
                break;
            case "head":
                command = new Head(appName, input, output, appArgs, currentDirectory);
                break;
            case "tail":
                command = new Tail(appName, input, output, appArgs, currentDirectory);
                break;
            case "grep":
                command = new Grep(appName, input, output, appArgs, currentDirectory);
                break;
            case "cut":
                command = new Cut(appName, input, output, appArgs, currentDirectory);
                break;
            case "sort":
                command = new Sort(appName, input, output, appArgs, currentDirectory);
                break;
            case "uniq":
                command = new Uniq(appName, input, output, appArgs, currentDirectory);
                break;
            case "find":
                command = new Find(appName, input, output, appArgs, currentDirectory);
                break;
            default:
                throw new ApplicationException("unknown application", appName);
        }
        if (unsafe) {
            return new UnsafeApplicationDecorator(command);
        } else {
            return command;
        }
    }
}