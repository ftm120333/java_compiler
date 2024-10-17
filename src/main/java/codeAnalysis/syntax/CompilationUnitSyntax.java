package codeAnalysis.syntax;

import java.util.List;

public class CompilationUnitSyntax extends SyntaxNode {
    ExpressionSyntax expression;
    SyntaxToken endOfFileToken;

    public CompilationUnitSyntax(ExpressionSyntax expression, SyntaxToken endOfFileToken) {
        this.expression = expression;
        this.endOfFileToken = endOfFileToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.CompilationUnit;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return null;
    }

    public ExpressionSyntax getExpression() {
        return expression;
    }

    public SyntaxToken getEndOfFileToken() {
        return endOfFileToken;
    }
}
