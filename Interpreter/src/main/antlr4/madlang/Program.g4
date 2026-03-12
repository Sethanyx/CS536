grammar Program;

/* --- Parser Rules --- */

program : decl* EOF;

baseType : 'int' | 'bool';
type : baseType | baseType '->' baseType;

decl : globalVarDecl | funDecl;
globalVarDecl : IDENT ':' type ( '=' expr )? ';';
funDecl : 'fn' IDENT '(' params? ')' ':' type block;
params : param ( ',' param )*;
param : IDENT ':' type;

stmt : block
     | varDef
     | funDef
     | assignStmt
     | ifStmt
     | whileStmt
     | returnStmt
     | exprStmt;

block : '{' stmt* '}';
varDef : IDENT ':' type ( '=' expr )? ';';
funDef : funDecl;
assignStmt : IDENT '=' expr ';';
ifStmt : 'if' '(' expr ')' stmt ( 'else' stmt )?;
whileStmt : 'while' '(' expr ')' block;
returnStmt : 'return' expr ';';
exprStmt : expr ';';

expr : expr ( '*' | '/' | '%' ) expr         # MultDiv
     | expr ( '+' | '-' ) expr               # AddSub
     | expr ( '>' | '<' | '>=' | '<=' ) expr # Relation
     | expr ( '==' | '!=' ) expr             # Equality
     | expr '&&' expr                        # And
     | expr '||' expr                        # Or
     | ( '-' | '!' ) expr                    # Unary
     | primary                               # PrimaryExpr
     ;

primary : INT_LIT               # IntLit
        | 'true'                # TrueLit
        | 'false'               # FalseLit
        | IDENT '(' args? ')'   # FunCall
        | IDENT                 # Ident
        | '(' expr ')'          # Parens
        ;

args : expr ( ',' expr )*;

/* --- Lexer Rules --- */

INT_LIT : '0' | [1-9][0-9]*;
IDENT : [A-Za-z_][A-Za-z0-9_]*;

LINE_COMMENT : '//' ~[\r\n]* -> skip ;
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;

WS : [ \t\r\n]+ -> skip;