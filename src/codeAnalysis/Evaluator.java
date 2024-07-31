package codeAnalysis;


import codeAnalysis.binding.*;
import codeAnalysis.syntax.*;

public class Evaluator{
    private final BoundExpression root;

    public Evaluator(BoundExpression root) {
        this.root = root;
    }

    public int Evaluate() throws Exception {
        return EvaluateExpression(root);
    }

    private int EvaluateExpression(BoundExpression node) throws Exception {
        if (node instanceof BoundLiteralExpression n) {
            return (int) n.getValue();
        }


        if (node instanceof BoundUnaryExpression u) {
            var operand = EvaluateExpression(u.getOperand());
            if (u.getOperatorKind() == BoundUnaryOperatorKind.Identity) {
                return operand;
            } else if (u.getOperatorKind() == BoundUnaryOperatorKind.Negation) {
                return -operand;
            } else
                throw new Exception("UnExpected unary operator " + u.getOperatorKind());
        }

        if (node instanceof BoundBinaryExpression b) {
            var left = EvaluateExpression(b.getLeft());
            var right = EvaluateExpression(b.getRight());
            return switch (b.getOperatorKind()) {
                case BoundBinaryOperatorKind.Addition -> left + right;
                case BoundBinaryOperatorKind.Subtraction -> left - right;
                case BoundBinaryOperatorKind.Multiplication -> left * right;
                case BoundBinaryOperatorKind.Division -> left / right;
                case null, default -> throw new Exception("UnExpected binary operator " + b.getOperatorKind());
            };
        }

    /*    if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }*/

        throw new Exception("UnExpected node " + node.getKind());

    }


}
