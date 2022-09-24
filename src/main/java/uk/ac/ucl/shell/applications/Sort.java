package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sorts the contents of a file/stdin line by line and prints the result to stdout. OPTIONS: -r sorts lines in reverse
 * order FILE is the name of the file. If not specified, uses stdin.
 */
public class Sort extends AbstractSortUniq {
    /**
     * Constructor that creates a sort application object
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
    public Sort(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory, 'r');
    }

    /**
     * Extracts words from a file a handles pre-processing required for sorting the words
     *
     * @param inputStream
     *         Stream being read from
     * @param specialCase
     *         True if sort is in reverse alphabetical order
     *
     * @throws ApplicationException
     *         Thrown if results cannot be printed to terminal
     */
    protected void handle(InputStream inputStream, boolean specialCase) throws ApplicationException {
        ArrayList<String> words = readLinesFromInput(inputStream);
        List<String> sortedWords = quick_sort(words);
        if (specialCase) {
            List<String> ReversedSortedWords = reverseArray(sortedWords);
            for (String uniqueItem : ReversedSortedWords) {
                write(uniqueItem);
            }
        } else {
            for (String uniqueItem : sortedWords) {
                write(uniqueItem);
            }
        }
    }

    /**
     * This method sorts out all elements in the list in alphabetical order.
     *
     * @param arr
     *         The list to be sorted.
     *
     * @return The list that has been sorted.
     */
    private List<String> quick_sort(ArrayList<String> arr) {
        ArrayList<String> less = new ArrayList<>();
        ArrayList<String> equal = new ArrayList<>();
        ArrayList<String> greater = new ArrayList<>();
        if (arr.size() > 1) {
            String pivot = arr.get(0);
            for (String s : arr) {
                if (s.compareTo(pivot) < 0) {
                    less.add(s);
                } else if (s.compareTo(pivot) == 0) {
                    equal.add(s);
                } else {
                    greater.add(s);
                }
            }
            return Stream.of(quick_sort(less), equal, quick_sort(greater))
                         .flatMap(Collection::stream)
                         .collect(Collectors.toList());
        } else {
            return arr;
        }
    }

    /**
     * This method returns a list in reverse order.
     *
     * @param arr
     *         The list to be reversed.
     *
     * @return The list that has been reversed.
     */
    private List<String> reverseArray(List<String> arr) {
        List<String> temp = new ArrayList<>();
        for (int i = arr.size() - 1; i >= 0; i--) {
            temp.add(arr.get(i));
        }
        return temp;
    }
}