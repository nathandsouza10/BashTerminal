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
 * List of arguments
 */
public class Arguments implements CommandInterface {
    /**
     * Name of application (first argument)
     */
    private final Argument appName;
    /**
     * Rest of arguments
     */
    private final ArrayList<Argument> arguments = new ArrayList<>();

    /**
     * A list of argument objects is created.
     *
     * @param tree
     *         Parse tree with arguments at root
     *
     * @throws CommandException
     *         Error in constructing of command
     */
    public Arguments(ShellGrammarParser.ArgumentsContext tree) throws CommandException {
        List<ShellGrammarParser.ArgumentContext> treeArguments = tree.argument();
        appName = new Argument(treeArguments.get(0));
        for (ShellGrammarParser.ArgumentContext child : treeArguments.subList(1, treeArguments.size())) {
            arguments.add(new Argument(child));
        }
    }

    /**
     * Getter for appName
     *
     * @return Name of application (first argument)
     */
    public Argument getAppName() {
        return appName;
    }

    /**
     * Getter for arguments
     *
     * @return Rest of arguments
     */
    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        commandVisitor.visit(this, input, output);
    }
}