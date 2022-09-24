package uk.ac.ucl.shell.commands;

import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A call command executes an application with specified inputs
 *
 * @see Argument
 * @see InputRedirection
 * @see OutputRedirection
 */
public class Call implements CommandInterface {
    /**
     * Subcommand
     */
    private final CommandInterface command;

    /**
     * Preprocessing for call command. If child of tree is an output redirection then create a new output redirection
     * command. If child is an input redirection then create a new input redirection command. Does not allow for chained
     * redirections.
     *
     * @param tree
     *         with call at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Call(ShellGrammarParser.CallContext tree) throws CommandException {
        ParseTree firstChild = tree.getChild(0);
        if (firstChild instanceof ShellGrammarParser.ArgumentsContext) {
            command = handleArgumentsFirst((ShellGrammarParser.ArgumentsContext) firstChild, tree);
        } else if (firstChild instanceof ShellGrammarParser.InputRedirectionContext) {
            command = handleInputRedirectionFirst((ShellGrammarParser.InputRedirectionContext) firstChild, tree);
        } else {
            command = handleOutputRedirectionFirst((ShellGrammarParser.OutputRedirectionContext) firstChild, tree);
        }
    }

    /**
     * Preprocessing for when call begins with any command but input or output redirection
     *
     * @param firstChild
     *         the arguments provided as a parse tree
     * @param tree
     *         the original call parse tree
     *
     * @return Command Interface that is generated from tree
     *
     * @throws CommandException
     *         Error in processing of command
     */
    private CommandInterface handleArgumentsFirst(ShellGrammarParser.ArgumentsContext firstChild,
                                                  ShellGrammarParser.CallContext tree) throws CommandException {
        Arguments result = new Arguments(firstChild);
        int inputRedirectionCount = tree.inputRedirection().size();
        int outputRedirectionCount = tree.outputRedirection().size();
        if (inputRedirectionCount > 1) {
            throw new CommandException("cannot chain input redirections");
        } else if (outputRedirectionCount > 1) {
            throw new CommandException("cannot chain output redirections");
        } else if (inputRedirectionCount == 1) {
            return new InputRedirection(result, tree.inputRedirection(0));
        } else if (outputRedirectionCount == 1) {
            return new OutputRedirection(result, tree.outputRedirection(0));
        } else {
            return result;
        }
    }

    /**
     * Preprocessing for when call begins with input redirection
     *
     * @param firstChild
     *         the input redirection as a parse tree
     * @param tree
     *         the original call parse tree
     *
     * @return Input Redirection command that is created from tree
     *
     * @throws CommandException
     *         Error in processing of command
     */
    private InputRedirection handleInputRedirectionFirst(ShellGrammarParser.InputRedirectionContext firstChild,
                                                         ShellGrammarParser.CallContext tree) throws CommandException {
        ShellGrammarParser.ArgumentsContext arguments = tree.arguments();
        if (arguments != null) {
            return new InputRedirection(new Arguments(arguments), firstChild);
        } else {
            return new InputRedirection(null, firstChild);
        }
    }

    /**
     * Preprocessing for when call begins with output redirection
     *
     * @param firstChild
     *         the output redirection as a parse tree
     * @param tree
     *         the original call parse tree
     *
     * @return Output Redirection command that is created from tree
     *
     * @throws CommandException
     *         Error in processing of command
     */
    private OutputRedirection handleOutputRedirectionFirst(ShellGrammarParser.OutputRedirectionContext firstChild,
                                                           ShellGrammarParser.CallContext tree)
            throws CommandException {
        ShellGrammarParser.ArgumentsContext arguments = tree.arguments();
        if (arguments != null) {
            return new OutputRedirection(new Arguments(arguments), firstChild);
        } else {
            return new OutputRedirection(null, firstChild);
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