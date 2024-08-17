package dev.algo.parsetest.benchmark

import com.strumenta.kolasu.model.Node
import dev.algo.parsetest.common.Benchmark
import dev.algo.parsetest.kolasuv15.ArithmeticExprKolasuParser
import java.nio.file.Path

/**
 * Benchmark for the Kolasu Parser for ArithmeticExpr
 */
class KolasuArithmeticExprBenchmark(folderPath: Path?) :
    Benchmark<Node?>(folderPath, ".ae") {
    override fun parseInput(input: String): Node {
        // Instantiate and parse
        return ArithmeticExprKolasuParser().parse(input)
    }
}