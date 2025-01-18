package codeAnalysis;

import codeAnalysis.binding.*;
import java.util.Map;
import java.util.Objects;

public class Evaluator {
    private final BoundStatement _root;
    private final Map<String, Object> _variables; // Store variable names and their values
    private Object _lastValue;

    public Evaluator(BoundStatement root, Map<String, Object> variables) {
        _root = root;
        _variables = variables;
    }

    public Object evaluate() throws Exception {
        evaluateStatement(_root);
        return _lastValue;
    }

    private void evaluateStatement(BoundStatement node) throws Exception {

        switch (node.getKind()) {
            case BoundNodeKind.BlockStatement -> evaluateBlockStatement((BoundBlockStatement) node);
            case BoundNodeKind.VariableDeclaration -> evaluateVariableDeclaration((BoundVariableDeclaration) node);
            case BoundNodeKind.ExpressionStatement -> evaluateExpressionStatement((BoundExpressionStatement) node);
            default -> throw new IllegalArgumentException("Unexpected Node Kind " + node.getKind());
        }
    }

    private void evaluateVariableDeclaration(BoundVariableDeclaration node) throws Exception {
        var value = evaluateExpression(node.getInitializer());
        _variables.put(node.getVariable().getName(), value);
        _lastValue = value;
    }

    private void evaluateBlockStatement(BoundBlockStatement node) throws Exception {
        for (var statement : node.getStatements()) {
            evaluateStatement(statement);
        }
    }

    private void evaluateExpressionStatement(BoundExpressionStatement node) throws Exception {
        _lastValue = evaluateExpression(node.getExpression());
    }

    private Object evaluateExpression(BoundExpression node) throws Exception {
        return switch (node.getKind()) {
            case UnaryExpression -> evaluateUnaryExpression((BoundUnaryExpression) node);
            case BinaryExpression -> evaluateBinaryExpression((BoundBinaryExpression) node);
            case VariableExpression -> evaluateVariableExpression((BoundVariableExpression) node);
            case LiteralExpression -> evaluateLiteralExpression((BoundLiteralExpression) node);
            case AssignmentExpression -> evaluateAssignmentExpression((BoundAssignmentExpression) node);
            default -> throw new Exception("Unexpected node " + node.getKind());
        };
    }

    private Object evaluateLiteralExpression(BoundLiteralExpression node) {

        return node.getValue();
    }

    private Object evaluateVariableExpression(BoundVariableExpression node) {
        return _variables.getOrDefault(node.getVariable().getName(), 0);
    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression node) throws Exception {
  
        var variable = node.getVariable();
        System.out.println("Variable: " + variable.getName() + ", isReadOnly: " + variable.isReadOnly());
    
        if (variable.isReadOnly()) {
            throw new Exception("Variable " + variable.getName() + " is read-only and cannot be assigned.");
        }
    
        var value = evaluateExpression(node.getExpression());
        _variables.put(variable.getName(), value);
        return value;

    }

   

    private Object evaluateUnaryExpression(BoundUnaryExpression node) throws Exception {
        var operand = evaluateExpression(node.getOperand());
        return switch (node.getOperatorKind().getKind()) {
            case Identity -> (int) operand;
            case Negation -> -(int) operand;
            case LogicalNegation -> !(boolean) operand;
        };
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression node) throws Exception {
        var left = evaluateExpression(node.getLeft());
        System.out.println();
        var right = evaluateExpression(node.getRight());

        return switch (node.getOperatorKind().getKind()) {
            case Addition -> (int) left + (int) right;
            case Subtraction -> (int) left - (int) right;
            case Multiplication -> (int) left * (int) right;
            case Division -> (int) left / (int) right;
            case LogicalAnd -> (boolean) left && (boolean) right;
            case LogicalOr -> (boolean) left || (boolean) right;
            case Equals -> Objects.equals(left, right);
            case NotEquals -> !Objects.equals(left, right);
            default -> throw new Exception("Unexpected operator kind: " + node.getOperatorKind().getKind());
        };
    }
}
