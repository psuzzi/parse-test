package dev.algo.parsetest.common;

/**
 * Class representing a single parse benchmark for a parser producing the generic type T as AST
 * @param <T>
 */
public class SingleParseBenchmark<T> {
    private final String basePath;
    private final String fileName;
    private final String input;
    private T parseResult;
    private long parseDuration;


    /**
     * Initialize the {@link SingleParseBenchmark} with a base path, fileName and input
     * @param basePath path to the base folder as string. Used for diagnostics, can be null
     * @param fileName file name as string. Must be non null
     * @param input file content
     */
    public SingleParseBenchmark(String basePath, String fileName, String input) {
        this.basePath = basePath;
        this.fileName = fileName;
        this.input = input;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Input to be parsed as string
     * @return benchmark input
     */
    public String getInput() {
        return input;
    }

    /**
     * Parse result to be retrieved later
     * @return parse result object (AST)
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
     * @return parse duration in millis
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
