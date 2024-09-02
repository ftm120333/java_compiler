package codeAnalysis;


import codeAnalysis.binding.*;

import java.util.Dictionary;
import java.util.Map;
import java.util.Objects;

public class Evaluator{
    private final BoundExpression _root;
    private final Map<VariableSymbol, Object> _variables;

    public Evaluator(BoundExpression root, Map<VariableSymbol, Object> variables) {
        _root = root;
        _variables = variables;
    }

    public Object Evaluate() throws Exception {
        return EvaluateExpression(_root);
    }

    private Object EvaluateExpression(BoundExpression node) throws Exception {
        if (node instanceof BoundLiteralExpression n) {
            return  n.getValue();
        }

        if (node instanceof BoundVariableExpression v) {
            return _variables.get(v);
        }

        if (node instanceof BoundAssignmentExpression a) {
            var value = EvaluateExpression(a.getExpression());
            _variables.put(a.getVariable(),value);
            return value;

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
                case Addition -> (int) left +(int) right;
                case Subtraction -> (int) left -(int)  right;
                case Multiplication -> (int) left *  (int) right;
                case Division -> (int) left / (int)  right;
                case LogicalAnd -> (Boolean) left && (Boolean) right;
                case LogicalOr -> (Boolean) left || (Boolean) right;
                case Equals -> Objects.equals(left, right);
                case NotEquals -> !Objects.equals(left, right);
                default -> throw new Exception("UnExpected binary operator " + b.getOperatorKind());
            };
        }

    /*    if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }*/

        throw new Exception("UnExpected node " + node.getKind());

    }


}
