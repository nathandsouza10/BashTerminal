package uk.ac.ucl.shell;

import static org.junit.Assert.assertEquals;

public class TestShell extends Test {
    @org.junit.Test public void IncorrectArgType() {
        Shell.main(new String[]{"-b", "echo hello"});
        String result = byteArrayOutputStream.toString();
        byteArrayOutputStream.reset();
        String[] expectedResult = new String[]{"COMP0010 shell: -b: unexpected argument"};
        assertEquals(formatExpectedOutput(expectedResult), result);
    }

    @org.junit.Test public void IncorrectArgCount() {
        Shell.main(new String[]{"-c"});
        String result = byteArrayOutputStream.toString();
        byteArrayOutputStream.reset();
        String[] expectedResult = new String[]{"COMP0010 shell: wrong number of arguments"};
        assertEquals(formatExpectedOutput(expectedResult), result);
    }
}