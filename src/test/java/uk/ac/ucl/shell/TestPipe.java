package uk.ac.ucl.shell;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestPipe extends Test {
    @org.junit.Test public void ValidPipeTest() throws IOException {
        setupTestingEnvironment();
        String command = "sort " + unsortedFileName + " | uniq";
        Arrays.sort(unsortedFileContent);
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }

    @org.junit.Test public void ValidMultiPipeTest() throws IOException {
        setupTestingEnvironment();
        String command = "cat " + unsortedFileName + " | sort | uniq";
        Arrays.sort(unsortedFileContent);
        assertEquals(formatExpectedOutput(unsortedFileContent), getCommandOutput(command));
    }
}