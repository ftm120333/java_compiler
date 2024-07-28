package main.lexer;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

class BinaryExpressionSyntax extends  ExpressionSyntax{
    ExpressionSyntax left;
    SyntaxToken operatorToken;
    ExpressionSyntax right;

    public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;
    }

    public ExpressionSyntax getLeft() {
        return left;
    }

    public SyntaxToken getOperatorToken() {
        return operatorToken;
    }

    public ExpressionSyntax getRight() {
        return right;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BinaryExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(left);
        children.add(operatorToken);
        children.add(right);
        return children;
    }
}

class ParanthrsizedExpressionSyntax extends ExpressionSyntax {
    SyntaxToken openParenthesisToken;
    ExpressionSyntax expression;
    SyntaxToken closedParenthesisToken;

    public ParanthrsizedExpressionSyntax(SyntaxToken openParenthesisToken, ExpressionSyntax expression, SyntaxToken closedParenthesisToken) {
         this.openParenthesisToken = openParenthesisToken;
         this.expression = expression;
         this.closedParenthesisToken = closedParenthesisToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ParanthrsizedExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(openParenthesisToken);
        children.add(expression);
        children.add(closedParenthesisToken);
        return children;
    }
}


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

        if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }

        throw new Exception("UnExpected node " + node.getKind());

    }


}
