package dev.algo.parsetest.benchmark.utils;

import dev.algo.parsetest.antlrv4.arithmetic_expr.ArithmeticExprGrammarFuzzer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Utility class responsible of generating sample files to test
 */
public final class GenerateTestCases {

    public static final String ARITHMETIC_EXPR_GEN = "arithmetic_expr_gen";
    public static final Path PATH_AE_GEN = Path.of("parse-test-benchmark/src/test/resources/", ARITHMETIC_EXPR_GEN);

    private GenerateTestCases(){
        // Prevent instantiation of utility class
    }

    /**
     * Triggers the generation of the sample files.
     * @param args unused
     */
    public static void main(String[] args)  {
        GenerateTestCases.generateArithmeticExprSamples(PATH_AE_GEN);
    }

    /**
     * Regenerate a set of samples for the ArithmeticExpr grammar
     *
     * @param folderPath where to generate the samples
     */
    public static void generateArithmeticExprSamples(Path folderPath) {
        checkFolderAndCleanup(folderPath, ".ae");
        generateArithmeticExprSamples(folderPath, "Sample", "small", 30);
        generateArithmeticExprSamples(folderPath, "Sample", "medium", 30);
        generateArithmeticExprSamples(folderPath, "Sample", "large", 30);
    }

    /**
     * Create the directory specified by the folderPath. If the folder exists, remove all the files with given extension.
     *
     * @param folderPath path to the folder
     * @param extension extension of files to cleanup
     */
    private static void checkFolderAndCleanup(Path folderPath, String extension){
        try {
            Files.createDirectories(folderPath);
            try (Stream<Path> files = Files.walk(folderPath)) {
                files.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(extension)).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException("Could not delete file " + path, e);
                    }
                });
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to setup or clean folder " + folderPath, e);
        }
    }

    private static void generateArithmeticExprSamples(Path folderPath, String prefix, String complexity, int nSamples) {
        int padding = String.valueOf(nSamples).length();
        ArithmeticExprGrammarFuzzer fuzzer = new ArithmeticExprGrammarFuzzer();

        for (int i = 0; i < nSamples; i++) {
            String input = fuzzer.generateInput(complexity);
            String formattedNumber = String.format("%0" + padding + "d", i + 1);
            String filename = String.format("%s_%s_%s.ae", prefix, complexity, formattedNumber );
            Path filePath = folderPath.resolve(filename);
            try {
                Files.writeString(filePath,input);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to generate sample file " + filePath, e);
            }
        }
    }


}
