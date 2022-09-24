package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestFind extends Test {
    @org.junit.Test public void ValidFindTest() throws IOException {
        setupTestingEnvironment();
        String[] splitFileName = file1Name.split("\\.");
        String fileName = splitFileName[0];
        String fileExtension = splitFileName[1];
        String command = "find -name " +
                         fileName.charAt(0) +
                         "*" +
                         fileName.charAt(fileName.length() - 1) +
                         "." +
                         fileExtension;
        String[] expectedResult = new String[]{findPathToFile1()};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArgumentsTest() {
        String command = "find";
        String[] expectedResult = new String[]{"COMP0010 shell: find: missing arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidArgumentsTest() throws IOException {
        setupTestingEnvironment();
        String command = "find fail1 fail2";
        String[] expectedResult = new String[]{"COMP0010 shell: find: -name missing"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidArgumentsTest2() throws IOException {
        setupTestingEnvironment();
        String command = "find fail1 fail2 fail3";
        String[] expectedResult = new String[]{"COMP0010 shell: find: -name missing"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void TooManyArgumentsTest() throws IOException {
        setupTestingEnvironment();
        String command = "find " + folder1Name + " -name fail1 fail2";
        String[] expectedResult = new String[]{"COMP0010 shell: find: many arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "find -name " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: find: cannot find any files with matching pattern"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest2() throws IOException {
        setupTestingEnvironment();
        String command = "find " + folder1Name + " -name " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: find: cannot find any files with matching pattern"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest3() throws IOException {
        setupTestingEnvironment();
        String command = "find " + nonExistentDirectoryName + " -name " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: find: cannot find any files with matching pattern"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}