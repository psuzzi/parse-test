package dev.algo.parsetest.kolasuv15.arithmetic_expr.benchmark

import dev.algo.parsetest.common.ParserBenchmarkTest
import dev.algo.parsetest.kolasuv15.benchmark.ArithmeticExprKolasuParserBenchmark
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.util.logging.Logger
import kotlin.io.path.writeText

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArithmeticExprKolasuParserBenchmarkTest : ParserBenchmarkTest() {

    companion object {
        private val logger: Logger = Logger.getLogger(ArithmeticExprKolasuParserBenchmarkTest::class.java.name)
        private val BR: String = System.lineSeparator()
    }

    @TempDir
    lateinit var tempDir: Path

    private fun prepareTempDir(){
        val testCases = mapOf(
            "test1.ae" to "2 + 3 * 4",
            "test2.ae" to "5 - 1 / 2",
        )
        loadResourceFiles(testCases, RESOURCE_ARITHMETIC_EXPR, ".ae")
        // Write inputs to temp dir for smoke test
        testCases.forEach { (filename, content) ->
            tempDir.resolve(filename).writeText(content)
        }
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     */
    @Test
    fun testSmokeBenchmarkArithmeticExprInTempFolder() {
        prepareTempDir()
        // get inputs from temp directory
        val benchmark = ArithmeticExprKolasuParserBenchmark()
        benchmark.loadDataFromPath(tempDir, ".ae")
        val data = benchmark.executeBenchmark()
        assertNotNull(data, "Benchmark data should not be null")
        assertEquals(5, data.numberOfFiles, "Expected number of parsed files")
        logger.info("KOLASU 1.5 SMOKE TEST$BR${data.toJson(true)}")
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     *
     */
    @Test
    fun testFullBenchmarkArithmeticExprInProvidedFolder() {
        // get input from resource folder (common jar)
        val benchmark = ArithmeticExprKolasuParserBenchmark()
        benchmark.loadDataFromResource(RESOURCE_ARITHMETIC_EXPR_GEN, ".ae")
        val data = benchmark.executeBenchmark()
        assertNotNull(data) { "Benchmark data should not be null" }
        assertEquals(90, data.numberOfFiles) { "Expected number of parsed files" }
        logger.info("KOLASU 1.5 FULL TEST $BR ${data.toJson()}")
    }

}