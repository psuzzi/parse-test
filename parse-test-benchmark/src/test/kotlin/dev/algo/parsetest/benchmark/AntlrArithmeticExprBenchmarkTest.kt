package dev.algo.parsetest.benchmark

import dev.algo.parsetest.antlrv4.ArithmeticExprGrammarFuzzer
import dev.algo.parsetest.benchmark.util.GenerateTestCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.io.TempDir
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.logging.Logger
import kotlin.io.path.writeText
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AntlrArithmeticExprBenchmarkTest {

    companion object {
        private val logger: Logger = Logger.getLogger(AntlrArithmeticExprBenchmarkTest::class.java.name)
        private val BR: String = System.lineSeparator()
    }

    @TempDir
    lateinit var tempDir:Path

    private lateinit var fuzzer: ArithmeticExprGrammarFuzzer

    @BeforeEach
    @Throws(IOException::class)
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
     * Fuzzer Smoke test, verifies we can run the tests against generated inputs
     * @throws IOException can be triggered by test
     */
    @Test
    @Throws(IOException::class)
    fun testSmokeBenchmarkArithmeticExprGeneratedInputs() {
        val inputSizes = listOf("small", "medium", "large")
        inputSizes.forEach { size ->
            val input = Files.readString(tempDir.resolve("$size.ae"))
            logger.fine("${size.uppercase()}: $input")
            assertTrue(fuzzer.isValidInput(input), "$size input should be valid")
        }
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     * @throws IOException can be triggered by test
     */
    @Test
    @Throws(IOException::class)
    fun testSmokeBenchmarkArithmeticExprInTempFolder() {
        val benchmark = AntlrArithmeticExprBenchmark(tempDir)
        val data = benchmark.executeBenchmark()
        assertNotNull(data, "Benchmark data should not be null")
        assertEquals(5, data.numberOfFiles, "Expected number of parsed files")
        logger.info("ANTLR 4 SMOKE TEST $BR ${data.toJson(true)}")
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also: [GenerateTestCases]
     */
    @Test
    fun testFullBenchmarkArithmeticExprInProvidedFolder() {
        val benchmarkFolderName = GenerateTestCases.ARITHMETIC_EXPR_GEN
        val benchmarkFolderPath = javaClass.classLoader.getResource(benchmarkFolderName)?.toURI()?.let { Paths.get(it) }
            ?: throw IllegalStateException("Cannot find benchmark folder: $benchmarkFolderName")

        val benchmark = AntlrArithmeticExprBenchmark(benchmarkFolderPath)
        val data = benchmark.executeBenchmark()

        assertNotNull(data, "Benchmark data should not be null")
        assertEquals(90, data.numberOfFiles, "Expected number of parsed files")

        logger.info("ANTLR 4 FULL TEST $BR ${data.toJson()}")
    }

}