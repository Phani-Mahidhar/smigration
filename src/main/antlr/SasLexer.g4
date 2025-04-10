lexer grammar SasLexer;

@header {
package com.sas.parser;
}

// Lexer tokens in precedence order
// 1. Special keywords that could be mistaken for identifiers
NOCENTER: [Nn][Oo][Cc][Ee][Nn][Tt][Ee][Rr];

// 2. Core SAS keywords
OPTIONS: [Oo][Pp][Tt][Ii][Oo][Nn][Ss];
OPTION: [Oo][Pp][Tt][Ii][Oo][Nn];
OUT: [Oo][Uu][Tt];
DATA: [Dd][Aa][Tt][Aa];
PROC: [Pp][Rr][Oo][Cc];
CONTENTS: [Cc][Oo][Nn][Tt][Ee][Nn][Tt][Ss];
RUN: [Rr][Uu][Nn];
SET: [Ss][Ee][Tt];
NULL: '_NULL_';
TO: [Tt][Oo];
BY: [Bb][Yy];
WHILE: [Ww][Hh][Ii][Ll][Ee];
UNTIL: [Uu][Nn][Tt][Ii][Ll];
INPUT: [Ii][Nn][Pp][Uu][Tt];

// Add SQL keywords after PROC
SQL: [Ss][Qq][Ll];
SELECT: [Ss][Ee][Ll][Ee][Cc][Tt];
FROM: [Ff][Rr][Oo][Mm];
INTO: [Ii][Nn][Tt][Oo];
QUIT: [Qq][Uu][Ii][Tt];
COUNT: [Cc][Oo][Uu][Nn][Tt];

// Add SQL-specific keywords
CREATE: [Cc][Rr][Ee][Aa][Tt][Ee];
TABLE: [Tt][Aa][Bb][Ll][Ee];
VIEW: [Vv][Ii][Ee][Ww];
GROUP: [Gg][Rr][Oo][Uu][Pp];
INSERT: [Ii][Nn][Ss][Ee][Rr][Tt];
UPDATE: [Uu][Pp][Dd][Aa][Tt][Ee];
DELETE: [Dd][Ee][Ll][Ee][Tt][Ee];
VALUES: [Vv][Aa][Ll][Uu][Ee][Ss];
INNER: [Ii][Nn][Nn][Ee][Rr];
LEFT: [Ll][Ee][Ff][Tt];
RIGHT: [Rr][Ii][Gg][Hh][Tt];
FULL: [Ff][Uu][Ll][Ll];
OUTER: [Oo][Uu][Tt][Ee][Rr];
JOIN: [Jj][Oo][Ii][Nn];
ON: [Oo][Nn];
ASC: [Aa][Ss][Cc];
DESC: [Dd][Ee][Ss][Cc];
AS: [Aa][Ss];
DISTINCT: [Dd][Ii][Ss][Tt][Ii][Nn][Cc][Tt];
HAVING: [Hh][Aa][Vv][Ii][Nn][Gg];

// Add SQL data type keywords
CHAR: [Cc][Hh][Aa][Rr];
VARCHAR: [Vv][Aa][Rr][Cc][Hh][Aa][Rr];
DATE: [Dd][Aa][Tt][Ee];
TIME: [Tt][Ii][Mm][Ee];
DATETIME: [Dd][Aa][Tt][Ee][Tt][Ii][Mm][Ee];
LENGTH: [Ll][Ee][Nn][Gg][Tt][Hh];

// Add CONTENTS-specific keywords
NOPRINT: [Nn][Oo][Pp][Rr][Ii][Nn][Tt];
PRINT: [Pp][Rr][Ii][Nn][Tt];
SHORT: [Ss][Hh][Oo][Rr][Tt];
DETAILS: [Dd][Ee][Tt][Aa][Ii][Ll][Ss];
VARNUM: [Vv][Aa][Rr][Nn][Uu][Mm];
ORDER: [Oo][Rr][Dd][Ee][Rr];
ASCENDING: [Aa][Ss][Cc][Ee][Nn][Dd][Ii][Nn][Gg];
FORMATWIDTH: [Ff][Oo][Rr][Mm][Aa][Tt][Ww][Ii][Dd][Tt][Hh];

// 3. Macro-related tokens
MACRO: '%'[Mm][Aa][Cc][Rr][Oo];
MEND: '%'[Mm][Ee][Nn][Dd];
LET: '%'[Ll][Ee][Tt];
THEN: '%'[Tt][Hh][Ee][Nn];
ELSE: '%'[Ee][Ll][Ss][Ee];
IF: '%'[Ii][Ff];
PUT: '%'[Pp][Uu][Tt];
INCLUDE: '%'[Ii][Nn][Cc][Ll][Uu][Dd][Ee];
INC: '%'[Ii][Nn][Cc];
DO: '%'[Dd][Oo];
END: '%'[Ee][Nn][Dd];

// Macro Function
M_FUNCTION_ID: '%'ID;
MACRO_VARIABLE: '&' ID DOT?;

// 4. Function and statement keywords
LIBNAME: [Ll][Ii][Bb][Nn][Aa][Mm][Ee];
FORMAT: [Ff][Oo][Rr][Mm][Aa][Tt];
MERGE: [Mm][Ee][Rr][Gg][Ee];
OUTPUT: [Oo][Uu][Tt][Pp][Uu][Tt];
CALL: [Cc][Aa][Ll][Ll];
WHERE: [Ww][Hh][Ee][Rr][Ee];
RENAME: [Rr][Ee][Nn][Aa][Mm][Ee];
INFILE: [Ii][Nn][Ff][Ii][Ll][Ee];
TRUNCOVER: [Tt][Rr][Uu][Nn][Cc][Oo][Vv][Ee][Rr];
FIRSTOBS: [Ff][Ii][Rr][Ss][Tt][Oo][Bb][Ss];
DLM: [Dd][Ll][Mm];
DSD: [Dd][Ss][Dd];
MISSOVER: [Mm][Ii][Ss][Ss][Oo][Vv][Ee][Rr];
LINESIZE: [Ll][Ii][Nn][Ee][Ss][Ii][Zz][Ee];
LS: [Ll][Ss];
MISSING: [Mm][Ii][Ss][Ss][Ii][Nn][Gg];
EXECUTE: [Ee][Xx][Ee][Cc][Uu][Tt];
COLUMN: [Cc][Oo][Ll][Uu][Mm][Nn];
KEEP: [Kk][Ee][Ee][Pp];
DROP: [Dd][Rr][Oo][Pp];

// 5. Operators and special characters
SEMICOLON: ';';
COMMA: ',';
DOT: '.';
EQUALS: '=' | [Ee][Qq];
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
POW: '**';

COLON: ':';
AT: '@';

// 6. Comparison operators
NE: '<>' | [Nn][Ee];
LT: '<' | [Ll][Tt];
LE: '<=' | [Ll][Ee];
GT: '>' | [Gg][Tt];
GE: '>=' | [Gg][Ee];
LIKE: [Ll][Ii][Kk][Ee];
BETWEEN: [Bb][Ee][Tt][Ww][Ee][Ee][Nn];
IS: [Ii][Ss];

// 7. Logical operators
AND: [Aa][Nn][Dd];
OR: [Oo][Rr];
NOT: [Nn][Oo][Tt];
IN: [Ii][Nn];
CONTAINS: [Cc][Oo][Nn][Tt][Aa][Ii][Nn][Ss];

// 9. Basic tokens (must come after all keywords)
WORD: ID;
STRING: ('"' (~["\r\n])* '"') | ('\'' (~['\r\n])* '\'');
NUMBER: [0-9]+ ('.' [0-9]+)?;

// 10. Comments and whitespace (last)
COMMENT: ('/*' .*? '*/') -> channel(HIDDEN);
LINE_COMMENT: ('\r' | '\n') ('*' | '%*') ~[\r\n]* -> channel(HIDDEN);
WS: [ \t\r\n\f]+ -> skip;

fragment ID: [a-zA-Z_][a-zA-Z0-9_]*;
