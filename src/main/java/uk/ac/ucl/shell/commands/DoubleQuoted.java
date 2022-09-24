package uk.ac.ucl.shell.commands;

import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Double Quotes allows for multiple arguments to be grouped and passed together as one argument.
 */
public class DoubleQuoted implements CommandInterface {
    /**
     * Subcommand
     */
    private final ArrayList<CommandInterface> children = new ArrayList<>();

    /**
     * Tree with double-quoted argument as the root of the tree. Extracts whether child is a command substitution or an
     * undefined command.
     *
     * @param tree
     *         Parse tree with double-quoted argument at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public DoubleQuoted(ShellGrammarParser.DoubleQuotedContext tree) throws CommandException {
        for (ParseTree child : tree.children) {
            if (child instanceof ShellGrammarParser.CommandSubstitutionContext) {
                children.add(new CommandSubstitution((ShellGrammarParser.CommandSubstitutionContext) child));
            } else if (!child.getText().equals("\"")) {
                children.add(new SingleCharacter(child));
            }
        }
    }

    /**
     * Getter for command
     *
     * @return subcommand
     */
    public ArrayList<CommandInterface> getChildren() {
        return children;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}