package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestGrep extends Test {
    @org.junit.Test public void ValidGrepTest() throws IOException {
        setupTestingEnvironment();
        String command = "grep A " + grepFile1Name;
        String[] expectedResult = new String[]{"AAA"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void MoreThanOneFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "grep A " + grepFile1Name + " " + grepFile2Name;
        String[] expectedResult = new String[]{grepFile1Name + ":AAA", grepFile2Name + ":AAA"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "grep -i " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: grep: " + nonExistentFileName + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongNumberOfArguments() {
        String command = "grep";
        String[] expectedResult = new String[]{"COMP0010 shell: grep: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}