package main.lexer;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;


public class Evaluator{
    private final ExpressionSyntax root;

    public Evaluator(ExpressionSyntax root) {
        this.root = root;
    }

    public int Evaluate() throws Exception {
        return EvaluateExpression(root);
    }

    private int EvaluateExpression(ExpressionSyntax node) throws Exception {
        if (node instanceof NumberExpressionSyntax n) {
            return (int) n.getNumberToken().value;
        }

        if (node instanceof BinaryExpressionSyntax b) {
            var left = EvaluateExpression(b.getLeft());
            var right = EvaluateExpression(b.getRight());
            if (b.getOperatorToken().kind == SyntaxKind.PlusToken) {
                return left + right;
            } else if (b.getOperatorToken().kind == SyntaxKind.MinusToken) {
                return left - right;
            } else if (b.getOperatorToken().kind == SyntaxKind.StarToken) {
                return left * right;
            } else if (b.getOperatorToken().kind == SyntaxKind.SlashToken) {
                return left / right;
            }else
                throw new Exception("UnExpected binary operator " + b.getOperatorToken().kind);
        }

        if (node instanceof UnaryExpressionSyntax u) {
            var operand = EvaluateExpression(u.operand);
            if (u.operatorToken.getKind() == SyntaxKind.PlusToken) {
                return operand;
            } else if (u.operatorToken.getKind() == SyntaxKind.MinusToken) {
                return -operand;
            } else
                throw new Exception("UnExpected unary operator " + u.operatorToken.getKind());
        }
        if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }

        throw new Exception("UnExpected node " + node.getKind());

    }


}
