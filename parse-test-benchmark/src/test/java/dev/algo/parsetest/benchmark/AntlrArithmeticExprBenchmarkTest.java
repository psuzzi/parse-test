package dev.algo.parsetest.benchmark;

import dev.algo.parsetest.antlrv4.arithmetic_expr.ArithmeticExprGrammarFuzzer;
import dev.algo.parsetest.benchmark.utils.GenerateTestCases;
import dev.algo.parsetest.common.BenchmarkData;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class AntlrArithmeticExprBenchmarkTest {

    private static final Logger logger = Logger.getLogger(AntlrArithmeticExprBenchmarkTest.class.getName());

    private static final String BR = System.lineSeparator();

    @TempDir
    Path tempDir;

    private ArithmeticExprGrammarFuzzer aeFuzzer;

    @BeforeEach
    void setup() throws IOException {
        logger.info("Initialize Test");
        aeFuzzer = new ArithmeticExprGrammarFuzzer();

        Files.writeString(tempDir.resolve("test1.ae"), "2 + 3 * 4");
        Files.writeString(tempDir.resolve("test2.ae"), "5 - 1 / 2");
        Files.writeString(tempDir.resolve("small.ae"), aeFuzzer.generateInput("small"));
        Files.writeString(tempDir.resolve("medium.ae"), aeFuzzer.generateInput("medium"));
        Files.writeString(tempDir.resolve("large.ae"), aeFuzzer.generateInput("large"));
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     * @throws IOException can be triggered by test
     */
    @Test
    void testSmokeBenchmarkArithmeticExprInTempFolder() throws IOException {
        AntlrArithmeticExprBenchmark antlrArithmeticExprBenchmark = new AntlrArithmeticExprBenchmark(Path.of(tempDir.toString()));

        BenchmarkData<ParseTree> data = antlrArithmeticExprBenchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");

        assertEquals(5, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("SMOKE TEST" + BR + data.toJson(true));
    }

    /**
     * Smoke test, verifies we can run the tests against generated inputs
     * @throws IOException can be triggered by test
     */
    @Test
    void testSmokeBenchmarkArithmeticExprGeneratedInputs() throws IOException {
        String small = Files.readString(tempDir.resolve("small.ae"));
        String medium = Files.readString(tempDir.resolve("medium.ae"));
        String large = Files.readString(tempDir.resolve("large.ae"));

        logger.fine("SMALL: " + small);
        logger.fine("MEDIUM: " + medium);
        logger.fine("LARGE: " + large);

        assertTrue(aeFuzzer.isValidInput(small), "Small input should be valid");
        assertTrue(aeFuzzer.isValidInput(medium), "Medium input should be valid");
        assertTrue(aeFuzzer.isValidInput(large), "Large input should be valid");
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also: {@link dev.algo.parsetest.benchmark.utils.GenerateTestCases}
     */
    @Test
    void testFullBenchmarkArithmeticExprInProvidedFolder() throws IOException, URISyntaxException {
        Path folderPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(GenerateTestCases.ARITHMETIC_EXPR_GEN)).toURI());
        AntlrArithmeticExprBenchmark antlrArithmeticExprBenchmark = new AntlrArithmeticExprBenchmark(folderPath);
        BenchmarkData<ParseTree> data = antlrArithmeticExprBenchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");

        assertEquals(90, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("FULL TEST" + BR + data.toJson());

    }
}