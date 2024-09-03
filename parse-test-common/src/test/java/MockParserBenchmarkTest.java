import dev.algo.parsetest.common.Benchmark;
import dev.algo.parsetest.common.BenchmarkData;
import dev.algo.parsetest.common.ParserBenchmarkTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test of the common benchmark infrastructure.
 * This test is a quick example on how to setup a new parser tests,
 *
 */
public class MockParserBenchmarkTest extends ParserBenchmarkTest {

    private static final Logger logger = Logger.getLogger(MockParserBenchmarkTest.class.getName());
    private static final String BR = System.lineSeparator();

    /**
     * Example of a Mock Benchmark, which return an Object as AST
     */
    static class MockParserBenchmark extends Benchmark<Object> {

        @Override
        public Object parseInput(String input) {
            // Here we do the parsing, returning the AST
            return "AST";
        }

    }

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
        Benchmark<Object> benchmark = new MockParserBenchmark();
        benchmark.loadDataFromPath(tempDir, ".ae");
        BenchmarkData<Object> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(5, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("MOCK SMOKE TEST " + BR + data.toJson());
    }

    @Test
    void parserFullTest() throws IOException {
        // get inputs from resource folder
        Benchmark<Object> benchmark = new MockParserBenchmark();
        benchmark.loadDataFromResource(RESOURCE_ARITHMETIC_EXPR_GEN, ".ae");
        BenchmarkData<Object> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(90, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("MOCK FULL TEST " + BR + data.toJson());
    }



}
