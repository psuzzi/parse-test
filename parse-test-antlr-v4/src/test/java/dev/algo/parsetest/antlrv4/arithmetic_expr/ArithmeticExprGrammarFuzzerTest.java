package dev.algo.parsetest.antlrv4.arithmetic_expr;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the functionality of the fuzzer for the ArithmeticExpr grammar
 */
class ArithmeticExprGrammarFuzzerTest {

    private static final Logger logger = Logger.getLogger(ArithmeticExprGrammarFuzzerTest.class.getName());

    private ArithmeticExprGrammarFuzzer fuzzer;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        fuzzer = new ArithmeticExprGrammarFuzzer();
    }

    @Test
    void testFuzzer(){
        logger.info("ArithmeticExprGrammarFuzzer test started");

        String small = fuzzer.generateInput("small");
        String medium = fuzzer.generateInput("medium");
        String large = fuzzer.generateInput("large");

        assertNotNull(small, "Small input is null");
        assertNotNull(medium, "Medium input is null");
        assertNotNull(large, "Large input is null");

        // set to info to see sample output on console
        logger.info("SMALL: " + small);
        logger.info("MEDIUM: " + medium);
        logger.info("LARGE: " + large);

        assertTrue(fuzzer.isValidInput(small), "Small input should be valid");
        assertTrue(fuzzer.isValidInput(medium), "Medium input should be valid");
        assertTrue(fuzzer.isValidInput(large), "Large input should be valid");
    }

}