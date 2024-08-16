lexer grammar ArithmeticExprLexer;

PLUS  : '+';
MINUS : '-';
MULT  : '*';
DIV   : '/';
LPAREN: '(';
RPAREN: ')';

NUMBER: [0-9]+ ('.' [0-9]+)?;

WS: [ \r\n\t]+ -> channel(HIDDEN);