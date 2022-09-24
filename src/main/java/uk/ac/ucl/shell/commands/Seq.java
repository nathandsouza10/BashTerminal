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
import java.util.ArrayList;

/**
 * A sequence of commands seperated by semicolon
 */
public class Seq implements CommandInterface {
    /**
     * List of subcommands
     */
    private final ArrayList<Top> children = new ArrayList<>();

    /**
     * Extracts all the commands seperated by semicolon and adds each command into an array list of children
     *
     * @param tree
     *         Parse tree with a sequence of commands at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Seq(ShellGrammarParser.SeqContext tree) throws CommandException {
        for (ShellGrammarParser.TopContext child : tree.top()) {
            children.add(new Top(child));
        }
    }

    /**
     * Getter for command
     *
     * @return subcommand
     */
    public ArrayList<Top> getChildren() {
        return children;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws ApplicationException, CommandException, ExitException, OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}