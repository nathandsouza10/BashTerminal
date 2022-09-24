package uk.ac.ucl.shell;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import uk.ac.ucl.shell.commands.base.CommandVisitor;
import uk.ac.ucl.shell.exceptions.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Main shell class to control command parsing and I/O
 */
public class Shell {
    /**
     * Stores the current working directory
     */
    private static String currentDirectory = System.getProperty("user.dir");

    /**
     * Runs shell program
     *
     * @param args
     *         Specifies shell launch parameters
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                if (args.length != 2) {
                    throw new ShellException("wrong number of arguments");
                }
                if (!args[0].equals("-c")) {
                    throw new ShellException(args[0] + ": unexpected argument");
                }
            } catch (ShellException e) {
                System.out.println(e.getMessage());
                return;
            }
            try {
                eval(args[1], System.out);
            } catch (CommandException | ApplicationException | OutputWriteException e) {
                String message = e.getMessage();
                if (!message.equals("")) {
                    System.out.println(message);
                }
            } catch (ExitException e) {
                exit(0);
            }
        } else {
            try (Scanner input = new Scanner(System.in)) {
                while (true) {
                    String prompt = currentDirectory + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.nextLine();
                        eval(cmdline, System.out);
                    } catch (CommandException | ApplicationException | OutputWriteException e) {
                        String message = e.getMessage();
                        if (!message.equals("")) {
                            System.out.println(message);
                        }
                    } catch (ExitException e) {
                        exit(0);
                    }
                }
            }
        }
    }

    /**
     * Evaluates command provided and outputs to specified output stream
     *
     * @param cmdline
     *         Command to be executed
     * @param output
     *         Output stream to write results to
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         * thrown if an output stream cannot be written to
     */
    public static void eval(String cmdline, OutputStream output)
            throws CommandException, ApplicationException, ExitException, OutputWriteException {
        CharStream parserInput = CharStreams.fromString(cmdline);
        ShellGrammarLexer lexer = new ShellGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ShellGrammarParser parser = new ShellGrammarParser(tokenStream);
        ShellGrammarParser.SeqContext tree = parser.seq();
        CommandVisitor commandVisitor = new CommandVisitor(tree, currentDirectory, output);
        currentDirectory = commandVisitor.getUpdatedDirectory();
        try {
            output.flush();
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Returns current working directory
     *
     * @return current working directory
     */
    public static String getCurrentDirectory() {
        return currentDirectory;
    }

    /**
     * Sets the working directory of the shell
     *
     * @param newDirectory
     *         New working directory to be set
     */
    public static void setCurrentDirectory(String newDirectory) {
        currentDirectory = newDirectory;
    }
}