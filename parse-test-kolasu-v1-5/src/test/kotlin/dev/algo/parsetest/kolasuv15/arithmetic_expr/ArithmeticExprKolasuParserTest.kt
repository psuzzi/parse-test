package dev.algo.parsetest.kolasuv15.arithmetic_expr

import dev.algo.parsetest.kolasuv15.ArithmeticExprKolasuParser
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.logging.Logger

/**
 * Simple test to verify the functionality of the Kolasu parser
 */
class ArithmeticExprKolasuParserTest {

    private val logger = Logger.getLogger(ArithmeticExprKolasuParserTest::class.toString())

    @Test
    fun testKolasuParser() {
        val parser = ArithmeticExprKolasuParser()
        val ast = parser.parse("(3+4)*2")
        assertNotNull(ast)
        logger.info(ast.toString())
    }
}