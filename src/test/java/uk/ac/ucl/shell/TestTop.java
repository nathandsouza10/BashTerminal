package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestTop extends Test {
    @org.junit.Test public void ExitInSequence() throws IOException {
        setupTestingEnvironment();
        String command = "";
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}