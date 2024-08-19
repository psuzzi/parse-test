package dev.algo.parsetest.benchmark

import com.google.inject.Injector
import dev.algo.parsetest.antlrv4.ArithmeticExprAntlrParser
import dev.algo.parsetest.common.Benchmark
import dev.algo.parsetest.xtext.ArithmeticExprStandaloneSetup
import java.nio.file.Path
import dev.algo.parsetest.xtext.arithmeticExpr.Model
import org.eclipse.xtext.testing.util.ParseHelper

/**
 * Benchmark for the Antlr Parser for ArithmeticExpr
 */
class XtextArithmeticExprBenchmark(folderPath: Path?) :
    Benchmark<Model?>(folderPath, ".ae") {

    private lateinit var parseHelper: ParseHelper<Model>

    init {
        // Perform additional initialization here
        val setup = ArithmeticExprStandaloneSetup()
        val injector: Injector = setup.createInjectorAndDoEMFRegistration()
        parseHelper = injector.getInstance(ParseHelper::class.java) as ParseHelper<Model>
    }

    override fun parseInput(input: String): Model {
        // Instantiate and parse
        return parseHelper.parse(input);
    }
}