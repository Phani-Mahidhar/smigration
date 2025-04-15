parser grammar SasParser;

@header {
package com.sas.parser;
}

options {
    tokenVocab = SasLexer;
}

// Program structure
program: (programStatement | comment)+ EOF;

programStatement
    : globalStatement
    | dataStepStatement
    | procStepStatement
    | macroStatement
    ;

macroStatement
    : letStatement
    | putStatement
    | ifStatement
    | macroFunctionDefStatement
    | macroFunctionCall
    ;

globalStatement
    : libraryStatement
    | callStatement
    | optionsStatement
    | infileStatement
    | inputStatement
    | incStatement
    ;

dataStepStatement
    : DATA (datasetName | NULL) (dataOptions)? SEMICOLON
      (dataStepContent)*
      RUN SEMICOLON
    ;

dataStepContent
    : globalStatement
    | keepStatement
    | whereClause
    | setStatement
    ;

// Add IF statement rules
ifStatement
    : IF condition THEN doStatement (ELSE doStatement)?
    ;

doStatement
    : simpleDo
    | iterativeDo
    | whileDo
    | untilDo
    ;

simpleDo
    : DO SEMICOLON
      dataStepStatement*
      END SEMICOLON
    ;

iterativeDo
    : DO identifier EQUALS expression TO expression (BY expression)? SEMICOLON
      (dataStepContent)*
      END SEMICOLON
    ;

whileDo
    : DO WHILE LPAREN condition RPAREN SEMICOLON
      (dataStepContent)*
      END SEMICOLON
    ;

untilDo
    : DO UNTIL LPAREN condition RPAREN SEMICOLON
      (dataStepContent)*
      END SEMICOLON
    ;

keepStatement
    : KEEP keepList SEMICOLON
    ;

procStepStatement
    : PROC procType procOptions? SEMICOLON
      procStepContent*
      (RUN | QUIT) SEMICOLON
    ;

procType
    : SQL
    | CONTENTS contentsOptions?
    | identifier
    ;

contentsOptions
    : DATA EQUALS (datasetName)
      (OUT EQUALS datasetName)?
    ;

procStepContent
    : sqlStatement
    | globalStatement
    ;

// Simplified SQL-related rules
sqlStatement
    : selectStatement
    ;


selectStatement
    : SELECT distinctClause? selectItems
      FROM tableReference
      (joinClause)*
      (whereClause)?
      (groupByClause)?
      (havingClause)?
      (orderByClause)?
      (INTO COLON identifier)?
      SEMICOLON
    ;

distinctClause: DISTINCT;

selectItems
    : MULT                                           // SELECT *
    | selectItem (COMMA selectItem)*                 // col1, col2, ...
    ;

selectItem
    : (tableRef DOT)? columnName (AS? alias)?       // table.col AS alias
    | aggregateFunction (AS? alias)?                // COUNT(*) AS total
    | expression (AS? alias)?                       // expr AS alias
    ;

aggregateFunction
    : COUNT LPAREN MULT RPAREN              // COUNT(*)
    | WORD LPAREN columnName RPAREN  // Other aggregates
    ;

tableReference
    : tableRef (AS? alias)?
    ;

joinClause
    : joinType? JOIN tableReference ON joinCondition
    ;

joinType
    : INNER
    | (LEFT | RIGHT | FULL) OUTER?
    ;

joinCondition
    : condition
    ;

groupByClause
    : GROUP BY groupByItem (COMMA groupByItem)*
    ;

groupByItem
    : columnName
    | expression
    ;

havingClause
    : HAVING condition
    ;

orderByClause
    : ORDER BY orderByItem (COMMA orderByItem)*
    ;

orderByItem
    : columnName (ASC | DESC)?
    | expression (ASC | DESC)?
    ;

tableRef: (datasetName DOT)? identifier;
columnName: identifier;
alias: identifier;

macroFunctionDefStatement
    : MACRO identifier (LPAREN macroParams? RPAREN)? SEMICOLON
      (programStatement)*
      MEND (identifier)? SEMICOLON
    ;

// Expression handling
expression
    : LPAREN expression RPAREN
    | (PLUS | MINUS) expression
    | expression POW expression
    | expression (MULT | DIV) expression
    | expression (PLUS | MINUS) expression
    | functionExpression
    | macroFunctionCall
    | macroVariable
    | literal
    ;

// Function handling
functionExpression
    : identifier LPAREN functionArgList? RPAREN
    ;

// Format handling
format
    : WORD (DOT)?
    ;

// Common elements
datasetName: (identifier | macroVariable) (DOT (identifier | macroVariable))?;
macroVariable: MACRO_VARIABLE;
identifier: WORD | INPUT | OUT;
literal: STRING | NUMBER | WORD;

// Options and parameters
optionsAndParameters
    : LBRACE option (COMMA? option)* RBRACE
    ;

option
    : identifier (EQUALS (expression | STRING))?
    ;

// Missing statement definitions
letStatement: LET assignment SEMICOLON;

// Update putStatement rule
putStatement: PUT (~SEMICOLON)+ SEMICOLON;

libraryStatement: LIBNAME libraryName (STRING | identifier) libOptions? SEMICOLON;

libraryName: macroVariable
    | identifier
    ;

callStatement
    : CALL expression SEMICOLON
    ;

// Missing option definitions
libOptions: optionsAndParameters;
procOptions: optionsAndParameters;
dataOptions
    : optionsAndParameters
    | formatOption
    | whereClause
    | (optionsAndParameters whereClause)
    | (whereClause optionsAndParameters)
    ;

formatOption: FORMAT formatList;
formatList: formatItem (COMMA formatItem)*;
formatItem: identifier format;

whereClause
    : WHERE whereExpression SEMICOLON
    ;

whereExpression
    : whereCondition
    | LPAREN whereExpression RPAREN
    | whereExpression AND whereExpression
    | whereExpression OR whereExpression
    | NOT whereExpression
    ;

whereCondition
    : identifier comparison (literal | identifier | macroVariable)
    | identifier IN LPAREN (literal | identifier | macroVariable) (COMMA (literal | identifier | macroVariable))* RPAREN
    | identifier CONTAINS STRING
    | FORMAT EQUALS (literal | identifier | macroVariable)
    ;

// Missing parameter definitions
functionArgList: functionArg (COMMA functionArg)*;
functionArg: (expression | format);
macroParams: macroParam (COMMA macroParam)* (COMMA)?;
macroParam: identifier (EQUALS | EQUALS expression)?;

// Data step specific statements
setStatement: SET datasetName setOptions? SEMICOLON;

setOptions
    : DIV LPAREN setOptionsList RPAREN
    | dataOptions
    ;

keepList
    : keepItem (keepItem)*
    ;

keepItem
    : identifier COLON?    // For prefix matches like IND_USER:
    | identifier
    ;

dropOption
    : DROP EQUALS? identifierList
    ;

setOptionsList
    : setOption (COMMA setOption)*
    ;

setOption
    : renameClause
    | option
    ;

renameClause
    : RENAME EQUALS LPAREN renameList RPAREN
    ;

renameList
    : renameItem (COMMA renameItem)*
    ;

renameItem
    : identifier EQUALS identifier
    ;

identifierList: identifier (COMMA identifier)*;

// Add missing assignment definition
assignment
    : identifier EQUALS expression
    ;

inputStatement
    : INPUT datasetFields SEMICOLON
    ;

datasetFields
    : datasetField (datasetField)*
    ;

datasetField
    : identifier format?
    | AT COLUMN identifier format?
    | pointer identifier format?
    ;

pointer
    : AT NUMBER
    | PLUS NUMBER
    ;

infileStatement
    : INFILE (STRING | identifier) infileOptions* SEMICOLON
    ;

infileOptions
    : option (COMMA? option)*
    | TRUNCOVER
    | FIRSTOBS EQUALS NUMBER
    | DLM EQUALS STRING
    | DSD
    | MISSOVER
    | (LINESIZE | LS) EQUALS NUMBER
    ;

// Add missing option statement
optionsStatement
    : (OPTIONS optionsList
    | OPTION identifier EQUALS expression) SEMICOLON
    ;

optionsList
    : optionsItem (COMMA? optionsItem)*
    ;

optionsItem
    : MISSING EQUALS STRING
    | NOCENTER
    | identifier (EQUALS expression)?
     ;

// Add comment type definitions
comment
    : blockComment
    | lineComment
    ;

blockComment: COMMENT;
lineComment: LINE_COMMENT;

// Add macro function definition
macroFunctionCall
    : M_FUNCTION_ID (LPAREN functionArgList RPAREN)? SEMICOLON?
    ;

incStatement
    : (INC | INCLUDE) (STRING | WORD) SEMICOLON
    ;

condition
    : LPAREN condition RPAREN
    | NOT condition
    | condition (AND | OR) condition
    | expression comparison expression
    | macroVariable comparison (literal | identifier | macroVariable)
    | functionExpression comparison (literal | identifier | macroVariable | functionExpression)
    | identifier IN LPAREN (literal | identifier | macroVariable) (COMMA (literal | identifier | macroVariable))* RPAREN
    | identifier CONTAINS STRING
    | identifier comparison (literal | identifier | macroVariable | functionExpression)
    ;

comparison
    : EQUALS                    // = or EQ
    | NE                        // <> or NE
    | LT                        // < or LT
    | LE                       // <= or LE
    | GT                       // > or GT
    | GE                      // >= or GE
    | IN                      // IN
    | CONTAINS               // CONTAINS
    | LIKE                   // LIKE
    | BETWEEN               // BETWEEN
    | IS                    // IS NULL, IS MISSING
    ;