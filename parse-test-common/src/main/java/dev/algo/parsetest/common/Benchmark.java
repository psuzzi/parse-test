package dev.algo.parsetest.common;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Class representing a generic abstract benchmark for a parser that builds a generic T AST.
 * The extending concrete classes should know how to initialize and execute the parsing
 *
 * @param <T>
 */
public abstract class Benchmark<T> {

    protected BenchmarkData<T> benchmarkData;

    public Benchmark(Path folderPath, String extension) throws IOException {
        benchmarkData = new BenchmarkData<>(folderPath, extension);
    }

    public BenchmarkData<T> executeBenchmark() throws IOException {
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
     * The specialized concrete class shuold know how to parse the input and produce an AST of type T
     * @param input as String
     * @return AST as T
     */
    public abstract T parseInput(String input);

}

