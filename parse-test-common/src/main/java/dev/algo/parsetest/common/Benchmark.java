package dev.algo.parsetest.common;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Class representing a generic abstract benchmark for a parser that builds a generic AST.
 * The extending concrete classes should know how to initialize and execute the parsing
 *
 * @param <T>
 */
public abstract class Benchmark<T> {

    protected BenchmarkData<T> benchmarkData;

    protected Benchmark(){}

    /**
     * Load data from {@link Path}, use it when loading from directory
     */
    public void loadDataFromPath(Path folderPath, String extension) throws IOException {
        Map<String, String> fileContents = IOUtil.loadContentsFromPath(folderPath, extension);
        benchmarkData = new BenchmarkData<>(folderPath.toString(), fileContents);
    }

    /**
     * Load data from resource, use it when loading from Jars or classpath resource folders
     */
    public void loadDataFromResource(String resourceFolderName, String extension) throws IOException {
        Map<String, String> fileContents = IOUtil.loadContentsFromResource(getClass().getClassLoader(), resourceFolderName, extension);
        benchmarkData = new BenchmarkData<>(resourceFolderName, fileContents);
    }

    /**
     * Executes the benchmark
     * @return
     */
    public BenchmarkData<T> executeBenchmark() {
        Runtime runtime = Runtime.getRuntime();

        // Force garbage collection before starting the benchmark
        runtime.gc();

        long startMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        for( SingleParseBenchmark<T> spbm : benchmarkData.getBenchmarks() ){
            long parseStartTime = System.nanoTime();

            T tree = parseInput(spbm.getInput());

            long parseEndTime = System.nanoTime();
            long duration = (parseEndTime - parseStartTime) / 1_000_000; // to ms

            spbm.setParseResult(tree);
            spbm.setParseDuration(duration);
        }

        long endTime = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();

        long duration = (endTime - startTime) / 1_000_000;// to ms
        long memUsed = (endMem - startMem) / 1024;// to KB

        benchmarkData.setTotalParsingTime(duration);
        benchmarkData.setTotalMemoryUsed(memUsed);
        return benchmarkData;
    }

    /**
     * Parse the input and produce an instance of AST
     *
     * @param input as String
     * @return T instance of Abstract Syntax Tree
     */
    public abstract T parseInput(String input);

}

