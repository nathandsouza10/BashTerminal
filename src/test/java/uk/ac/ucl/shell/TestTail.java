package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestTail extends Test {
    @org.junit.Test public void ValidTailTest() throws IOException {
        setupTestingEnvironment();
        String command = "tail " + file1Name;
        int lineCount = file1Content.length;
        String[] expectedResult = new String[]{file1Content[lineCount - 10], file1Content[lineCount - 9],
                                               file1Content[lineCount - 8], file1Content[lineCount - 7],
                                               file1Content[lineCount - 6], file1Content[lineCount - 5],
                                               file1Content[lineCount - 4], file1Content[lineCount - 3],
                                               file1Content[lineCount - 2], file1Content[lineCount - 1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidLineNumberTailTest() throws IOException {
        setupTestingEnvironment();
        String command = "tail -n 2 " + file1Name;
        int lineCount = file1Content.length;
        String[] expectedResult = new String[]{file1Content[lineCount - 2], file1Content[lineCount - 1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void NotEnoughLinesTest() throws IOException {
        setupTestingEnvironment();
        assertEquals(3, file2Content.length);
        String command = "tail -n 999999 " + file2Name;
        int lineCount = file2Content.length;
        String[] expectedResult = new String[]{file2Content[lineCount - 3], file2Content[lineCount - 2],
                                               file2Content[lineCount - 1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidGreaterLineNumber() throws Exception {
        setupTestingEnvironment();
        String command = "tail -n 11 " + file1Name;
        int lineCount = file1Content.length;
        String[] expectedResult = new String[]{file1Content[lineCount - 11], file1Content[lineCount - 10],
                                               file1Content[lineCount - 9], file1Content[lineCount - 8],
                                               file1Content[lineCount - 7], file1Content[lineCount - 6],
                                               file1Content[lineCount - 5], file1Content[lineCount - 4],
                                               file1Content[lineCount - 3], file1Content[lineCount - 2],
                                               file1Content[lineCount - 1]};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}