package dev.algo.parsetest.antlrv4.benchmark;

import dev.algo.parsetest.antlrv4.ArithmeticExprAntlrParser;
import dev.algo.parsetest.common.Benchmark;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Benchmark for the Antlr Parser for ArithmeticExpr
 */
public class ArithmeticExprAntlrParserBenchmark extends Benchmark<ParseTree> {

    @Override
    public ParseTree parseInput(String input) {
        return new ArithmeticExprAntlrParser().parse(input);
    }
}
