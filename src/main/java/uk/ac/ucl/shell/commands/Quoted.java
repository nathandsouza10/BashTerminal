package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Quoting is used to remove the special meaning of certain characters or words to the shell.
 */
public class Quoted implements CommandInterface {
    /**
     * Subcommand
     */
    private final CommandInterface child;

    /**
     * Tree with quoted argument as root. Extracts whether the quoted argument is single quoted or double-quoted.
     *
     * @param tree
     *         Parse tree with quoted argument at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Quoted(ShellGrammarParser.QuotedContext tree) throws CommandException {
        if (tree.singleQuoted() != null) {
            this.child = new SingleQuoted(tree.singleQuoted());
        } else {
            this.child = new DoubleQuoted(tree.doubleQuoted());
        }
    }

    /**
     * Getter for command
     *
     * @return subcommand
     */
    public CommandInterface getChild() {
        return child;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}