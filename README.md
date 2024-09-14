
# BashTerminal

## Overview

**BashTerminal** is a custom shell that implements various widely-used UNIX commands such as `cd`, `pwd`, `ls`, `cat`, `echo`, and many more. Unlike traditional UNIX shells, BashTerminal offers some distinct features, such as executing applications inside the shell process and handling exceptions instead of exit codes.

## Features

- Supports standard UNIX commands: `cd`, `pwd`, `ls`, `cat`, `echo`, `grep`, `find`, `sort`, `uniq`, `cut`, and more.
- Applications execute inside the shell process instead of spawning new processes.
- Exception handling is used in place of exit codes.
- Supports input redirection and piping between commands.
- Command substitution and globbing for dynamic command execution.
- Safe and unsafe versions of commands for flexibility.

## Applications

BashTerminal provides implementations for the following UNIX commands:

- **pwd**: Outputs the current working directory.
- **cd**: Changes the current working directory.
- **ls**: Lists contents of a directory.
- **cat**: Concatenates and displays the content of files.
- **echo**: Prints arguments to stdout.
- **head/tail**: Display the beginning or end of files.
- **grep**: Searches for patterns in files.
- **find**: Searches for files and directories.
- **sort**: Sorts lines of text files.
- **uniq**: Filters out repeated lines.
- **cut**: Removes sections from each line of files.

For a full list of commands and detailed usage, refer to the [Applications](applications.md) file.

## Command Syntax and Interface

The shell supports various command-line features:

1. **Command Substitution**: Embed one command's output into another command using backquotes, e.g., `` wc -l `find -name '*.java'` ``.
2. **Globbing**: Use the `*` (asterisk) to match file patterns.
3. **Input Redirection and Piping**: Connect the output of one command to another.

For a more in-depth explanation of the command interface, visit the [Interface Documentation](interface.md).

## Language Features

- Command-level parsing and exception handling.
- Support for redirection and pipeline operations.
- Globbing and command substitution.

For technical details about language features and implementation, refer to the [Language Documentation](language.md).

## Setup & Installation

To install and run BashTerminal, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/BashTerminal.git
   cd BashTerminal
   ```

2. Build and run the terminal:
   ```bash
   make
   ./bash_terminal
   ```

3. Use the shell as you would with any other UNIX shell, entering commands and leveraging redirection, piping, and substitution.

## Usage Examples

### Basic Commands

```bash
pwd           # Outputs the current directory
cd /home      # Changes the current directory to /home
ls            # Lists files in the current directory
```

### Using Pipes and Redirection

```bash
ls | grep 'file'      # Lists files matching the pattern 'file'
cat file.txt > new.txt # Redirects content from file.txt to new.txt
```

### Command Substitution

```bash
wc -l `find -name '*.java'`  # Counts lines in all .java files
```

## Testing

We used **unit testing** and **integration testing** to ensure the reliability and correctness of BashTerminal. The testing process included:

- **Unit Tests**: Each individual command (e.g., `cd`, `ls`, `cat`) was tested for various edge cases and input scenarios.
- **Integration Tests**: Commands were tested together to ensure they interact correctly when piped or used in sequence.
  
To evaluate test coverage, we used **line coverage** and **branch coverage** tools. These tools helped identify untested lines of code and ensure that both simple and complex code paths were thoroughly validated.

### Running Tests

To run the tests, execute the following command in the terminal:

```bash
make test
```

This will run the full suite of unit and integration tests and generate coverage reports.

## Development Workflow

During the development of BashTerminal, we adopted a robust workflow involving **pull requests** and continuous integration tools to maintain high code quality and collaboration efficiency:

- **Pull Requests (PRs)**: All changes were submitted via pull requests, allowing team members to review and discuss the code before merging it into the main branch. This process helped to maintain code consistency and catch potential issues early through peer reviews.
  
- **DevOps Tools**: We integrated continuous integration (CI) pipelines using tools such as **GitHub Actions** and **Travis CI**. Every time a pull request was created or updated, automated tests were triggered to ensure that new changes did not introduce regressions. These pipelines also provided real-time feedback on test coverage, build status, and code quality.

The combination of PR-based workflows and DevOps tools ensured that development was smooth, bugs were minimized, and code coverage remained high.

