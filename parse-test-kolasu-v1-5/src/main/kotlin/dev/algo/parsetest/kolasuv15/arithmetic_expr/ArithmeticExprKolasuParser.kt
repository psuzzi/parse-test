package dev.algo.parsetest.kolasuv15.arithmetic_expr

import com.strumenta.kolasu.model.Point
import com.strumenta.kolasu.model.Position
import dev.algo.parsetest.kolasuv15.grammar.arithmetic_expr.ArithmeticExprLexer
import dev.algo.parsetest.kolasuv15.grammar.arithmetic_expr.ArithmeticExprParser
import dev.algo.parsetest.kolasuv15.grammar.arithmetic_expr.ArithmeticExprParserBaseVisitor
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

/**
 * Entry point responsible to set up the ANTLR lexer and parser, and construct the AST
 */
class ArithmeticExprKolasuParser {
    fun parse(input: String): Expression {
        val lexer = ArithmeticExprLexer(CharStreams.fromString(input))
        val parser = ArithmeticExprParser(CommonTokenStream(lexer))
        return ExpressionBuilder().visitExpr(parser.expr())
    }
}

/**
 * Builder extending ANTLR ParserBaseVisitor, responsible to construct the AST
 */
private class ExpressionBuilder : ArithmeticExprParserBaseVisitor<Expression>() {

    override fun visitExpr(ctx: ArithmeticExprParser.ExprContext): Expression {
        var expr = visit(ctx.term(0))
        for (i in 1 until ctx.term().size) {
            val operator = if (ctx.PLUS(i - 1) != null) "+" else "-"
            val right = visit(ctx.term(i))
            expr = BinaryExpression(expr, operator, right).withPosition(ctx)
        }
        return expr
    }

    override fun visitTerm(ctx: ArithmeticExprParser.TermContext): Expression {
        var term = visit(ctx.factor(0))
        for (i in 1 until ctx.factor().size) {
            val operator = if (ctx.MULT(i - 1) != null) "*" else "/"
            val right = visit(ctx.factor(i))
            term = BinaryExpression(term, operator, right).withPosition(ctx)
        }
        return term
    }

    override fun visitFactor(ctx: ArithmeticExprParser.FactorContext): Expression {
        return when {
            ctx.NUMBER() != null -> NumberLiteral(ctx.NUMBER().text).withPosition(ctx)
            ctx.expr() != null -> ParenthesizedExpression(visit(ctx.expr())).withPosition(ctx)
            else -> throw IllegalStateException("Unexpected factor: ${ctx.text}")
        }
    }

    private fun <T : Expression> T.withPosition(ctx: ParserRuleContext): T {
        this.position = Position(
            start = ctx.start.startPoint,
            end = ctx.stop.endPoint,
        )
        return this
    }

    private val Token.startPoint
        get() = Point(line, charPositionInLine)

    private val Token.endPoint
        get() = Point(line, charPositionInLine + text.length)
}