package dev.algo.parsetest.antlrv4;

import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprLexer;
import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Antlr Parser, responsible to construct the AST from the input
 */
public class ArithmeticExprAntlrParser {

    /**
     * Parse by setting up the ANTLR Lexer and Parser, and construct the Antlr {@link ParseTree}
     *
     * @param input for the parser
     * @return resulting AST
     */
    public ParseTree parse(String input) {
        ArithmeticExprLexer lexer = new ArithmeticExprLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArithmeticExprParser parser = new ArithmeticExprParser(tokens);
        // Root expression as (ParseTree) AST
        return parser.expr();
    }
}
