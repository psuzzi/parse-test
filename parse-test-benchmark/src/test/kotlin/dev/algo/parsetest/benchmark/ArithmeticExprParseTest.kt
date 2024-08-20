package dev.algo.parsetest.benchmark

import com.google.inject.Inject
import com.google.inject.Injector
import dev.algo.parsetest.xtext.ArithmeticExprStandaloneSetup
import dev.algo.parsetest.xtext.arithmeticExpr.Model
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith




@ExtendWith(InjectionExtension::class)
internal class ArithmeticExprParseTest {

    private lateinit var parseHelper: ParseHelper<Model>

    @Test
    @Throws(Exception::class)
    fun loadModel() {
        val setup = ArithmeticExprStandaloneSetup()
        val injector: Injector = setup.createInjectorAndDoEMFRegistration()
        val parseHelper = injector.getInstance(ParseHelper::class.java)
        val result = parseHelper.parse("(13 * 7) * (2 * 17)")
        Assertions.assertNotNull(result, "Unexpected null parse result")

        val errors = result.eResource().errors
        Assertions.assertTrue(errors.isEmpty(), "Unexpected errors: $errors")
    }
}
