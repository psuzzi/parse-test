package dev.algo.parsetest.benchmark.util

import dev.algo.parsetest.antlrv4.ArithmeticExprGrammarFuzzer
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.logging.Logger
import kotlin.io.path.*


private const val SAMPLE_PREFIX = "Sample"
private const val N_SAMPLES = 30
private const val EXT_AE = "ae"

/**
 * Utility class responsible for generating sample files to parse-test-common
 */
object GenerateTestCases {

    private val logger = Logger.getLogger(this::class.java.name)

    const val ARITHMETIC_EXPR_GEN: String = "arithmetic_expr_gen"
    private val PATH_AE_GEN: Path = Path.of("parse-test-common/src/test/resources/", ARITHMETIC_EXPR_GEN)

    /**
     * Triggers the generation of the sample files.
     * @param args unused
     */
    @JvmStatic
    fun main(args: Array<String>) {
        generateArithmeticExprSamples(PATH_AE_GEN)
    }

    /**
     * Regenerate a set of samples for the ArithmeticExpr grammar
     *
     * @param folderPath where to generate the samples
     */
    private fun generateArithmeticExprSamples(folderPath: Path) {
        checkFolderAndCleanup(folderPath, EXT_AE)
        generateArithmeticExprSamples(folderPath, SAMPLE_PREFIX, "small", N_SAMPLES)
        generateArithmeticExprSamples(folderPath, SAMPLE_PREFIX, "medium", N_SAMPLES)
        generateArithmeticExprSamples(folderPath, SAMPLE_PREFIX, "large", N_SAMPLES)
    }

    /**
     * Create the directory specified by the folderPath. If the folder exists, remove all the files with given extension.
     *
     * @param folderPath path to the folder
     * @param extension extension of files to cleanup
     */
    private fun checkFolderAndCleanup(folderPath: Path, extension: String) {
        logger.info("Check and cleanup folder $folderPath.")
        var deleted = 0
        try {
            Files.createDirectories(folderPath)
            Files.walk(folderPath)
                .filter{ it.isRegularFile() && it.extension == extension }
                .forEach{ file ->
                    try {
                        file.deleteExisting()
                        deleted ++
                    } catch (e: IOException) {
                        throw UncheckedIOException("Could not delete file $file", e)
                    }
                }
        } catch (e: IOException) {
            throw UncheckedIOException("Failed to setup or clean folder $folderPath", e)
        }
        if (deleted > 0) {
            logger.info("Cleanup: deleted $deleted *.$extension files.")
        }
    }

    /**
     * Generate a set of nSamples sample files with specified prefix and complexity in the given folder
     */
    private fun generateArithmeticExprSamples(
        folderPath: Path,
        prefix: String,
        complexity: String,
        nSamples: Int
    ) {
        val fuzzer = ArithmeticExprGrammarFuzzer()
        var nGenerated = 0
        (1..nSamples).forEach { i ->
            val input = fuzzer.generateInput(complexity)
            // e.g. "Sample_small_01.ae"
            val filename = "${prefix}_${complexity}_%0${nSamples.toString().length}d.ae".format(i)
            val filePath = folderPath.resolve(filename)
            try {
                filePath.writeText(input)
                nGenerated++
            } catch (e: IOException) {
                throw UncheckedIOException("Failed to generate sample file $filePath", e)
            }
        }
        if (nGenerated > 0) {
            logger.info("Generated $nGenerated sample files named ${prefix}_${complexity}_*.ae ")
        }
    }
}
