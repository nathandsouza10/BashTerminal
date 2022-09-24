package uk.ac.ucl.shell.commands;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.OutputWriteException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Single quotes are used to disable interpretation of special characters. Note that the rule for double quotes is
 * different from single quotes: double quotes do not disable interpretation of back quotes.
 */
public class SingleQuoted implements CommandInterface {
    /**
     * Value
     */
    private final String text;

    /**
     * Extracts the text within the single quotes for it to be evaluated.
     *
     * @param tree
     *         A parse tree with single quoted text at root
     */
    public SingleQuoted(ShellGrammarParser.SingleQuotedContext tree) {
        String text = tree.getText();
        this.text = text.substring(1, text.length() - 1);
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