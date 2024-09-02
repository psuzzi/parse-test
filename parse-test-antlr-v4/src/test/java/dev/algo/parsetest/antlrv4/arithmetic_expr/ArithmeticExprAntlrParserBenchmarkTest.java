package dev.algo.parsetest.antlrv4.arithmetic_expr;

import dev.algo.parsetest.antlrv4.benchmark.ArithmeticExprAntlrParserBenchmark;
import dev.algo.parsetest.common.BenchmarkData;
import dev.algo.parsetest.common.ParserBenchmarkTest;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArithmeticExprAntlrParserBenchmarkTest extends ParserBenchmarkTest {

    private static final Logger logger = Logger.getLogger(ArithmeticExprAntlrParserBenchmarkTest.class.getName());
    private static final String BR = System.lineSeparator();

    @TempDir
    Path tempDir;

    void prepareTempDir() throws IOException {
        // This is how to setup temp directory for test
        Map<String, String> testCases = new HashMap<>();
        testCases.put("test1.ae", "2 + 3 * 4");
        testCases.put("test2.ae", "5 - 1 / 2");
        loadResourceFiles(testCases, RESOURCE_ARITHMETIC_EXPR, ".ae");
        // Write inputs to temp dir for smoke test
        for (Map.Entry<String, String> entry : testCases.entrySet()) {
            Files.writeString(tempDir.resolve(entry.getKey()), entry.getValue());
        }
    }

    @Test
    void parserSmokeTest() throws IOException {
        prepareTempDir();
        // get inputs from temp directory
        ArithmeticExprAntlrParserBenchmark benchmark = new ArithmeticExprAntlrParserBenchmark();
        benchmark.loadDataFromPath(tempDir, ".ae");
        BenchmarkData<ParseTree> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(5, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("ANTLR 4 SMOKE TEST " + BR + data.toJson());
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also:
     */
    @Test
    void testFullBenchmarkArithmeticExprInProvidedFolder() throws IOException {
        // get inputs from resource folder
        ArithmeticExprAntlrParserBenchmark benchmark = new ArithmeticExprAntlrParserBenchmark();
        benchmark.loadDataFromResource(RESOURCE_ARITHMETIC_EXPR_GEN, ".ae");
        BenchmarkData<ParseTree> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(90, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("ANTLR 4 FULL TEST " + BR + data.toJson());
    }

}
