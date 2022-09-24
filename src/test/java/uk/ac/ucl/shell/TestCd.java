package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCd extends Test {
    @org.junit.Test public void ValidCdTest() throws IOException {
        setupTestingEnvironment();
        String command = "cd " + folder1Name;
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
        assertEquals(pathToItem(folder1Name), Shell.getCurrentDirectory());
    }

    @org.junit.Test public void MoreThanOneArgumentTest() throws IOException {
        setupTestingEnvironment();
        String command = "cd " + folder1Name + " anotherArgument";
        String[] expectedResult = new String[]{"COMP0010 shell: cd: too many arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArgumentsTest() {
        String command = "cd";
        String[] expectedResult = new String[]{"COMP0010 shell: cd: missing argument"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidDirectoryTest() throws IOException {
        setupTestingEnvironment();
        String command = "cd " + nonExistentDirectoryName;
        String[] expectedResult = new String[]{
                "COMP0010 shell: cd: " + nonExistentDirectoryName + " is not an existing directory"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidPathIsFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "cd " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cd: " + file1Name + " is not an existing directory"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}