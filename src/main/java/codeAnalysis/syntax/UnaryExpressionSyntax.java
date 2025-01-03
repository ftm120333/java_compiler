package codeAnalysis.syntax;

import java.util.ArrayList;
import java.util.List;


public class UnaryExpressionSyntax extends  ExpressionSyntax{

    public SyntaxToken operatorToken;
    public ExpressionSyntax operand;

    public UnaryExpressionSyntax( SyntaxToken operatorToken, ExpressionSyntax operand) {

        this.operatorToken = operatorToken;
        this.operand = operand;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.UnaryExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();

        children.add(operatorToken);
        children.add(operand);
        return children;
    }

}
