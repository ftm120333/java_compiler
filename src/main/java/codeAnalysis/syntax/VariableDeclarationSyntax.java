package codeAnalysis.syntax;

import java.util.List;

public class VariableDeclarationSyntax extends StatementSyntax {


    private final SyntaxToken keyword;
    private final SyntaxToken identifier;
    private final SyntaxToken equalsToken;
    private final ExpressionSyntax initializer;

    public VariableDeclarationSyntax(SyntaxToken keyword, SyntaxToken identifier, SyntaxToken equalsToken, ExpressionSyntax initializer) {
        this.keyword = keyword;
        this.identifier = identifier;
        this.equalsToken = equalsToken;
        this.initializer = initializer;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.VariableDeclaration;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return List.of();
    }

    public SyntaxToken getKeyword() {
        return keyword;
    }

    public SyntaxToken getIdentifier() {
        return identifier;
    }

    public SyntaxToken getEqualsToken() {
        return equalsToken;
    }

    public ExpressionSyntax getInitializer() {
        return initializer;
    }
}
