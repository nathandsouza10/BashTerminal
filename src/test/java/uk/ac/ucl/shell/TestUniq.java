package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestUniq extends Test {
    @org.junit.Test public void ValidUniq() throws IOException {
        setupTestingEnvironment();
        String command = "uniq " + uniqFileName;
        String[] expectedResult = new String[]{"AAA", "aaa", "BBB"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidUniqIgnoreCase() throws IOException {
        setupTestingEnvironment();
        String command = "uniq -i " + uniqFileName;
        String[] expectedResult = new String[]{"AAA", "BBB"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "uniq -i " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: uniq: " + nonExistentFileName + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongNumberOfArgumentsTest() throws IOException {
        setupTestingEnvironment();
        String command = "uniq -i " + file1Name + " argument";
        String[] expectedResult = new String[]{"COMP0010 shell: uniq: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongTagTest() throws IOException {
        setupTestingEnvironment();
        String command = "uniq -a " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: uniq: wrong argument -a"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArguments() {
        String command = "uniq";
        String[] expectedResult = new String[]{"COMP0010 shell: uniq: missing arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}