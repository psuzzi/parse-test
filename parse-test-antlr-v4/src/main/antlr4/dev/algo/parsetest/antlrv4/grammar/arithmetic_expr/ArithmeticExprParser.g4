parser grammar ArithmeticExprParser;

options {
    tokenVocab=ArithmeticExprLexer;
}

expr
    : term ((PLUS | MINUS) term)*
    ;

term
    : factor ((MULT | DIV) factor)*
    ;

factor
    : NUMBER
    | LPAREN expr RPAREN
    ;