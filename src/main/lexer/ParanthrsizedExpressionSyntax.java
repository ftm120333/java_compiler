package main.lexer;


import java.util.ArrayList;
import java.util.List;

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

