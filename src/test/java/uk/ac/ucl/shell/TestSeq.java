package uk.ac.ucl.shell;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestSeq extends Test {
    @org.junit.Test public void ValidSeqTest() throws IOException {
        setupTestingEnvironment();
        String command = "echo aaa ; echo bbb";
        String[] expectedResult = new String[]{"aaa", "bbb"};
        assertEquals(formatExpectedOutput(expectedResult), getCommandOutput(command));
    }
}