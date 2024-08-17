package dev.algo.parsetest.benchmark;

import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprLexer;
import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprParser;
import dev.algo.parsetest.common.Benchmark;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Path;

public class AntlrArithmeticExprBenchmark extends Benchmark<ParseTree> {

    public AntlrArithmeticExprBenchmark(Path folderPath) throws IOException {
        super(folderPath, ".ae");
    }

    @Override
    public ParseTree parseInput(String input) {
        ArithmeticExprLexer lexer = new ArithmeticExprLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArithmeticExprParser parser = new ArithmeticExprParser(tokens);
        return parser.expr();
    }
}