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

                return evaluateUnaryExpression((BoundUnaryExpression) node);
            }
            case BinaryExpression -> {

                return evaluateBinaryExpression((BoundBinaryExpression) node);
            }
            case VariableExpression -> {

                return evaluateVariableExpression((BoundVariableExpression) node);
            }
            case LiteralExpression -> {

                return evaluateLiteralExpression((BoundLiteralExpression) node);
            }
            case AssignmentExpression -> {

                return evaluateAssignmentExpression((BoundAssignmentExpression) node);
            }
            default -> throw new Exception("Unexpected node "+ node.getKind());


        }

    }




    public void evaluateStatement(BoundStatement node) throws Exception {
        switch (node.getKind()){
            case BoundNodeKind.BlockStatement -> {
                evaluateBlockStatement((BoundBlockStatement) node);
                break;
            }
            case BoundNodeKind.VariableDeclaration -> {
                evaluateVariableDeclaration((BoundVariableDeclaration) node);
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

    private void evaluateVariableDeclaration(BoundVariableDeclaration node) throws Exception {
         var value = evaluateExpression(node.getInitializer());
         _variables.put(node.getVariable(),value);
         _lastValue = value;
    }

    private void evaluateBlockStatement(BoundBlockStatement node) throws Exception {
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

        _variables.values().forEach(System.out::println);

        return _variables.get(a.getVariable());

    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression a) throws Exception {
        var value = evaluateExpression(a.getExpression());
        _variables.put(a.getVariable(),value);


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
