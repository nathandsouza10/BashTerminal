package uk.ac.ucl.shell;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestHead extends Test {
    @org.junit.Test public void ValidHeadTest() throws IOException {
        setupTestingEnvironment();
        String command = "head " + file1Name;
        String[] expectedResult = new String[]{file1Content[0], file1Content[1], file1Content[2], file1Content[3],
                                               file1Content[4], file1Content[5], file1Content[6], file1Content[7],
                                               file1Content[8], file1Content[9]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidHeadFewerLinesAvailableTest() throws IOException {
        setupTestingEnvironment();
        String command = "head -n 9999 " + file2Name;
        assertEquals(3, file2Content.length);
        String[] expectedResult = new String[]{file2Content[0], file2Content[1], file2Content[2]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidLineNumberHeadTest() throws IOException {
        setupTestingEnvironment();
        String command = "head -n 2 " + file1Name;
        String[] expectedResult = new String[]{file1Content[0], file1Content[1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidLineNumberInputRedirectedHeadTest() throws IOException {
        setupTestingEnvironment();
        String command = "head -n 2 < " + file1Name;
        String[] expectedResult = new String[]{file1Content[0], file1Content[1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void MissingArgumentTest() {
        String command = "head";
        String[] expectedResult = new String[]{"COMP0010 shell: head: missing arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongNumberOfArguments() throws IOException {
        setupTestingEnvironment();
        String command = "head -n 10 " + file1Name + " anotherArgument";
        String[] expectedResult = new String[]{"COMP0010 shell: head: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void IncorrectTagTest() throws IOException {
        setupTestingEnvironment();
        String command = "head -a 10 " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: head: wrong argument -a"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void IncorrectTagInputRedirectedTest() throws IOException {
        setupTestingEnvironment();
        String command = "head -a 10 < " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: head: wrong argument -a"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistentFile() throws IOException {
        setupTestingEnvironment();
        String command = "head -n 10 " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: head: " + nonExistentFileName + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidHeadWithDirectory() throws IOException {
        setupTestingEnvironment();
        String badPath = folder1Name + File.separator + file1Name;
        String command = "head -n 10 " + badPath;
        String[] expectedResult = new String[]{"COMP0010 shell: head: " + badPath + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonIntegerLineNumber() throws IOException {
        setupTestingEnvironment();
        String command = "head -n a " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: head: wrong argument a"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}