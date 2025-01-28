package codeAnalysis.syntax;

import java.util.List;

public class ExpressionStatementSyntax extends StatementSyntax {
    private final ExpressionSyntax expression;


    public ExpressionStatementSyntax(ExpressionSyntax expression) {
        this.expression = expression;

    }

    public ExpressionSyntax getExpression() {
        return expression;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ExpressionStatement;
    }
    @Override
    public List<SyntaxNode> GetChildren() {
         return List.of(expression);
    }

}
