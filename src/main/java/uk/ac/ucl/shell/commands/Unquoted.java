package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.OutputWriteException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Anything that is not quoted with either single quotes, double quotes or back quotes
 */
public class Unquoted implements CommandInterface {
    /**
     * Value
     */
    private final String text;

    /**
     * No preprocessing is done and the unquoted text is not processed during evaluation
     *
     * @param tree
     *         A parse tree with the unquoted text at root
     */
    public Unquoted(ShellGrammarParser.UnquotedContext tree) {
        this.text = tree.getText();
    }

    /**
     * Getter for value
     *
     * @return value
     */
    public String getText() {
        return text;
    }

    @Override public void eval(CommandVisitor commandVisitor, InputStream input, OutputStream output)
            throws OutputWriteException {
        commandVisitor.visit(this, output);
    }
}