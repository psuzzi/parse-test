package dev.algo.parsetest.kolasuv15.benchmark

import com.strumenta.kolasu.model.Node
import dev.algo.parsetest.common.Benchmark
import dev.algo.parsetest.kolasuv15.ArithmeticExprKolasuParser

class ArithmeticExprKolasuParserBenchmark() :
    Benchmark<Node?>() {
    override fun parseInput(input: String): Node {
        // Instantiate and parse
        return ArithmeticExprKolasuParser().parse(input)
    }
}