package dev.algo.parsetest.benchmark;

import dev.algo.parsetest.antlrv4.arithmetic_expr.ArithmeticExprGrammarFuzzer;
import dev.algo.parsetest.common.BenchmarkData;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class AntlrArithmeticExprBenchmarkTest {

    private static final Logger logger = Logger.getLogger(AntlrArithmeticExprBenchmarkTest.class.getName());

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

    @Test
    void testBenchmarkArithmeticExprInFolder() throws IOException {
        AntlrArithmeticExprBenchmark antlrArithmeticExprBenchmark = new AntlrArithmeticExprBenchmark(Path.of(tempDir.toString()));

        BenchmarkData<ParseTree> data = antlrArithmeticExprBenchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");

        assertEquals(5, data.getNumberOfFiles(), "Expected number of files parsed is 5");
        logger.info(data.toJson());
    }

    @Test
    void testBenchmarkArithmeticExprGeneratedInputs() throws IOException {
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
}