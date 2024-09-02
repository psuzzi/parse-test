package dev.algo.parsetest.common;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Base class for Benchmark tests.
 * As the parse-test-common provides basic resources, this class helps with addressing them
 */
public class ParserBenchmarkTest {

    private static final Logger logger = Logger.getLogger(ParserBenchmarkTest.class.getName());

    /**
     * Resource folder providing three examples of arithmetic expression files
     */
    public static String RESOURCE_ARITHMETIC_EXPR = "arithmetic_expr";
    /**
     * Resource folder providing a full set of 90 examples of arithmetic expression files
     */
    public static String RESOURCE_ARITHMETIC_EXPR_GEN = "arithmetic_expr_gen";

    /**
     * Loads resources from a path and an extension. The results are stored in the map that is passed as parameter
     *
     * @param testCases map which will hold the test cases
     * @param resourceFolderName folder in the classpath holding the resources
     * @param extension to filter for the resource files
     * @throws IOException if there's an error reading the resources
     */
    protected void loadResourceFiles(Map<String, String> testCases, String resourceFolderName, String extension) throws IOException {
        testCases.putAll(IOUtil.loadContentsFromResource(getClass().getClassLoader(), resourceFolderName, extension));
    }
}
