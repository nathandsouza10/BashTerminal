package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * An input redirections passes the contents of a file as an input to another command. For example, grep "Interesting
 * String" &lt; text1.txt finds all lines of the file text1.txt that contain the string Interesting String as a
 * substring
 */
public class InputRedirection implements CommandInterface {
    /**
     * Command to be executed
     */
    private final CommandInterface left;
    /**
     * Path to input data
     */
    private final String right;

    /**
     * Pre-processing for input redirection. Extracts filename to be read from and parse tree for command input is
     * passed to
     *
     * @param left
     *         Command interface that describes properties of the command on the left of input redirection
     * @param tree
     *         Parse tree with an input redirection at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    InputRedirection(CommandInterface left, ShellGrammarParser.InputRedirectionContext tree) throws CommandException {
        this.right = validateSetFilename(tree);
        this.left = left;
    }

    /**
     * Extracts filename of file to be read from
     *
     * @param tree
     *         Tree with an input redirection at root which will contain filename as an argument
     *
     * @return Filename
     *
     * @throws CommandException
     *         Error in processing of command
     */
    private String validateSetFilename(ShellGrammarParser.InputRedirectionContext tree) throws CommandException {
        ShellGrammarParser.ArgumentContext argument = tree.argument();
        if (argument != null) {
            return argument.getText();
        } else {
            throw new CommandException("incorrect number/type of arguments");
        }
    }

    /**
     * Getter for command
     *
     * @return Command to be executed
     */
    public CommandInterface getLeft() {
        return left;
    }

    /**
     * Getter for file name
     *
     * @return Path to input data
     */
    public String getRight() {
        return right;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, output);
    }
}