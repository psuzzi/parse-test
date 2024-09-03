package dev.algo.parsetest.xtext.benchmark;

import com.google.inject.Injector;
import dev.algo.parsetest.common.Benchmark;
import dev.algo.parsetest.common.BenchmarkData;
import dev.algo.parsetest.common.ParserBenchmarkTest;
import dev.algo.parsetest.xtext.ArithmeticExprStandaloneSetup;
import dev.algo.parsetest.xtext.arithmeticExpr.Model;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Disabled;
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

public class ArithmeticExprXtextParserBenchmarkTest extends ParserBenchmarkTest {

    private static final Logger logger = Logger.getLogger(ArithmeticExprXtextParserBenchmarkTest.class.getName());
    private static final String BR = System.lineSeparator();

    /**
     * Benchmark for the Xtext Parser for ArithmeticExpr
     */
    static class ArithmeticExprXtextParserBenchmark extends Benchmark<Model> {

        ParseHelper<Model> parseHelper;

        public ArithmeticExprXtextParserBenchmark(){
            ArithmeticExprStandaloneSetup setup = new ArithmeticExprStandaloneSetup();
            Injector injector = setup.createInjectorAndDoEMFRegistration();
            parseHelper = injector.<ParseHelper>getInstance(ParseHelper.class);
        }

        @Override
        public Model parseInput(String input) {
            Model model;
            try {
                model = parseHelper.parse(input);
            } catch (Exception e) {
                throw new RuntimeException("Error parsing Xtext model", e);
            }
            return model;
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
    void parserSmokeArithmeticExprXtextParserBenchmarkTest() throws IOException {
        prepareTempDir();
        // get inputs from temp directory
        ArithmeticExprXtextParserBenchmark benchmark = new ArithmeticExprXtextParserBenchmark();
        benchmark.loadDataFromPath(tempDir, ".ae");
        BenchmarkData<Model> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(5, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("XTEXT SMOKE TEST " + BR + data.toJson());
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * PLEASE NOTE: I often observed Xtext raising a StackOverflowError
     */
//    @Test
    @Disabled("Xtext raising StackOverflowError")
    void parserFullArithmeticExprXtextParserBenchmarkTest() throws IOException {
        // get inputs from resource folder
        ArithmeticExprXtextParserBenchmark benchmark = new ArithmeticExprXtextParserBenchmark();
        benchmark.loadDataFromResource(RESOURCE_ARITHMETIC_EXPR_GEN, ".ae");
        BenchmarkData<Model> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");
        assertEquals(90, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("XTEXT FULL TEST " + BR + data.toJson());
    }


}
