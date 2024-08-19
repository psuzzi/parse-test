package dev.algo.parsetest.benchmark;

import dev.algo.parsetest.antlrv4.ArithmeticExprGrammarFuzzer;
import dev.algo.parsetest.benchmark.util.GenerateTestCases;
import dev.algo.parsetest.common.BenchmarkData;
import dev.algo.parsetest.xtext.arithmeticExpr.Model;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class XtextArithmeticExprBenchmarkTest {

    private final static Logger logger = Logger.getLogger(XtextArithmeticExprBenchmarkTest.class.getName());

    private static final String BR = System.lineSeparator();

    @TempDir
    Path tempDir;

    private ArithmeticExprGrammarFuzzer fuzzer;

    @BeforeEach
    void setup() throws IOException {
        logger.info("Initialize Test");
        fuzzer = new ArithmeticExprGrammarFuzzer();

        Files.writeString(tempDir.resolve("test1.ae"), "2 + 3 * 4");
        Files.writeString(tempDir.resolve("test2.ae"), "5 - 1 / 2");
        Files.writeString(tempDir.resolve("small.ae"), fuzzer.generateInput("small"));
        Files.writeString(tempDir.resolve("medium.ae"), fuzzer.generateInput("medium"));
        Files.writeString(tempDir.resolve("large.ae"), fuzzer.generateInput("large"));
    }

    /**
     * Smoke test, check we can read files from a temporary directory
     * @throws IOException can be triggered by test
     */
    @Test
    void testSmokeBenchmarkArithmeticExprInTempFolder() throws IOException {
        XtextArithmeticExprBenchmark benchmark = new XtextArithmeticExprBenchmark(Path.of(tempDir.toString()));

        BenchmarkData<Model> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");

        assertEquals(5, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("XTEXT SMOKE TEST" + BR + data.toJson(true));
    }

    /**
     * The most complete test, against tens of generated inputs, stored in the project.
     * See also: {@link GenerateTestCases}
     */
    @Test
    void testFullBenchmarkArithmeticExprInProvidedFolder() throws IOException, URISyntaxException {
        Path folderPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(GenerateTestCases.ARITHMETIC_EXPR_GEN)).toURI());
        XtextArithmeticExprBenchmark benchmark = new XtextArithmeticExprBenchmark(folderPath);
        BenchmarkData<Model> data = benchmark.executeBenchmark();
        assertNotNull(data, "Benchmark data should not be null");

        assertEquals(90, data.getNumberOfFiles(), "Expected number of parsed files");
        logger.info("XTEXT FULL TEST" + BR + data.toJson());

    }

}
