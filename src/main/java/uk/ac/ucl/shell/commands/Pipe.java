package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Pipe passes the result of a call as an argument ot the right hand side command. For example, sort file.txt | uniq -
 * will first sort file.txt and then the uniq operation is called on the result
 */
public class Pipe implements CommandInterface {
    /**
     * First subcommand
     */
    private final Call firstChild;
    /**
     * Subcommands between first and last (can contain none)
     */
    private final ArrayList<Call> children = new ArrayList<>();
    /**
     * Final subcommand
     */
    private final Call lastChild;

    /**
     * Pre-processing for Pipe. Extracts the result after executing the left hand  to be read from and parse tree for
     * command input is passed to
     *
     * @param tree
     *         Parse tree with a pipe operator('|') at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Pipe(ShellGrammarParser.PipeContext tree) throws CommandException {
        List<ShellGrammarParser.CallContext> calls = tree.call();
        firstChild = new Call(calls.get(0));
        int callCount = calls.size();
        for (ShellGrammarParser.CallContext child : calls.subList(1, callCount - 1)) {
            children.add(new Call(child));
        }
        lastChild = new Call(calls.get(callCount - 1));
    }

    /**
     * Getter for commands
     *
     * @return First subcommand
     */
    public Call getFirstChild() {
        return firstChild;
    }

    /**
     * Getter for commands
     *
     * @return Subcommands between first and last (can contain none)
     */
    public ArrayList<Call> getChildren() {
        return children;
    }

    /**
     * Getter for commands
     *
     * @return Final subcommand
     */
    public Call getLastChild() {
        return lastChild;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}