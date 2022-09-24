grammar ShellGrammar;

seq : WHITESPACE? top (WHITESPACE? ';' WHITESPACE? top)*;
top : exit
    | empty
    | command;
exit : 'exit';
empty : EOF;
command : pipe
        | call;
pipe : call (WHITESPACE? '|' WHITESPACE? call)+;
call : (arguments (WHITESPACE? (inputRedirection | outputRedirection))*)
     | (inputRedirection (WHITESPACE? arguments)?)
     | (outputRedirection (WHITESPACE? arguments)?);
arguments : argument (WHITESPACE argument)*;
inputRedirection : '<' (WHITESPACE? argument)?;
outputRedirection : '>' (WHITESPACE? argument)?;
argument : (commandSubstitution | quoted | unquoted)+;
commandSubstitution : '`' WHITESPACE? seq WHITESPACE? '`';
quoted : singleQuoted
       | doubleQuoted;
unquoted : UNQUOTED;
singleQuoted : SINGLEQUOTED;
doubleQuoted : '"' (commandSubstitution | (~'"'))* '"';

WHITESPACE : [ \t]+;
SINGLEQUOTED : '\''(~[\n'])*'\'';
UNQUOTED : ~[ \t'"`\n;|<>]+;