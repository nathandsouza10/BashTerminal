package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A command is either a call or a pipe
 *
 * @see Call
 * @see Pipe
 */
public class Command implements CommandInterface {
    /**
     * Subcommand
     */
    private final CommandInterface command;

    /**
     * Preprocessing of a command evaluation. If child is a pipe then a new pipe command is created. If child is a call
     * then a new call command is created. Otherwise, an error is thrown.
     *
     * @param tree
     *         with command at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    Command(ShellGrammarParser.CommandContext tree) throws CommandException {
        if (tree.pipe() != null) {
            command = new Pipe(tree.pipe());
        } else {
            command = new Call(tree.call());
        }
    }

    /**
     * Getter for command
     *
     * @return subcommand
     */
    public CommandInterface getCommand() {
        return command;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}