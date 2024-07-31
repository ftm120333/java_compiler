package codeAnalysis;


import codeAnalysis.binding.BoundExpression;
import codeAnalysis.binding.BoundLiteralExpression;
import codeAnalysis.binding.BoundUnaryExpression;
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
            if (u.getOperatorKind().getKind() == SyntaxKind.PlusToken) {
                return operand;
            } else if (u.operatorToken.getKind() == SyntaxKind.MinusToken) {
                return -operand;
            } else
                throw new Exception("UnExpected unary operator " + u.operatorToken.getKind());
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

        if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }

        throw new Exception("UnExpected node " + node.getKind());

    }


}
