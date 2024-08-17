package dev.algo.parsetest.benchmark

import dev.algo.parsetest.antlrv4.ArithmeticExprAntlrParser
import dev.algo.parsetest.common.Benchmark
import org.antlr.v4.runtime.tree.ParseTree
import java.nio.file.Path

/**
 * Benchmark for the Antlr Parser for ArithmeticExpr
 */
class AntlrArithmeticExprBenchmark(folderPath: Path?) :
    Benchmark<ParseTree?>(folderPath, ".ae") {
    override fun parseInput(input: String): ParseTree {
        // Instantiate and parse
        return ArithmeticExprAntlrParser().parse(input)
    }
}