package codeAnalysis;


import codeAnalysis.binding.*;
import codeAnalysis.syntax.*;

public class Evaluator{
    private final BoundExpression root;

    public Evaluator(BoundExpression root) {
        this.root = root;
    }

    public Object Evaluate() throws Exception {
        return EvaluateExpression(root);
    }

    private Object EvaluateExpression(BoundExpression node) throws Exception {
        if (node instanceof BoundLiteralExpression n) {
            return (int) n.getValue();
        }


        if (node instanceof BoundUnaryExpression u) {
            var operand =  EvaluateExpression(u.getOperand());
            return switch (u.getOperatorKind().getKind()) {
                case Identity -> (int) operand;
                case Negation -> -(int) operand;
                case LogicalNegation -> !(boolean) operand;
                case null, default -> throw new Exception("UnExpected unary operator " + u.getOperatorKind());
            };
        }

        if (node instanceof BoundBinaryExpression b) {
            var left =  EvaluateExpression(b.getLeft());
            var right =  EvaluateExpression(b.getRight());
            return switch (b.getOperatorKind().getKind()) {
                case BoundBinaryOperatorKind.Addition -> (int) left +(int) right;
                case BoundBinaryOperatorKind.Subtraction -> (int) left -(int)  right;
                case BoundBinaryOperatorKind.Multiplication -> (int) left *  (int) right;
                case BoundBinaryOperatorKind.Division -> (int) left / (int)  right;
                case BoundBinaryOperatorKind.LogicalAnd -> (Boolean) left && (Boolean) right;
                case BoundBinaryOperatorKind.LogicalOr -> (Boolean) left || (Boolean) right;
                case null, default -> throw new Exception("UnExpected binary operator " + b.getOperatorKind());
            };
        }

    /*    if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }*/

        throw new Exception("UnExpected node " + node.getKind());

    }


}
