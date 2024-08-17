package dev.algo.parsetest.kolasuv15

import com.strumenta.kolasu.model.Node

// Base for all our AST nodes
sealed class Expression : Node()

// Binary operations like add, sub, mult, and div.
data class BinaryExpression(
    val left: Expression,
    val operator: String,
    val right: Expression,
) : Expression()

// Numeric values
data class NumberLiteral(
    val value: String
) : Expression()

// Expression in parens
data class ParenthesizedExpression(
    val expr: Expression,
) : Expression()