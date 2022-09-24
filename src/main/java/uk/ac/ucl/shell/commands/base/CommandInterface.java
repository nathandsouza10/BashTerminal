package uk.ac.ucl.shell.commands.base;

import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface defining properties of a command
 */
public interface CommandInterface {
    /**
     * Evaluates command
     *
     * @param commandVisitor
     *         Instance of visitor to be used for processing of evaluation
     * @param input
     *         Input data to be passed to command
     * @param output
     *         Output stream for result to be written to
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws CommandException, ApplicationException, UnsafeApplicationException, ExitException,
                   OutputWriteException;
}