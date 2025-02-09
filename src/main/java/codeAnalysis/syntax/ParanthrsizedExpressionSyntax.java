package codeAnalysis.syntax;


import java.util.ArrayList;
import java.util.List;

public class ParanthrsizedExpressionSyntax extends ExpressionSyntax {
    SyntaxToken openParenthesisToken;
    public ExpressionSyntax expression;
    SyntaxToken closedParenthesisToken;

    public ParanthrsizedExpressionSyntax(SyntaxToken openParenthesisToken, ExpressionSyntax expression, SyntaxToken closedParenthesisToken) {
        this.openParenthesisToken = openParenthesisToken;
        this.expression = expression;
        this.closedParenthesisToken = closedParenthesisToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ParanthesizedExpression;
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

