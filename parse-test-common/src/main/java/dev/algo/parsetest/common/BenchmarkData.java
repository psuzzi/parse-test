package dev.algo.parsetest.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
     * Initialize the {@link BenchmarkData} with a string representing the base folder, and an extension to identify
     * the files to parse
     * @param folderPathString representing the containing folder
     * @param fileContents map file-names:file-contents representing the files
     */
    public BenchmarkData(String folderPathString, Map<String, String> fileContents){
        this.folderPath = Path.of(folderPathString); // not defined for a set of files
        this.benchmarks = fileContents.entrySet().stream()
                .map(entry -> new SingleParseBenchmark<T>(folderPathString, entry.getKey(), entry.getValue()))
                .toList();
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
                benchmarkNode.put("file_name", benchmark.getFileName());
                benchmarkNode.put("parse_time_ms", benchmark.getParseDuration());
                benchmarkArray.add(benchmarkNode);
            }
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }
}
