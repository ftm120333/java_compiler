package codeAnalysis;
import codeAnalysis.binding.*;

import java.util.Map;
import java.util.Objects;

public class Evaluator{
    private final BoundExpression _root;
    private final Map<VariableSymbol, Object> _variables;

    public Evaluator(BoundExpression root, Map<VariableSymbol, Object> variables) {
        _root = root;
        _variables = variables;
    }
    private Object EvaluateExpression(BoundExpression node) throws Exception {
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

    private Object evaluateLiteralExpression( BoundLiteralExpression l) {
        return l.getValue();
    }

    public Object Evaluate() throws Exception {
        return EvaluateExpression(_root);
    }

    private Object evaluateVariableExpression(BoundVariableExpression a){
        return _variables.get(a.getVariable());
    }

    private Object evaluateAssignmentExpression(BoundAssignmentExpression a) throws Exception {
        var value = EvaluateExpression(a.getExpression());
        _variables.put(a.getVariable(),value);
        return value;
    }

    private Object evaluateUnaryExpression(BoundUnaryExpression u) throws Exception {
        var operand =  EvaluateExpression(u.getOperand());
        return switch (u.getOperatorKind().getKind()) {
            case Identity -> (int) operand;
            case Negation -> -(int) operand;
            case LogicalNegation -> !(boolean) operand;
        };
    }

    private Object evaluateBinaryExpression(BoundBinaryExpression b) throws Exception {
        var left =  EvaluateExpression(b.getLeft());
        var right =  EvaluateExpression(b.getRight());
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
