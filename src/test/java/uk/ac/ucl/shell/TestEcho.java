package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestEcho extends Test {
    @org.junit.Test public void ValidEcho() throws IOException {
        setupTestingEnvironment();
        String command = "echo AAA";
        String[] expectedResult = new String[]{"AAA"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidEmptyArgumentEcho() {
        String command = "echo";
        String[] expectedResult = new String[]{};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}