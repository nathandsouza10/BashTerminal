package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestCut extends Test {
    @org.junit.Test public void SingleCharValidCutTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 1 " + file4Name;
        String[] expectedResult = new String[]{"A", "D", "G"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void InvalidDirectoryProvidedAsFile() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 1 " + folder1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cut: " + folder1Name + ": No such file"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void SingleRangeValidCutTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 1-2 " + file4Name;
        String[] expectedResult = new String[]{"AB", "DE", "GH"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void StartRangeValidCutTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b -2 " + file4Name;
        String[] expectedResult = new String[]{"AB", "DE", "GH"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void EndRangeValidCutTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 2- " + file4Name;
        String[] expectedResult = new String[]{"BC", "EF", "HI"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void CharAndRangeValidCutTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 1,2-3 " + file4Name;
        String[] expectedResult = new String[]{"ABC", "DEF", "GHI"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void OutOfRangeTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b " + Integer.MAX_VALUE + " " + file4Name;
        String[] expectedResult = new String[]{"", "", ""};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void BeforeStartRangeTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b -2,1 " + file4Name;
        String[] expectedResult = new String[]{"AB", "DE", "GH"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void AfterEndRangeTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 2-,3 " + file4Name;
        String[] expectedResult = new String[]{"BC", "EF", "HI"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void EmptyArgumentsTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut";
        String[] expectedResult = new String[]{"COMP0010 shell: cut: Wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void IncorrectSecondParameterTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -a 1 " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cut: You must specify a list of bytes"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NonExistentFileTest() throws IOException {
        setupTestingEnvironment();
        assertFalse(nonExistentFileExists());
        String command = "cut -b 1 " + nonExistentFileName;
        String[] expectedResult = new String[]{"COMP0010 shell: cut: " + nonExistentFileName + ": No such file"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void BadlySpecifiedByteTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b a " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cut: Bytes to extract specified incorrectly"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void BadlySpecifiedRangeTest() throws IOException {
        setupTestingEnvironment();
        String command = "cut -b 2-1 " + file1Name;
        String[] expectedResult = new String[]{"COMP0010 shell: cut: Invalid decreasing range"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}