package uk.ac.ucl.shell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestLs extends Test {
    @org.junit.Test public void ValidLsNoArgumentTest() throws IOException {
        setupTestingEnvironment();
        String command = "ls";
        String[] expectedResult = getItemsInRoot();
        String commandResult = getCommandOutput(command);
        assertTrue(commandResult.matches("[^" + System.lineSeparator() + "]*" + System.lineSeparator()));
        String[] formattedCommandResult = commandResult.strip().split("\t");
        Arrays.sort(expectedResult);
        Arrays.sort(formattedCommandResult);
        assertEquals(Arrays.toString(expectedResult), Arrays.toString(formattedCommandResult));
    }

    @org.junit.Test public void ValidLsDirectoryTest() throws IOException {
        setupTestingEnvironment();
        String command = "ls " + folder1Name;
        String[] expectedResult = new String[]{folder2Name};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void SubFolderLsTest() throws IOException {
        setupTestingEnvironment();
        String command = "ls " + folder1Name + File.separator + folder2Name;
        String[] expectedResult = new String[]{folder3Name};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void emptyDirectoryLsTest() throws IOException {
        setupTestingEnvironment();
        String command = "ls " + folder1Name + File.separator + folder2Name + File.separator + folder3Name;
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistentDirectory() throws IOException {
        setupTestingEnvironment();
        String command = "ls " + nonExistentDirectoryName;
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void TooManyArgumentsLsTest() throws IOException {
        setupTestingEnvironment();
        String command = "ls arg1 arg2";
        String[] expectedResult = new String[]{"COMP0010 shell: ls: too many arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void HiddenFileTest() throws IOException {
        setupTestingEnvironment();
        File newFile = temporaryFolder.newFile(".test.txt");
        generateFile(new String[]{"aaa"}, newFile);
        String command = "ls";
        ArrayList<String> expectedResult = new ArrayList<>(List.of(getItemsInRoot()));
        String commandResult = getCommandOutput(command);
        assertTrue(commandResult.matches("[^" + System.lineSeparator() + "]*" + System.lineSeparator()));
        String[] formattedCommandResult = commandResult.strip().split("\t");
        Collections.sort(expectedResult);
        assertTrue(expectedResult.contains(".test.txt"));
        expectedResult.remove(".test.txt");
        Arrays.sort(formattedCommandResult);
        assertEquals(expectedResult.toString(), Arrays.toString(formattedCommandResult));
    }
}