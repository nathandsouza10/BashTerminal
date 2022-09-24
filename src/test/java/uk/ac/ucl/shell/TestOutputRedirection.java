package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestOutputRedirection extends Test {
    @org.junit.Test public void ValidExistingFileOutputRedirection() throws IOException {
        setupTestingEnvironment();
        assertNotEquals("aaa", fileContents(file1Name));
        runCommand("echo aaa > " + file1Name);
        assertEquals("aaa", fileContents(file1Name));
    }

    @org.junit.Test public void ValidNonExistingFileOutputRedirection() throws IOException {
        setupTestingEnvironment();
        assertFalse(nonExistentFileExists());
        runCommand("echo aaa > " + nonExistentFileName);
        assertEquals("aaa", fileContents(nonExistentFileName));
    }

    @org.junit.Test public void PrefixRedirectionTest() throws IOException {
        setupTestingEnvironment();
        assertFalse(nonExistentFileExists());
        runCommand("> " + nonExistentFileName + " echo aaa");
        assertEquals("aaa", fileContents(nonExistentFileName));
    }

    @org.junit.Test public void NoSpaceTest() throws IOException {
        setupTestingEnvironment();
        assertFalse(nonExistentFileExists());
        runCommand("echo aaa >" + nonExistentFileName);
        assertEquals("aaa", fileContents(nonExistentFileName));
    }

    @org.junit.Test public void NoCommandTest() throws IOException {
        setupTestingEnvironment();
        assertFalse(nonExistentFileExists());
        runCommand("> " + nonExistentFileName);
        assertTrue(nonExistentFileExists());
        assertEquals("", fileContents(nonExistentFileName));
    }

    @org.junit.Test public void InvalidChainInputRedirectionTest() {
        String command = "echo aaa > " + file1Name + " > " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cannot chain output redirections"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidNoArguments() {
        String command = ">";
        String[] expectedResult = new String[]{"COMP0010 shell: incorrect number/type of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}