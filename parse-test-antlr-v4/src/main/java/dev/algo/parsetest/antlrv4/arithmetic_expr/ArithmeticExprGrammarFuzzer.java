package dev.algo.parsetest.antlrv4.arithmetic_expr;

import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprLexer;
import dev.algo.parsetest.antlrv4.grammar.arithmetic_expr.ArithmeticExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Arrays;
import java.util.Random;

/**
 * Class responsible of generating and validating random inputs for the ArithmeticExpr grammar.
 */
public class ArithmeticExprGrammarFuzzer {

    public static final String SMALL = "small";
    public static final String MEDIUM = "medium";
    public static final String LARGE = "large";

    private static final String[] OPERATORS = {"+", "-", "*", "/"};
    private static final int MAX_NUMBER = 20;

    private final Random random = new Random();

    /**
     * Generate a random input with given complexity
     *
     * @param complexity: SMALL | MEDIUM | LARGE
     * @return String representing the input root (Expression)
     */
    public String generateInput(String complexity){
        return switch (complexity.toLowerCase()) {
            case SMALL -> generateExpression(randomBetween(3,8));
            case MEDIUM -> generateExpression(randomBetween(7,16));
            case LARGE -> generateExpression(randomBetween(17,30));
            default -> throw
                    new IllegalArgumentException(String.format("Unrecognized complexity: %s, it should be in %s",
                            complexity, Arrays.toString(new String[]{SMALL, MEDIUM, LARGE})));
        };
    }

    private String generateExpression(int depth){
        if (depth <= 1) {
            return generateNumber();
        } else {
            // randomly choose between binary expression or a parenthesized one
            if(random.nextBoolean()) {
                String leftOp = generateExpression(depth - 1);
                String rightOp = generateExpression(depth - 1);
                return leftOp + " " + generateOperator() + " " + rightOp;
            } else {
                String innerExpr = generateExpression(depth - 1);
                if( isBinaryOperation(innerExpr) && !isFullyParenthesized(innerExpr)) {
                    return "(" + innerExpr + ")";
                } else {
                    return innerExpr;
                }
            }
        }
    }

    private boolean isBinaryOperation(String expr){
        return expr.matches(".*[+\\-*/].*");
    }

    private boolean isFullyParenthesized(String expr){
        // starts with '(', ends with ')', and the inner part has balanced parentheses
        return expr.charAt(0) == '(' &&
                expr.charAt(expr.length()-1) == ')' &&
                countParentheses(expr.substring(1, expr.length()-1)) == 0;
    }

    private int countParentheses(String expr){
        // count parentheses balance, algorithm can be improved
        return expr.chars().map(ch -> ch == '(' ? 1 : ch == ')' ? -1 : 0).sum();
    }

    private int randomBetween(int min, int max){
        // fails when max > min
        if( max==min)
            return min;
        return random.nextInt(max - min)+min;
    }

    private String generateNumber(){
        return String.valueOf(random.nextInt(MAX_NUMBER));
    }

    private String generateOperator(){
        return OPERATORS[random.nextInt(OPERATORS.length)];
    }

    /**
     * Tell if the input is valid for the ArithmeticExpr grammar
     * @param input ArithmeticExpr input
     * @return true if valid
     */
    public boolean isValidInput(String input){
        try{
            ArithmeticExprLexer lexer = new ArithmeticExprLexer(CharStreams.fromString(input));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ArithmeticExprParser parser = new ArithmeticExprParser(tokens);
            // build the ParseTree
            parser.expr();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
