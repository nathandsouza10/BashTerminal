package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.ApplicationException;
import uk.ac.ucl.shell.exceptions.CommandException;
import uk.ac.ucl.shell.exceptions.ExitException;
import uk.ac.ucl.shell.exceptions.OutputWriteException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A command substitution takes a command surrounded by back quotes and replaces this entire string with the result of
 * the command's execution
 */
public class CommandSubstitution implements CommandInterface {
    /**
     * Subcommand
     */
    private final Seq child;

    /**
     * Pre-processing for command substitution. Extracts child parse tree from contents
     *
     * @param tree
     *         Parse tree with a command substitution at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public CommandSubstitution(ShellGrammarParser.CommandSubstitutionContext tree) throws CommandException {
        this.child = new Seq(tree.seq());
    }

    /**
     * Getter for command
     *
     * @return subcommand
     */
    public Seq getChild() {
        return child;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws ApplicationException, CommandException, ExitException, OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}