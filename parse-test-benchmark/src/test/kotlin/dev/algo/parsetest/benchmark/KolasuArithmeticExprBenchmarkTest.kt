package dev.algo.parsetest.benchmark

import dev.algo.parsetest.antlrv4.ArithmeticExprGrammarFuzzer
import dev.algo.parsetest.benchmark.util.GenerateTestCases
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.IOException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Logger
import kotlin.io.path.readText
import kotlin.io.path.writeText


class KolasuArithmeticExprBenchmarkTest {

    companion object{
        private val logger = Logger.getLogger(KolasuArithmeticExprBenchmarkTest::class.java.name)
        private val BR = System.lineSeparator()
    }

    @TempDir
    lateinit var tempDir:Path

    private lateinit var fuzzer: ArithmeticExprGrammarFuzzer

    @BeforeEach
    fun setup() {
        logger.info("Initialize Test")
        fuzzer = ArithmeticExprGrammarFuzzer()
        // Using Kotlin experimental extension functions path.writeText
        tempDir.resolve("test1.ae").writeText("2 + 3 * 4")
        tempDir.resolve("test2.ae").writeText("5 - 1 / 2")
        tempDir.resolve("small.ae").writeText(fuzzer.generateInput("small"))
        tempDir.resolve("medium.ae").writeText(fuzzer.generateInput("medium"))
        tempDir.resolve("large.ae").writeText(fuzzer.generateInput("large"))
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     */
    @Test
    @Throws(IOException::class)
    fun testSmokeBenchmarkArithmeticExprInTempFolder() {
        val benchmark = KolasuArithmeticExprBenchmark(tempDir)
        val data = benchmark.executeBenchmark()

        assertNotNull(data, "Benchmark data should not be null")

        assertEquals(5, data.numberOfFiles, "Expected number of parsed files")
        logger.info("SMOKE TEST$BR${data.toJson(true)}")
    }

    /**
     * Smoke test, verifies we can run the tests against generated inputs
     */
    @Test
    fun testSmokeBenchmarkArithmeticExprGeneratedInputs() {
        val small = tempDir.resolve("small.ae").readText()
        val medium = tempDir.resolve("medium.ae").readText()
        val large = tempDir.resolve("large.ae").readText()

        logger.fine("SMALL: $small")
        logger.fine("MEDIUM: $medium")
        logger.fine("LARGE: $large")

        assertTrue(fuzzer.isValidInput(small)) { "Small input should be valid" }
        assertTrue(fuzzer.isValidInput(medium)) { "Medium input should be valid" }
        assertTrue(fuzzer.isValidInput(large)) { "Large input should be valid" }
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also: [GenerateTestCases]
     */
    @Test
    fun testFullBenchmarkArithmeticExprInProvidedFolder() {
        val resourceUrl = javaClass.classLoader.getResource(GenerateTestCases.ARITHMETIC_EXPR_GEN)
        assertNotNull(resourceUrl, "Resource $resourceUrl should not be null")

        val folderPath = Paths.get(resourceUrl!!.toURI())
        val benchmark = KolasuArithmeticExprBenchmark(folderPath)
        val data = benchmark.executeBenchmark()

        assertNotNull(data) { "Benchmark data should not be null" }
        assertEquals(90, data.numberOfFiles) { "Expected number of parsed files" }

        logger.info("KOLASU FULL TEST$BR${data.toJson()}")
    }

}