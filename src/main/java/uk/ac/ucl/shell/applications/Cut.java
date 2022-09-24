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
import java.util.HashSet;
import java.util.Objects;

/**
 * Cuts out sections from each line of a given file or stdin and prints the result to stdout. OPTION specifies the bytes
 * to extract from each line: -b 1,2,3 extracts 1st, 2nd and 3rd bytes. -b 1-3,5-7 extracts the bytes from 1st to 3rd
 * and from 5th to 7th. -b -3,5- extracts the bytes from the beginning of line to 3rd, and from 5th to the end of line.
 * FILE is the name of the file. If not specified, uses stdin.
 */
public class Cut extends AbstractApplication {
    /**
     * Regex to match a valid number (number)
     */
    private final String standardRegex = "([1-9][0-9]*)";
    /**
     * Regular expression for a range of positive numbers (number-number)
     */
    private final String rangeRegex = "([1-9][0-9]*-[1-9][0-9]*)";
    /**
     * Regex to match a start range string (-number)
     */
    private final String startRangeRegex = "(-[1-9][0-9]*)";
    /**
     * Regex to match an end range string (number-)
     */
    private final String endRangeRegex = "([1-9][0-9]*-)";

    /**
     * Constructor that creates a Cut application object using the super constructor InputtedApplication
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
    public Cut(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
               String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory);
    }

    @Override protected void doOperations() throws ApplicationException {
        argumentValidation(appArgs);
        String[] bytesToRemove = appArgs.get(1).split(",");
        //Byte specification validation and value setting
        int startRange = 0;
        int endRange = Integer.MAX_VALUE;
        HashSet<Integer> byteSet = new HashSet<>();
        for (String byteToRemove : bytesToRemove) {
            if (!checkValidByte(byteToRemove)) {
                throw new ApplicationException("Bytes to extract specified incorrectly", this);
            }
            if (!checkValidRange(byteToRemove)) {
                throw new ApplicationException("Invalid decreasing range", this);
            }
            startRange = getNewStartRange(byteToRemove, startRange);
            endRange = getNewEndRange(byteToRemove, endRange);
            byteSet.addAll(extractByteSet(byteToRemove, startRange, endRange));
        }
        try {
            if (input == null) {
                FileInputStream fileInputStream = new FileInputStream(validateFile(appArgs, currentDirectory).toFile());
                write(getResultsToWrite(byteSet, startRange, endRange, fileInputStream));
            } else {
                write(getResultsToWrite(byteSet, startRange, endRange, input));
            }
        } catch (IOException e) {
            throw new ApplicationException("Cannot open file", this);
        }
    }

    /**
     * Validates the number and type of arguments passed by the user
     *
     * @param appArgs
     *         Arguments passed by user
     *
     * @throws ApplicationException
     *         Thrown if arguments provided is invalid
     */
    private void argumentValidation(ArrayList<String> appArgs) throws ApplicationException {
        if (input == null && appArgs.size() != 3 || input != null && appArgs.size() != 2) {
            throw new ApplicationException("Wrong number of arguments", this);
        } else if (!Objects.equals(appArgs.get(0), "-b")) {
            throw new ApplicationException("You must specify a list of bytes", this);
        }
    }

    /**
     * Check if =bytes to remove specified is valid
     *
     * @param byteToRemove
     *         The bytes to remove
     *
     * @return True if bytes to remove is valid, false otherwise
     */
    private boolean checkValidByte(String byteToRemove) {
        return byteToRemove.matches(
                String.join("|", new String[]{standardRegex, rangeRegex, startRangeRegex, endRangeRegex}));
    }

    /**
     * Check if range of bytes to remove specified is valid
     *
     * @param byteToRemove
     *         The range of bytes to remove
     *
     * @return True if range of bytes to remove is valid, false otherwise
     */
    private boolean checkValidRange(String byteToRemove) {
        if (byteToRemove.matches(rangeRegex)) {
            String[] range = byteToRemove.split("-");
            return Integer.parseInt(range[0]) <= Integer.parseInt(range[1]);
        }
        return true;
    }

    /**
     * Gets the new start range of bytes to remove
     *
     * @param byteToRemove
     *         The bytes to remove
     * @param currentStartRange
     *         The current start range of bytes to remove
     *
     * @return The new start range
     */
    private int getNewStartRange(String byteToRemove, int currentStartRange) {
        if (byteToRemove.matches(startRangeRegex)) {
            return Math.max(currentStartRange, Integer.parseInt(byteToRemove.substring(1)));
        }
        return currentStartRange;
    }

    /**
     * Gets the new end range of bytes to remove
     *
     * @param byteToRemove
     *         The bytes to remove
     * @param currentEndRange
     *         The current end range of bytes to remove
     *
     * @return The new end range
     */
    private int getNewEndRange(String byteToRemove, int currentEndRange) {
        if (byteToRemove.matches(endRangeRegex)) {
            return Math.min(currentEndRange, Integer.parseInt(byteToRemove.substring(0, byteToRemove.length() - 1)));
        }
        return currentEndRange;
    }

    /**
     * Gets the new set of bytes to remove
     *
     * @param byteToRemove
     *         The bytes to remove
     * @param startRange
     *         The current start range of bytes to remove
     * @param endRange
     *         The current end range of bytes to remove
     *
     * @return A hashset of the new bytes
     */
    private HashSet<Integer> extractByteSet(String byteToRemove, int startRange, int endRange) {
        HashSet<Integer> bytes = new HashSet<>();
        if (byteToRemove.matches(standardRegex)) {
            int byteValue = Integer.parseInt(byteToRemove);
            if (byteValue > startRange && byteValue < endRange) {
                bytes.add(byteValue);
            }
        } else if (byteToRemove.matches(rangeRegex)) {
            String[] range = byteToRemove.split("-");
            int startByteValue = Math.max(startRange + 1, Integer.parseInt(range[0]));
            int endByteValue = Math.min(endRange - 1, Integer.parseInt(range[1]));
            for (int i = startByteValue; i < endByteValue + 1; ++i) {
                bytes.add(i);
            }
        }
        return bytes;
    }

    /**
     * A method that validates the filename passed by the user and returns the path of that file in the system
     *
     * @param appArgs
     *         Arguments passed by user
     * @param currentDirectory
     *         The current directory the system is in
     *
     * @return The file path of the file passed as an argument by user
     *
     * @throws ApplicationException
     *         Thrown if file does not exist
     */
    private Path validateFile(ArrayList<String> appArgs, String currentDirectory) throws ApplicationException {
        String fileName = appArgs.get(2);
        Path filePath = Paths.get(currentDirectory).resolve(fileName);
        if (invalidFile(filePath)) {
            throw new ApplicationException("No such file", this, fileName);
        }
        return filePath;
    }

    /**
     * Extracts the result from byte set within start and end range
     *
     * @param byteSet
     *         The byte set to extract results from
     * @param startRange
     *         The start range
     * @param endRange
     *         The end range
     * @param inputStream
     *         Stream being read from
     *
     * @return A list of the results
     *
     * @throws ApplicationException
     *         Thrown if an error is faced with reading the file
     */
    private ArrayList<String> getResultsToWrite(HashSet<Integer> byteSet, int startRange, int endRange,
                                                InputStream inputStream) throws ApplicationException {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = readLineFromInput(inputStream)) != null) {
            //Math.min() used for dealing with startRange being greater than line.length()
            StringBuilder cutLine = new StringBuilder(line.substring(0, Math.min(line.length(), startRange)));
            for (Integer byteNumber : byteSet) {
                //Break used to avoid attempting to read char beyond line length
                if (byteNumber > line.length()) {
                    break;
                }
                cutLine.append(line.charAt(byteNumber - 1));
            }
            //Math.min() used for dealing with endRange being greater than line.length()
            cutLine.append(line, Math.min(line.length(), endRange - 1), line.length());
            lines.add(cutLine.toString());
        }
        return lines;
    }
}