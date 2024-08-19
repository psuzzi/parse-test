package dev.algo.parsetest.benchmark

import dev.algo.parsetest.antlrv4.ArithmeticExprGrammarFuzzer
import dev.algo.parsetest.benchmark.util.GenerateTestCases
import dev.algo.parsetest.common.BenchmarkData
import dev.algo.parsetest.xtext.arithmeticExpr.Model
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Logger
import kotlin.io.path.writeText

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class XtextArithmeticExprBenchmarkTest {

    companion object {
        private val logger: Logger = Logger.getLogger(XtextArithmeticExprBenchmarkTest::class.java.name)
        private val BR: String = System.lineSeparator()
    }

    @TempDir
    lateinit var tempDir:Path

    private lateinit var fuzzer: ArithmeticExprGrammarFuzzer

    @BeforeEach
    fun setup() {
        logger.info("Initialize Test")
        fuzzer = ArithmeticExprGrammarFuzzer()

        val testCases = mapOf(
            "test1.ae" to "2 + 3 * 4",
            "test2.ae" to "5 - 1 / 2",
            "small.ae" to fuzzer.generateInput("small"),
            "medium.ae" to fuzzer.generateInput("medium"),
            "large.ae" to fuzzer.generateInput("large")
        )
        testCases.forEach { (filename, content) ->
            tempDir.resolve(filename).writeText(content)
        }
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     * @throws IOException can be triggered by test
     */
    @Test
    fun testSmokeBenchmarkArithmeticExprInTempFolder() {
        val benchmark = XtextArithmeticExprBenchmark(tempDir)
        val data: BenchmarkData<Model?> = benchmark.executeBenchmark()
        assertNotNull(data, "Benchmark data should not be null")
        assertEquals(5, data.numberOfFiles, "Expected number of parsed files")
        logger.info("XTEXT SMOKE TEST $BR ${data.toJson(true)}")
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also: [GenerateTestCases]
     */
    @Test
    @Throws(IOException::class, URISyntaxException::class)
    fun testFullBenchmarkArithmeticExprInProvidedFolder() {
        val benchmarkFolderName = GenerateTestCases.ARITHMETIC_EXPR_GEN
        val benchmarkFolderPath = javaClass.classLoader.getResource(benchmarkFolderName)?.toURI()?.let { Paths.get(it) }
            ?: throw IllegalStateException("Cannot find benchmark folder: $benchmarkFolderName")

        val benchmark = XtextArithmeticExprBenchmark(benchmarkFolderPath)
        val data: BenchmarkData<Model?> = benchmark.executeBenchmark()

        assertNotNull(data, "Benchmark data should not be null")
        assertEquals(90, data.numberOfFiles, "Expected number of parsed files")

        logger.info("XTEXT FULL TEST $BR ${data.toJson()}")
    }


}