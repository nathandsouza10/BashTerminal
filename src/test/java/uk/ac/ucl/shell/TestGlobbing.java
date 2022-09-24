package uk.ac.ucl.shell;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestGlobbing extends Test {
    @org.junit.Test public void ValidGlobbingTest() throws IOException {
        setupTestingEnvironment();
        String command = "echo *1";
        String[] expectedResult = new String[]{"folder1"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidGlobbingTest2() throws IOException {
        setupTestingEnvironment();
        String command = "echo ???der1";
        String[] expectedResult = new String[]{"folder1"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void GlobbingWithCommandSubstitution() throws IOException {
        setupTestingEnvironment();
        String command = "echo *`echo 1`";
        String[] expectedResult = new String[]{"*1"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidRegexTest() throws IOException {
        setupTestingEnvironment();
        String command = "echo folder2" + File.separator + "{4.txt";
        String[] expectedResult = new String[]{"COMP0010 shell: invalid regex was entered"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}