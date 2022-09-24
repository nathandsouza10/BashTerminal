package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestUnsafeApplications extends Test {
    @org.junit.Test public void ValidUnsafeTest() throws IOException {
        setupTestingEnvironment();
        String command = "_cat " + file1Name;
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void ValidInputtedInputRedirectionTest() throws IOException {
        setupTestingEnvironment();
        String command = "_cat < " + file1Name;
        assertEquals(formatExpectedOutput(file1Content), getCommandOutput(command));
    }

    @org.junit.Test public void ValidUnsafeThrowTest() throws IOException {
        setupTestingEnvironment();
        String command = "_cat " + nonExistentFileName + "; echo BBB";
        String[] expectedResult = new String[]{"COMP0010 shell: _cat: " + nonExistentFileName + ": does not exist",
                                               "BBB"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidUnsafeLsThrowTest() throws IOException {
        setupTestingEnvironment();
        String command = "_ls " + nonExistentDirectoryName + "; echo BBB";
        String[] expectedResult = new String[]{"BBB"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}