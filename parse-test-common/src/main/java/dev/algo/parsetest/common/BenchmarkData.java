package dev.algo.parsetest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic class representing BenchmarkData for a parser producing the generic type T as AST
 * @param <T>
 */
public class BenchmarkData <T>{

    private final Path folderPath;
    private final List<SingleParseBenchmark<T>> benchmarks;

    private long totalParsingTime = 0 ;
    private long totalMemoryUsed = 0;

    /**
     * Initialize the {@link BenchmarkData} with path pointing to a folder and an extension to identify the files to parse
     * @param folderPath {@link Path} to the containing folder
     * @param extension String representing the extension
     * @throws IOException
     */
    public BenchmarkData(Path folderPath, String extension) throws IOException {
        this.folderPath = folderPath;
        this.benchmarks = Files.walk(this.folderPath)
                .filter(path -> path.toString().endsWith(extension))
                .map(path -> {
                    try {
                        return new SingleParseBenchmark<T>(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading file: " + path, e);
                    }
                })
                .collect(Collectors.toList());
    }

    public Path getFolderPath() {
        return folderPath;
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
     * Return a JSON representation of the benchmark results, excluding inputs and ASTs
     * @return
     * @throws JsonProcessingException
     */
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        rootNode.put("folder_path", folderPath.toString());
        rootNode.put("number_of_files", getNumberOfFiles());
        rootNode.put("total_parsing_time_ms", getTotalParsingTime());
        rootNode.put("total_memory_used_kb", getTotalMemoryUsed());

        ArrayNode benchmarkArray = rootNode.putArray("benchmarks");
        for (SingleParseBenchmark<T> benchmark : benchmarks) {
            ObjectNode benchmarkNode = mapper.createObjectNode();
            benchmarkNode.put("file_name", benchmark.getFilePath().getFileName().toString());
            benchmarkNode.put("parse_time_ms", benchmark.getParseDuration());
            benchmarkArray.add(benchmarkNode);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
