package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestQuoted extends Test {
    @org.junit.Test public void ValidDoubleQuoteTest() {
        String command = "echo \"hello world\"";
        String[] expectedResult = new String[]{"hello world"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void CommandSubstitutionInDoubleQuotes() throws IOException {
        setupTestingEnvironment();
        String command = "echo \"`echo hello world`\"";
        String[] expectedResult = new String[]{"hello world"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }

    @org.junit.Test public void ValidSingleQuoteTest() {
        String command = "echo 'hello world'";
        String[] expectedResult = new String[]{"hello world"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}
