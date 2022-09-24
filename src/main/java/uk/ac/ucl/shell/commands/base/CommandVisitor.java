package uk.ac.ucl.shell.commands.base;

import uk.ac.ucl.shell.ShellGrammarParser;
import uk.ac.ucl.shell.applications.base.ApplicationFactory;
import uk.ac.ucl.shell.commands.*;
import uk.ac.ucl.shell.exceptions.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Visitor for evaluating combinations of shell commands
 */
public class CommandVisitor {
    /**
     * Current working directory
     */
    private String currentDirectory;

    /**
     * Constructor that creates the visitor
     *
     * @param tree
     *         Parser result tree
     * @param currentDirectory
     *         Current shell working directory
     * @param output
     *         output stream to be written to
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
    public CommandVisitor(ShellGrammarParser.SeqContext tree, String currentDirectory, OutputStream output)
            throws CommandException, OutputWriteException, ApplicationException, ExitException {
        this.currentDirectory = currentDirectory;
        visit(new Seq(tree), null, output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param seq
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    public void visit(Seq seq, InputStream input, OutputStream output)
            throws OutputWriteException, ApplicationException, CommandException, ExitException {
        for (Top child : seq.getChildren()) {
            try {
                child.eval(this, input, output);
            } catch (UnsafeApplicationException e) {
                String message = e.getMessage();
                if (!message.equals("")) {
                    writeStringToOutputStream(e.getMessage() + System.lineSeparator(), output);
                }
            }
        }
    }

    /**
     * Takes a string and writes it to a stream
     *
     * @param input
     *         value to be written
     * @param outputStream
     *         stream to write result to
     *
     * @throws OutputWriteException
     *         thrown if stream cannot be written to
     */
    private void writeStringToOutputStream(String input, OutputStream outputStream) throws OutputWriteException {
        try {
            for (int characterIndex = 0; characterIndex < input.length(); ++characterIndex) {
                outputStream.write(input.charAt(characterIndex));
            }
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Returns current active directory after executing the command (for updating currentDirectory e.g. after cd)
     *
     * @return Updated directory path
     */
    public String getUpdatedDirectory() {
        return currentDirectory;
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param top
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Top top, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        if (top.getCommand() != null) {
            top.getCommand().eval(this, input, output);
        }
    }

    /**
     * Exits shell
     *
     * @throws ExitException
     *         exit called within command
     */
    public void visit() throws ExitException {
        throw new ExitException();
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param command
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Command command, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        command.getCommand().eval(this, input, output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param pipe
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Pipe pipe, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        File previousFile = new File("_previous.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(previousFile);
            pipe.getFirstChild().eval(this, input, fileOutputStream);
            for (Call child : pipe.getChildren()) {
                File nextFile = new File("_next.txt");
                fileOutputStream = new FileOutputStream(nextFile);
                FileInputStream fileInputStream = new FileInputStream(previousFile);
                child.eval(this, fileInputStream, fileOutputStream);
                if (!nextFile.renameTo(previousFile)) {
                    throw new OutputWriteException();
                }
                nextFile.deleteOnExit();
            }
            FileInputStream fileInputStream = new FileInputStream(previousFile);
            pipe.getLastChild().eval(this, fileInputStream, output);
        } catch (IOException e) {
            throw new OutputWriteException();
        }
        previousFile.deleteOnExit();
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param call
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Call call, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        call.getCommand().eval(this, input, output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param inputRedirection
     *         Command to be evaluated
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(InputRedirection inputRedirection, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(currentDirectory + File.separator + inputRedirection.getRight());
        } catch (FileNotFoundException e) {
            throw new CommandException("cannot find file");
        }
        if (inputRedirection.getLeft() != null) {
            inputRedirection.getLeft().eval(this, fileInputStream, output);
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param outputRedirection
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(OutputRedirection outputRedirection, InputStream input)
            throws CommandException, UnsafeApplicationException, ApplicationException, ExitException,
                   OutputWriteException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (outputRedirection.getLeft() != null) {
            outputRedirection.getLeft().eval(this, input, byteArrayOutputStream);
        }
        try {
            byteArrayOutputStream.close();
            File file = new File(currentDirectory + File.separator + outputRedirection.getRight());
            if (!file.exists() && !file.createNewFile()) {
                throw new CommandException("Output file cannot be created");
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(byteArrayOutputStream.toString().strip());
            writer.close();
            fw.close();
        } catch (IOException e) {
            throw new CommandException("Cannot write to file");
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param arguments
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Arguments arguments, InputStream input, OutputStream output)
            throws ApplicationException, UnsafeApplicationException, CommandException, ExitException,
                   OutputWriteException {
        String appName;
        ArrayList<String> appArgs = new ArrayList<>();
        ByteArrayOutputStream appNameOutput = new ByteArrayOutputStream();
        arguments.getAppName().eval(this, input, appNameOutput);
        try {
            appNameOutput.close();
            appName = appNameOutput.toString();
            int counter = 0;
            int lastArgumentIndex = arguments.getArguments().size() - 1;
            for (Argument argument : arguments.getArguments()) {
                ByteArrayOutputStream argumentOutput = new ByteArrayOutputStream();
                argument.eval(this, input, argumentOutput);
                argumentOutput.close();
                if (argument.isGlobs() && counter == lastArgumentIndex) {
                    appArgs.addAll(Globbing.globbing(argumentOutput.toString(), currentDirectory));
                } else {
                    appArgs.add(argumentOutput.toString());
                }
                ++counter;
            }
            currentDirectory = new ApplicationFactory().buildCommand(appName, input, output, appArgs, currentDirectory)
                                                       .run();
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param argument
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Argument argument, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        for (CommandInterface child : argument.getChildren()) {
            child.eval(this, input, output);
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param commandSubstitution
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    public void visit(CommandSubstitution commandSubstitution, InputStream input, OutputStream output)
            throws ApplicationException, CommandException, ExitException, OutputWriteException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        commandSubstitution.getChild().eval(this, input, byteArrayOutputStream);
        try {
            byteArrayOutputStream.close();
            writeStringToOutputStream(byteArrayOutputStream.toString().strip().replaceAll(System.lineSeparator(), " "),
                                      output);
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param quoted
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(Quoted quoted, InputStream input, OutputStream output)
            throws UnsafeApplicationException, ApplicationException, CommandException, ExitException,
                   OutputWriteException {
        quoted.getChild().eval(this, input, output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param unquoted
     *         Command to be evaluated
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    public void visit(Unquoted unquoted, OutputStream output) throws OutputWriteException {
        writeStringToOutputStream(unquoted.getText(), output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param singleQuoted
     *         Command to be evaluated
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    public void visit(SingleQuoted singleQuoted, OutputStream output) throws OutputWriteException {
        writeStringToOutputStream(singleQuoted.getText(), output);
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param doubleQuoted
     *         Command to be evaluated
     * @param input
     *         Input data to be passed to subcommands
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws CommandException
     *         error in processing of command
     * @throws ApplicationException
     *         error in processing of application within command
     * @throws ExitException
     *         exit called within command
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     * @throws UnsafeApplicationException
     *         error in processing of unsafe application within command
     */
    public void visit(DoubleQuoted doubleQuoted, InputStream input, OutputStream output)
            throws OutputWriteException, UnsafeApplicationException, ApplicationException, CommandException,
                   ExitException {
        PipedInputStream tempInputStream = new PipedInputStream();
        try {
            PipedOutputStream tempOutputStream = new PipedOutputStream(tempInputStream);
            for (CommandInterface child : doubleQuoted.getChildren()) {
                child.eval(this, input, tempOutputStream);
            }
            tempOutputStream.close();
            tempInputStream.transferTo(output);
            tempInputStream.close();
        } catch (IOException e) {
            throw new OutputWriteException();
        }
    }

    /**
     * Performs final evaluation of command and returns the resultant shell output string
     *
     * @param undefinedCommand
     *         Command to be evaluated
     * @param output
     *         Output stream to be passed to subcommands
     *
     * @throws OutputWriteException
     *         thrown if an output stream cannot be written to
     */
    public void visit(SingleCharacter undefinedCommand, OutputStream output) throws OutputWriteException {
        writeStringToOutputStream(undefinedCommand.getText(), output);
    }
}