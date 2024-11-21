package codeAnalysis;
import codeAnalysis.binding.*;

import java.util.Map;
import java.util.Objects;

public class Evaluator{
    private final BoundStatement _root;
    private final Map<VariableSymbol, Object> _variables;

    private Object _lastValue;
    public Evaluator(BoundStatement root, Map<VariableSymbol, Object> variables) {
        _root = root;
        _variables = variables;
    }
    public Object Evaluate() throws Exception {
         evaluateStatement(_root);
         return _lastValue;

    }
    private Object evaluateExpression(BoundExpression node) throws Exception {
        switch (node.getKind()){
            case UnaryExpression -> {
                System.out.println("Evaluating: " + node.getKind());
                return evaluateUnaryExpression((BoundUnaryExpression) node);
            }
            case BinaryExpression -> {
                System.out.println("Evaluating: " + node.getKind());
                return evaluateBinaryExpression((BoundBinaryExpression) node);
            }
            case VariableExpression -> {
                System.out.println("Evaluating: " + node.getKind());
                return evaluateVariableExpression((BoundVariableExpression) node);
            }
            case LiteralExpression -> {
                System.out.println("Evaluating: " + node.getKind());
                return evaluateLiteralExpression((BoundLiteralExpression) node);
            }
            case AssignmentExpression -> {
                System.out.println("Evaluating: " + node.getKind());
                return evaluateAssignmentExpression((BoundAssignmentExpression) node);
            }
            default -> throw new Exception("Unexpected node "+ node.getKind());


        }

    }




    public void evaluateStatement(BoundStatement node) {
        switch (node.getKind()){
            case BoundNodeKind.BlockStatement -> {
                evaluateBlockStatement((BoundBlockStatement) node);
                break;
            }
            case  BoundNodeKind.ExpressionStatement -> {
                try {
                    evaluateExpressionStatement((BoundExpressionStatement) node);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            default -> throw new IllegalArgumentException("Unexpected Node Kind " + node.getKind());

        }

    }

    private void evaluateBlockStatement(BoundBlockStatement node) {
        for (var statement : node.getStatements()) {
            evaluateStatement(statement);
        }
    }

    private void evaluateExpressionStatement(BoundExpressionStatement node) throws Exception {
        // Evaluates the expression, but its value is not returned.
        _lastValue = evaluateExpression(node.getExpression());
    }


    private Object evaluateLiteralExpression( BoundLiteralExpression l) {
        return l.getValue();
    }



    private Object evaluateVariableExpression(BoundVariableExpression a){
        System.out.println("Current variables: ");
        _variables.values().forEach(System.out::println);

        return _variables.get(a.getVariable());

    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression a) throws Exception {
        var value = evaluateExpression(a.getExpression());
        _variables.put(a.getVariable(),value);
        System.out.println("Current variables: ");

        _variables.values().forEach(System.out::println);
        return value;
    }

    private Object evaluateUnaryExpression(BoundUnaryExpression u) throws Exception {
        var operand =  evaluateExpression(u.getOperand());
        return switch (u.getOperatorKind().getKind()) {
            case Identity -> (int) operand;
            case Negation -> -(int) operand;
            case LogicalNegation -> !(boolean) operand;
        };
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression b) throws Exception {
        var left =  evaluateExpression(b.getLeft());
        var right =  evaluateExpression(b.getRight());
        return switch (b.getOperatorKind().getKind()) {
            case Addition ->  (int) left +(int) right;
            case Subtraction -> (int) left -(int)  right;
            case Multiplication -> (int) left *  (int) right;
            case Division ->  (int) left / (int)  right;
            case LogicalAnd -> (Boolean) left && (Boolean) right;
            case LogicalOr -> (Boolean) left || (Boolean) right;
            case Equals -> Objects.equals(left, right);
            case NotEquals -> !Objects.equals(left, right);
        };
    }



}
