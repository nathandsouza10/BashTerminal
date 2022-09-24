package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Redirects to next command, ignores empty command or redirects to exit
 */
public class Top implements CommandInterface {
    /**
     * Subcommand
     */
    private final CommandInterface command;

    /**
     * Redirects to next command, ignores empty command or redirects to exit
     *
     * @param tree
     *         Parse tree with arguments at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    Top(ShellGrammarParser.TopContext tree) throws CommandException {
        if (tree.command() != null) {
            command = new Command(tree.command());
        } else if (tree.exit() != null) {
            command = new Exit();
        } else {
            command = null;
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