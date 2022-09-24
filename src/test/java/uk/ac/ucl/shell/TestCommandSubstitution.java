package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestCommandSubstitution extends Test {
    @org.junit.Test public void ValidCommandSubstitution() throws IOException {
        setupTestingEnvironment();
        String command = "echo `echo test`";
        String[] expectedResult = new String[]{"test"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}