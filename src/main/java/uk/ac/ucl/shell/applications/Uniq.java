package uk.ac.ucl.shell.applications;

import uk.ac.ucl.shell.exceptions.ApplicationException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Detects and deletes adjacent duplicate lines from an input file/stdin and prints the result to stdout. OPTIONS: -i
 * ignores case when doing comparison (case-insensitive) FILE is the name of the file. If not specified, uses stdin.
 */
public class Uniq extends AbstractSortUniq {
    /**
     * Constructor that creates an Uniq application object
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
    public Uniq(String setName, InputStream input, OutputStream output, ArrayList<String> appArgs,
                String currentDirectory) {
        super(setName, input, output, appArgs, currentDirectory, 'i');
    }

    /**
     * Extracts words from a file a handles pre-processing required for finding unique words
     *
     * @param inputStream
     *         Stream being read from
     * @param specialCase
     *         True if letter case does not need to be matched
     *
     * @throws ApplicationException
     *         Thrown if results cannot be printed to terminal
     */
    protected void handle(InputStream inputStream, boolean specialCase) throws ApplicationException {
        ArrayList<String> words = readLinesFromInput(inputStream);
        ArrayList<String> uniqueWords;
        if (!specialCase) {
            uniqueWords = getUniqueWords(words);
        } else {
            uniqueWords = getUniqueWordsIgnoreCase(words);
        }
        for (String uniqueItem : uniqueWords) {
            write(uniqueItem);
        }
    }

    /**
     * Extracts unique words from a list (case-sensitive)
     *
     * @param words
     *         List of words to search unique word for
     *
     * @return List of unique words
     */
    private ArrayList<String> getUniqueWords(ArrayList<String> words) {
        ArrayList<String> uniqueWords = new ArrayList<>();
        String currentWord = words.get(0);
        uniqueWords.add(currentWord);
        for (int i = 1; i < words.size(); i++) {
            if (!words.get(i).equals(currentWord)) {
                currentWord = words.get(i);
                uniqueWords.add(currentWord);
            }
        }
        return uniqueWords;
    }

    /**
     * Extracts unique words from a list (case-insensitive)
     *
     * @param words
     *         List of words to search unique word for
     *
     * @return List of unique words
     */
    private ArrayList<String> getUniqueWordsIgnoreCase(ArrayList<String> words) {
        ArrayList<String> uniqueWords = new ArrayList<>();
        String currentWord = words.get(0);
        uniqueWords.add(currentWord);
        for (String word : words) {
            if (!word.equalsIgnoreCase(currentWord)) {
                currentWord = word;
                uniqueWords.add(currentWord);
            }
        }
        return uniqueWords;
    }
}