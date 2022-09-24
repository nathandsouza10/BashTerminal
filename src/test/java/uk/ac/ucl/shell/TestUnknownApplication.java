package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestUnknownApplication extends Test {
    @org.junit.Test public void UnknownApplicationTest() throws IOException {
        setupTestingEnvironment();
        String command = "hello hello";
        String[] expectedResult = new String[]{"COMP0010 shell: hello: unknown application"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}