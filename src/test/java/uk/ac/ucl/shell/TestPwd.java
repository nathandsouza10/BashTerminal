package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestPwd extends Test {
    @org.junit.Test public void ValidPwdTest() throws IOException {
        setupTestingEnvironment();
        String command = "pwd";
        String[] expectedResult = new String[]{rootPath()};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}
