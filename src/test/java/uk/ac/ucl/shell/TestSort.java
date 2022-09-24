package uk.ac.ucl.shell;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class TestSort extends Test {
    @org.junit.Test public void ValidSortTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort " + unsortedFileName;
        Arrays.sort(unsortedFileContent);
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void ValidSortTest2() throws IOException {
        setupTestingEnvironment();
        String command = "sort " + unsortedFileName2;
        Arrays.sort(unsortedFileContent);
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void ValidReverseSortTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -r " + unsortedFileName;
        Arrays.sort(unsortedFileContent, Comparator.reverseOrder());
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputRedirectedReverseSortTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -r < " + unsortedFileName;
        Arrays.sort(unsortedFileContent, Comparator.reverseOrder());
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArguments() {
        String command = "sort";
        String[] expectedResult = new String[]{"COMP0010 shell: sort: missing arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongNumberOfArgumentsTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -r " + unsortedFileName + " more";
        String[] expectedResult = new String[]{"COMP0010 shell: sort: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongNumberOfArgumentsInputRedirectedTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -r more < " + unsortedFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: sort: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistingFileTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -r " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: sort: " + nonExistentFileName + ": does not exist"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void WrongTagTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort -a " + unsortedFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: sort: wrong argument -a"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}