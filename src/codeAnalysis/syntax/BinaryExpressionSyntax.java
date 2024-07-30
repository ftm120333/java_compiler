package codeAnalysis.syntax;

import java.util.ArrayList;
import java.util.List;

public class BinaryExpressionSyntax extends  ExpressionSyntax{
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
