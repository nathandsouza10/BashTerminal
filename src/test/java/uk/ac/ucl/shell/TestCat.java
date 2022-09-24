package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCat extends Test {
    @org.junit.Test public void ValidCatTest() throws IOException {
        setupTestingEnvironment();
        String command = "cat " + file1Name;
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArgumentsTest() {
        String command = "cat";
        String[] expectedResult = new String[]{"COMP0010 shell: cat: missing arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "cat " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: cat: " + nonExistentFileName + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}