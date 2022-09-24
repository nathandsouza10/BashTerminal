package uk.ac.ucl.shell;

import uk.ac.ucl.shell.exceptions.ApplicationException;
import uk.ac.ucl.shell.exceptions.CommandException;
import uk.ac.ucl.shell.exceptions.ExitException;
import uk.ac.ucl.shell.exceptions.OutputWriteException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestExit extends Test {
    @org.junit.Test public void ValidExitTest() {
        try {
            Shell.eval("exit", byteArrayOutputStream);
        } catch (ExitException e) {
            assertTrue(true);
        } catch (ApplicationException | CommandException | OutputWriteException e) {
            fail();
        }
    }
}