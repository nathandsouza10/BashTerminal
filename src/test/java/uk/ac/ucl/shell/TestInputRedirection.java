package uk.ac.ucl.shell;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestInputRedirection extends Test {
    @org.junit.Test public void ValidInputRedirectionTest() throws IOException {
        setupTestingEnvironment();
        String command = "cat < " + file1Name;
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestCd() throws IOException {
        setupTestingEnvironment();
        String command = "cd < " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cd: missing argument"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestCut() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 1 < " + file4Name;
        String[] expectedResult = new String[]{"A", "D", "G"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestEcho() throws IOException {
        setupTestingEnvironment();
        String command = "echo AAA < " + file1Name;
        String[] expectedResult = new String[]{"AAA"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestFind() throws IOException {
        setupTestingEnvironment();
        String[] splitFileName = file1Name.split("\\.");
        String fileName = splitFileName[0];
        String fileExtension = splitFileName[1];
        String command = "find -name " +
                         fileName.charAt(0) +
                         "*" +
                         fileName.charAt(fileName.length() - 1) +
                         "." +
                         fileExtension +
                         " < " +
                         file1Name;
        String[] expectedResult = new String[]{findPathToFile1()};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestGrep() throws IOException {
        setupTestingEnvironment();
        String command = "grep A < " + grepFile1Name;
        String[] expectedResult = new String[]{"AAA"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestHead() throws IOException {
        setupTestingEnvironment();
        String command = "head < " + file1Name;
        String[] expectedResult = new String[]{file1Content[0], file1Content[1], file1Content[2], file1Content[3],
                                               file1Content[4], file1Content[5], file1Content[6], file1Content[7],
                                               file1Content[8], file1Content[9]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestTail() throws IOException {
        setupTestingEnvironment();
        String command = "tail < " + file1Name;
        int lineCount = file1Content.length;
        String[] expectedResult = new String[]{file1Content[lineCount - 10], file1Content[lineCount - 9],
                                               file1Content[lineCount - 8], file1Content[lineCount - 7],
                                               file1Content[lineCount - 6], file1Content[lineCount - 5],
                                               file1Content[lineCount - 4], file1Content[lineCount - 3],
                                               file1Content[lineCount - 2], file1Content[lineCount - 1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestLs() throws IOException {
        setupTestingEnvironment();
        String command = "ls < " + file1Name;
        String[] expectedResult = getItemsInRoot();
        String commandResult = getCommandOutput(command);
        assertTrue(commandResult.matches("[^" + System.lineSeparator() + "]*" + System.lineSeparator()));
        String[] formattedCommandResult = commandResult.strip().split("\t");
        Arrays.sort(expectedResult);
        Arrays.sort(formattedCommandResult);
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(formattedCommandResult));
    }

    @org.junit.Test public void ValidInputRedirectionTestPwd() throws IOException {
        setupTestingEnvironment();
        String command = "pwd < " + file1Name;
        String[] expectedResult = new String[]{rootPath()};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestSort() throws IOException {
        setupTestingEnvironment();
        String command = "sort < " + unsortedFileName;
        Arrays.sort(unsortedFileContent);
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectionTestUniq() throws IOException {
        setupTestingEnvironment();
        String command = "uniq < " + uniqFileName;
        String[] expectedResult = new String[]{"AAA", "aaa", "BBB"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void PrefixRedirectionTest() throws IOException {
        setupTestingEnvironment();
        String command = "< " + file1Name + " cat";
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void NoSpaceTest() throws IOException {
        setupTestingEnvironment();
        String command = "cat <" + file1Name;
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void NoCommandTest() throws IOException {
        setupTestingEnvironment();
        String command = "< " + file1Name;
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidNonExistentFileTest() {
        String command = "cat < " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: cannot find file"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidChainInputRedirectionTest() {
        String command = "cat < " + file1Name + " < " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cannot chain input redirections"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidNoArguments() {
        String command = "<";
        String[] expectedResult = new String[]{"COMP0010 shell: incorrect number/type of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}