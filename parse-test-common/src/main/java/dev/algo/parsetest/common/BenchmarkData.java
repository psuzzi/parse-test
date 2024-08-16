package dev.algo.parsetest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Generic class representing BenchmarkData for a parser producing the generic type T as AST
 * @param <T>
 */
public class BenchmarkData <T>{

    private static final Logger logger = Logger.getLogger(BenchmarkData.class.getName());

    private final Path folderPath;
    private final List<SingleParseBenchmark<T>> benchmarks;

    private long totalParsingTime = 0 ;
    private long totalMemoryUsed = 0;

    /**
     * Initialize the {@link BenchmarkData} with path pointing to a folder and an extension to identify the files to parse
     * @param folderPath {@link Path} to the containing folder
     * @param extension String representing the extension
     * @throws IOException can be triggered by walking the folder in search of the files
     */
    public BenchmarkData(Path folderPath, String extension) throws IOException {
        this.folderPath = folderPath;
        try (Stream<Path> pathStream = Files.walk(folderPath)) {
            this.benchmarks = pathStream
                    .filter(path -> path.toString().endsWith(extension))
                    .map(this::createSingleParseBenchmark)
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private SingleParseBenchmark<T> createSingleParseBenchmark(Path path)  {
        try {
            return new SingleParseBenchmark<>(path);
        } catch (IOException e) {
            logger.severe("Error reading file: " + path);
            return null;
        }
    }

    public List<SingleParseBenchmark<T>> getBenchmarks() {
        return benchmarks;
    }

    public int getNumberOfFiles() {
        return benchmarks.size();
    }

    public long getTotalParsingTime() {
        return totalParsingTime;
    }

    public void setTotalParsingTime(long totalParsingTime) {
        this.totalParsingTime = totalParsingTime;
    }

    public long getTotalMemoryUsed() {
        return totalMemoryUsed;
    }

    public void setTotalMemoryUsed(long totalMemoryUsed) {
        this.totalMemoryUsed = totalMemoryUsed;
    }

    @Override
    public String toString() {
        return "BenchmarkData{" +
                "folderPath='" + folderPath + '\'' +
                ", numberOfFiles=" + getNumberOfFiles() +
                ", totalParsingTime=" + totalParsingTime + " ms" +
                ", totalMemoryUsed=" + totalMemoryUsed + " KB" +
                '}';
    }

    /**
     * Return a simplified JSON representation of the benchmark results.
     * @return simplified JSON representation
     * @throws JsonProcessingException in case the JSON processing fails
     */
    public String toJson() throws JsonProcessingException {
        return toJson(false);
    }

    /**
     * Return a JSON representation of the benchmark results, excluding inputs and ASTs
     * @param detailed true to get a detailed representation
     * @return the JSON representation
     * @throws JsonProcessingException in case the JSON processing fails
     */
    public String toJson(boolean detailed) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("folder_path", folderPath.toString());
        rootNode.put("number_of_files", getNumberOfFiles());
        rootNode.put("total_parsing_time_ms", getTotalParsingTime());
        rootNode.put("total_memory_used_kb", getTotalMemoryUsed());

        if(detailed){
            ArrayNode benchmarkArray = rootNode.putArray("benchmarks");
            for (SingleParseBenchmark<T> benchmark : benchmarks) {
                ObjectNode benchmarkNode = mapper.createObjectNode();
                benchmarkNode.put("file_name", benchmark.getFilePath().getFileName().toString());
                benchmarkNode.put("parse_time_ms", benchmark.getParseDuration());
                benchmarkArray.add(benchmarkNode);
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
