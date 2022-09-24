package uk.ac.ucl.shell.commands.base;

import uk.ac.ucl.shell.exceptions.CommandException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Performs globbing on input string within the current directory
 */
class Globbing {
    /**
     * Takes a regex and returns a list of files or directories matching regex as needed
     *
     * @param input
     *         Input regex to perform globbing with
     * @param currentDirectory
     *         Current working directory of shell
     *
     * @return list of directories found by globbing (or original string only if no matches)
     *
     * @throws CommandException
     *         error in processing of command
     */
    public static ArrayList<String> globbing(String input, String currentDirectory) throws CommandException {
        //when the string input contains a directory the format of the output will change
        String containingDirectory;
        if (input.contains(File.separator)) {
            String[] formatGlobbingResult = formatGlobbingWithDirectories(input);
            input = formatGlobbingResult[0];
            containingDirectory = formatGlobbingResult[1] + File.separator;
        } else {
            containingDirectory = "";
        }
        return globbingResult(input, containingDirectory, currentDirectory);
    }

    /**
     * Returns the filename and path to subdirectory for globbing where subdirectory is specified
     *
     * @param input
     *         string to glob
     *
     * @return list of directories found by globbing (or original string only if no matches)
     */
    private static String[] formatGlobbingWithDirectories(String input) {
        int separatorIndex = input.lastIndexOf(File.separator);
        String containingDirectory = input.substring(0, separatorIndex);
        return new String[]{input.substring(separatorIndex + 1), containingDirectory};
    }

    /**
     * Takes a regex and returns a list of files or directories matching regex as needed
     *
     * @param input
     *         Input regex to perform globbing with
     * @param currentDirectory
     *         Current working directory of shell
     * @param directory
     *         subdirectory globbing is being performed in
     *
     * @return list of directories found by globbing (or original string only if no matches)
     *
     * @throws CommandException
     *         error in processing of command
     */
    private static ArrayList<String> globbingResult(String input, String directory, String currentDirectory)
            throws CommandException {
        String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
        ArrayList<String> tokens = new ArrayList<>();
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(input);
        String nonQuote;
        while (regexMatcher.find()) {
            nonQuote = regexMatcher.group().trim();
            ArrayList<String> globbingResult = new ArrayList<>();
            try {
                try {
                    Path dir = Paths.get(currentDirectory + File.separator + directory);
                    DirectoryStream<Path> stream = Files.newDirectoryStream(dir, nonQuote);
                    for (Path entry : stream) {
                        globbingResult.add(directory + entry.getFileName().toString());
                    }
                    if (globbingResult.isEmpty()) {
                        globbingResult.add(directory + input);
                    }
                    tokens.addAll(globbingResult);
                } catch (IOException e) {
                    globbingResult.add(directory + input);
                    tokens.addAll(globbingResult);
                }
            } catch (PatternSyntaxException e) {
                throw new CommandException("invalid regex was entered");
            }
        }
        return tokens;
    }
}
