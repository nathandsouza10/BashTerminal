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
 * Represents an argument to be passed to a command. Can be unquoted, single quoted, double-quoted or a command
 * substitution (back quoted) or some combination of multiple of these concatenated
 *
 * @see Unquoted
 * @see Quoted
 * @see CommandSubstitution
 */
public class Argument implements CommandInterface {
    /**
     * List of parts of argument (commandSubstitution | quoted | unquoted)+
     */
    private final ArrayList<CommandInterface> children = new ArrayList<>();
    /**
     * Indicates whether argument is compatible for globbing
     */
    private final boolean globs;

    /**
     * Pre-processing for argument handling. A list is built of each quoted, unquoted and command substitution in order
     * which makes up the command to be evaluated by the visitor. If conditions are met that an argument may glob, this
     * is also identified
     *
     * @param tree
     *         Parse tree with an argument at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Argument(ShellGrammarParser.ArgumentContext tree) throws CommandException {
        globs = tree.getChild(0) instanceof ShellGrammarParser.UnquotedContext && tree.getChildCount() == 1;
        for (ParseTree child : tree.children) {
            if (child instanceof ShellGrammarParser.QuotedContext) {
                this.children.add(new Quoted((ShellGrammarParser.QuotedContext) child));
            } else if (child instanceof ShellGrammarParser.UnquotedContext) {
                this.children.add(new Unquoted((ShellGrammarParser.UnquotedContext) child));
            } else {
                this.children.add(new CommandSubstitution((ShellGrammarParser.CommandSubstitutionContext) child));
            }
        }
    }

    /**
     * Getter for children
     *
     * @return List of parts of argument (commandSubstitution | quoted | unquoted)+
     */
    public ArrayList<CommandInterface> getChildren() {
        return children;
    }

    /**
     * Getter for globs
     *
     * @return Indicates whether argument is compatible for globbing
     */
    public boolean isGlobs() {
        return globs;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}