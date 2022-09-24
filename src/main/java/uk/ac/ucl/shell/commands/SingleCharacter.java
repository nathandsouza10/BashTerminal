package uk.ac.ucl.shell.commands;

import org.antlr.v4.runtime.tree.ParseTree;
import uk.ac.ucl.shell.commands.base.CommandInterface;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.OutputWriteException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Any undefined text or command (should be a single unmatched character from parser)
 */
public class SingleCharacter implements CommandInterface {
    /**
     * Value
     */
    private final String text;

    /**
     * Extracts any text in parse tree.
     *
     * @param tree
     *         A parse tree with the undefined text/command at root
     */
    public SingleCharacter(ParseTree tree) {
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