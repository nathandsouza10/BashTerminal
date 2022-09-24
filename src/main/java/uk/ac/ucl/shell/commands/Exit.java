package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.ExitException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Command to exit shell
 */
public class Exit implements CommandInterface {
    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws ExitException {
        commandVisitor.visit();
    }
}