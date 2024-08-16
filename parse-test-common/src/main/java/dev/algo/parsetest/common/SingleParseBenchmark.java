package dev.algo.parsetest.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class representing a single parse benchmark for a parser producing the generic type T as AST
 * @param <T>
 */
public class SingleParseBenchmark<T> {
    private final Path filePath;
    private final String input;
    private T parseResult;
    private long parseDuration;

    /**
     * Initialize the {@link SingleParseBenchmark} by providing a path to the file to be parsed.
     * The initialization consists of loading the file in memory as a string
     * @param filePath
     * @throws IOException
     */
    public SingleParseBenchmark(Path filePath) throws IOException {
        this.filePath = filePath;
        this.input = new String(Files.readAllBytes(filePath));
    }

    public Path getFilePath() {
        return filePath;
    }

    /**
     * Input to be parsed as string
     * @return
     */
    public String getInput() {
        return input;
    }

    /**
     * Parse result to be retrieved later
     * @return
     */
    public T getParseResult() {
        return parseResult;
    }

    /**
     * After parsing, we should store the AST with this setter
     * @param parseResult
     */
    public void setParseResult(T parseResult) {
        this.parseResult = parseResult;
    }

    /**
     * Parsing duration
     * @return
     */
    public long getParseDuration() {
        return parseDuration;
    }

    /**
     * After parsing, we should store the parsing duration
     * @param parseDuration
     */
    public void setParseDuration(long parseDuration) {
        this.parseDuration = parseDuration;
    }
}
